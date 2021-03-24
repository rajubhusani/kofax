package com.fab.kofax

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fab.kofax.common.CommonUtils.cleanupImageObj
import com.fab.kofax.common.CommonUtils.formatAnalysisMessage
import com.fab.kofax.component.ComponentManager
import com.fab.kofax.databinding.CapturePhotoBinding
import com.fab.kofax.uicontrols.PageDetectionOverlay
import com.fab.kofax.utils.ApplicationState
import com.fab.kofax.utils.ConstValues
import com.fab.kofax.utils.Enums
import com.fab.kofax.utils.InMemoryKfxImages
import com.kofax.kmc.ken.engines.ImageProcessor
import com.kofax.kmc.ken.engines.ImageProcessor.*
import com.kofax.kmc.ken.engines.data.*
import com.kofax.kmc.ken.engines.processing.ImageProcessorConfiguration
import com.kofax.kmc.kui.uicontrols.*
import com.kofax.kmc.kui.uicontrols.captureanimations.CheckCaptureExperience
import com.kofax.kmc.kui.uicontrols.captureanimations.CheckCaptureExperienceCriteriaHolder
import com.kofax.kmc.kui.uicontrols.data.Flash
import com.kofax.kmc.kut.utilities.Size
import com.kofax.kmc.kut.utilities.error.ErrorInfo
import com.kofax.kmc.kut.utilities.error.KmcException
import com.kofax.kmc.kut.utilities.error.KmcRuntimeException
import com.kofax.mobile.sdk.capture.check.CheckCaptureActivity
import org.json.JSONException
import java.util.*

class CheckCaptureActivity : AppCompatActivity(),
        ImageCapturedEventListener,
        CameraInitializationListener,
        CameraInitializationFailedListener,
        PageDetectionListener,
        ImageOutListener,
        AnalysisCompleteListener {
    private var mExperience: CheckCaptureExperience? = null
    private var imageProcessor: ImageProcessor? = null
    private var mCaptureMode = Enums.CaptureMode.VIDEO.ordinal
    private var mPageDetectionOverlayHandler: Handler? = null
    private var mPageDetectionOverlayRunnable: Runnable? = null

    private var timer: Timer? = null
    private var mPageDetectionOverlay: PageDetectionOverlay? = null

    private lateinit var binding: CapturePhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CapturePhotoBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        initializeListeners()
        setUp()
        prepareBaseCaptureExperience()
    }

    private fun setUp() {
        if (null == ConstValues.mAppObject) {
            ConstValues.mAppObject = ApplicationState()
            val componentManager = ComponentManager(this)
            val component = componentManager.component
            ConstValues.mAppObject!!.mActiveComponent = component
        }

        if(InMemoryKfxImages.scanType.equals(Enums.ScanType.BACK.name, true)) {
            ConstValues.mAppObject!!.mCaptureState = Enums.CaptureState.BACK
        } else {
            ConstValues.mAppObject!!.mCaptureState = Enums.CaptureState.FRONT
        }

        ConstValues.mAppObject!!.mFrontState = Enums.ImageStatus.NONE
        ConstValues.mAppObject!!.mBackState = Enums.ImageStatus.NONE
        imageProcessor = ImageProcessor()
        val criteriaHolder = initCheckCaptureExpCriteriaHolder()
        mExperience = CheckCaptureExperience(binding.imageCaptureView)
        mExperience!!.captureCriteria = criteriaHolder
        mExperience!!.enableAnimationTutor(true)
        mExperience!!.addOnImageCapturedEventListener(this)
        addPageDetectionListener()
        initPreview()
    }

    private fun initializeListeners() {
        binding.tglBtnTorch.setOnCheckedChangeListener { _, isChecked ->
            try {
                if (isChecked) {
                    binding.imageCaptureView.flash = Flash.TORCH
                } else {
                    binding.imageCaptureView.flash = Flash.OFF
                }
            } catch (e: Exception) {
                binding.tglBtnTorch.isChecked = false
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        binding.rescanBtn.setOnClickListener { finishReview() }
        binding.useBtn.setOnClickListener {
            useBtnAction()
        }
        binding.backIcon.setOnClickListener{
            sendFailedResult()
            finish()
        }
    }

    private fun useBtnAction(){
        if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.FRONT &&
                ConstValues.mAppObject!!.mFrontState == Enums.ImageStatus.PROCESSED) {
            ConstValues.mAppObject!!.mCaptureState = Enums.CaptureState.BACK
        } else if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.BACK &&
                ConstValues.mAppObject!!.mBackState == Enums.ImageStatus.PROCESSED) {
            ConstValues.mAppObject!!.mCaptureState = Enums.CaptureState.FRONT
        }

        if(InMemoryKfxImages.scanType.equals(Enums.ScanType.BOTH.name, true) &&
                ConstValues.mAppObject!!.mFrontState == Enums.ImageStatus.PROCESSED &&
                        ConstValues.mAppObject!!.mBackState == Enums.ImageStatus.PROCESSED){
            sendSuccessResult()
        } else if(InMemoryKfxImages.scanType.equals(Enums.ScanType.FRONT.name, true) &&
                ConstValues.mAppObject!!.mFrontState == Enums.ImageStatus.PROCESSED){
            sendSuccessResult()
        }else if(InMemoryKfxImages.scanType.equals(Enums.ScanType.BACK.name, true) &&
                ConstValues.mAppObject!!.mBackState == Enums.ImageStatus.PROCESSED){
            sendSuccessResult()
        } else {
            finishReview()
        }
    }

    private fun initPreview() {
        binding.imageCaptureView.addCameraInitializationListener(this)
        binding.imageCaptureView.addCameraInitializationFailedListener(this)
        binding.imageCaptureView.useVideoFrame = mCaptureMode == Enums.CaptureMode.VIDEO.ordinal
        binding.imageCaptureView.sessionCreate()
    }

    private fun reviewImage(image: Image) {
        binding.imageReview.visibility = View.VISIBLE
        binding.imageCaptureView.visibility = View.GONE
        binding.captureMenu.visibility = View.GONE
        binding.reviewFrame.visibility = View.VISIBLE
        binding.captureUserHint.visibility = View.GONE
        try {
            binding.imageReview.image = image
        } catch (e: KmcException) {
            Log.e(TAG, "Exception", e)
            Toast.makeText(this, resources.getString(R.string.review_error),
                    Toast.LENGTH_LONG).show()
        }
    }

    private fun finishReview(): Boolean {
        if (binding.imageReview.visibility != View.VISIBLE) {
            return false
        }

        binding.imageReview.visibility = View.GONE
        binding.imageCaptureView.visibility = View.VISIBLE
        binding.reviewFrame.visibility = View.GONE
        binding.captureUserHint.visibility = View.VISIBLE
        initCaptureState()
        return true
    }

    private fun processImage(image: Image?) {
        imageProcessor = ImageProcessor()
        // Set the listener to the image processor
        imageProcessor!!.addImageOutEventListener(this)
        try {
            // Use the image processing string profile
            val processString = CapturedImages.FRONT_IMG_PROCESS
            val configuration = ImageProcessorConfiguration(processString)

            imageProcessor!!.processImage(image, configuration)
        } catch (e: KmcException) {
            Log.e(TAG, "Exception", e)
            Toast.makeText(this, resources.getString(R.string.process_error),
                    Toast.LENGTH_LONG).show()
        } catch (e: java.lang.Exception){
            Log.e(TAG, "Exception", e)
        }
        binding.progressLayout.visibility = View.VISIBLE
    }

    private fun isHighResolution(sz: Size): Boolean {
        return sz.width >= 720 && sz.height >= 720
    }

    override fun pageDetected(pageDetectionEvent: PageDetectionEvent) {}
    override fun onCameraInitialized(event: CameraInitializationEvent) {
        if (event.cameraInitStatus !=
                CameraInitializationEvent.CameraInitStatus.CAMERA_VIEW_CREATED) {
            return
        }
        initFlash()
        if (mCaptureMode == Enums.CaptureMode.VIDEO.ordinal) {
            var isHighResolution = false
            for (sz in binding.imageCaptureView.allowableImageResolutions) {
                if (isHighResolution(sz)) {
                    isHighResolution = true
                    break
                }
            }
            if (!isHighResolution) {
                binding.imageCaptureView.useVideoFrame = false
            }
        }
    }

    private fun initFlash() {
        if (CapturedImages.autoTorch && binding.imageCaptureView.isFlashSupported(Flash.AUTOTORCH)) {
//            binding.tglBtnTorch.visibility = View.INVISIBLE
            binding.imageCaptureView.flash = Flash.AUTOTORCH
        }
//        else if (binding.imageCaptureView.isFlashSupported(Flash.TORCH)) {
//            binding.tglBtnTorch.visibility = View.VISIBLE
//            binding.tglBtnTorch.isChecked = false
//        } else {
//            binding.tglBtnTorch.visibility = View.INVISIBLE
//        }
    }

    override fun onCameraInitializationFailed(event: CameraInitializationFailedEvent) {
        var message = event.cause.message
        if (TextUtils.isEmpty(message)) {
            message = resources.getString(R.string.cameraunavailable)
        }
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onImageCapturedEvent(iImagesCaptured: IImagesCaptured) {
        if (iImagesCaptured.primaryImage == null ||
                iImagesCaptured.primaryImage.imageBitmap == null) {
            return
        }
        val primaryImage = Image(Bitmap.createBitmap(iImagesCaptured.primaryImage.imageBitmap))
        primaryImage.targetFrame = iImagesCaptured.primaryImage.targetFrame
        var glareFreeImage: Image? = null
        if (iImagesCaptured.glareFreeImage != null &&
                iImagesCaptured.glareFreeImage.imageBitmap != null) {
            glareFreeImage = Image(Bitmap.createBitmap(iImagesCaptured.glareFreeImage.imageBitmap))
            glareFreeImage.targetFrame = iImagesCaptured.glareFreeImage.targetFrame
        }
        updateAppObj(primaryImage, glareFreeImage)
        primaryImage.imageMimeType = Image.ImageMimeType.MIMETYPE_TIFF
        imageProcessor!!.processedImageRepresentation = Image.ImageRep.IMAGE_REP_BITMAP
        doQuickAnalysis(iImagesCaptured.primaryImage)
    }

    private fun updateAppObj(primaryImage: Image?, glareFreeImage: Image?) {
        if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.FRONT) {
            if (glareFreeImage != null && glareFreeImage.imageBitmap != null) {
                InMemoryKfxImages.mFrontRawImage = glareFreeImage
                InMemoryKfxImages.mFrontRawPrimaryImage = primaryImage
            } else if (primaryImage != null) {
                InMemoryKfxImages.mFrontRawImage = primaryImage
                InMemoryKfxImages.mFrontRawPrimaryImage = null
            }
        } else {
            if (primaryImage != null) {
                InMemoryKfxImages.mBackRawImage = primaryImage
            }
        }
        System.gc()
    }

    private fun configureDetectionSettings(settings: DetectionSettings) {
        val cameraSettings = ConstValues.mAppObject!!.mActiveComponent!!.settings!!.camerasettings
        settings.targetFramePaddingPercent = cameraSettings.TargetFramePadding
        settings.maxFillFraction = cameraSettings.MaxFillFraction.toDouble()
        settings.minFillFraction = cameraSettings.MinFillFraction.toDouble()
        settings.maxSkewAngle = cameraSettings.MaxSkewAngle.toDouble()
        settings.toleranceFraction = cameraSettings.ToleranceFranction.toDouble()
        settings.targetFrameAspectRatio = cameraSettings.AspectRatio.toDouble()
        ConstValues.mAppObject!!.mActiveComponent!!.settings!!.camerasettings = cameraSettings
    }

    private fun configureCheckDetectorSettings(): CheckDetectionSettings {
        val checkDetectionSettings = CheckDetectionSettings()
        configureDetectionSettings(checkDetectionSettings)
        checkDetectionSettings.side =
                if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.FRONT)
                    CheckSide.FRONT
                else
                    CheckSide.BACK
        if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.BACK &&
                InMemoryKfxImages.mFrontProcessedImage != null &&
                InMemoryKfxImages.mFrontProcessedImage!!.imageBitmap != null) {
            checkDetectionSettings.targetFrameAspectRatio =
                    InMemoryKfxImages.mFrontProcessedImage!!.imageBitmapWidth.toFloat() /
                            InMemoryKfxImages.mFrontProcessedImage!!.imageBitmapHeight.toDouble()
        }
        return checkDetectionSettings
    }

    private fun initCheckCaptureExpCriteriaHolder(): CheckCaptureExperienceCriteriaHolder {
        val criteriaHolder = CheckCaptureExperienceCriteriaHolder()
        val checkDetectionSettings = configureCheckDetectorSettings()
        criteriaHolder.checkDetectionSettings = checkDetectionSettings
        criteriaHolder.isStabilityThresholdEnabled = CapturedImages.isStabilityThresholdEnabled
        criteriaHolder.stabilityThreshold = CapturedImages.stabilityThreshold
        criteriaHolder.isPitchThresholdEnabled = CapturedImages.isPitchThresholdEnabled
        criteriaHolder.pitchThreshold = CapturedImages.pitchThreshold
        criteriaHolder.isRollThresholdEnabled = CapturedImages.isRollThresholdEnabled
        criteriaHolder.rollThreshold = CapturedImages.rollThreshold
        criteriaHolder.isFocusEnabled = CapturedImages.isFocusEnabled
        return criteriaHolder
    }

    private fun prepareBaseCaptureExperience() {
        try {
            if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.FRONT) {
                binding.captureUserHint.text = resources.getString(R.string.camera_view_front_hint_text)
                mExperience!!.takePictureContinually()
            } else {
                binding.captureUserHint.text = resources.getString(R.string.camera_view_back_hint_text)
                mExperience!!.takePicture()

            }
        } catch (e: RuntimeException) {
            Toast.makeText(this, CapturedImages.CAPTURE_EXCEPTION + e.message,
                    Toast.LENGTH_SHORT).show()
            Log.e(TAG, "RuntimeException ::  " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception occurred " + e.message)
            Toast.makeText(this, CapturedImages.CAPTURE_EXCEPTION + e.message,
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun addPageDetectionListener() {
        if (ConstValues.mAppObject!!.mActiveComponent!!.settings!!.camerasettings.ShowPageDetection) {
            mPageDetectionOverlay = PageDetectionOverlay(this)
            mPageDetectionOverlay!!.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT)
            binding.rlCaptureLayout.addView(mPageDetectionOverlay)
            mExperience!!.addPageDetectionListener(this)
            mPageDetectionOverlayRunnable = Runnable {
                if (mPageDetectionOverlay != null) {
                    mPageDetectionOverlay!!.postInvalidate()
                }
                if (mPageDetectionOverlayHandler != null && mPageDetectionOverlayRunnable != null) {
                    mPageDetectionOverlayHandler!!.postDelayed(mPageDetectionOverlayRunnable, 50)
                }
            }
            removePageDetectionCallbacks()
            mPageDetectionOverlayHandler = Handler()
            mPageDetectionOverlayHandler!!.postDelayed(mPageDetectionOverlayRunnable, 50)
        } else {
            mExperience!!.removePageDetectionListener(this)
            if (mPageDetectionOverlay != null) {
                binding.rlCaptureLayout.removeView(mPageDetectionOverlay)
            }
            removePageDetectionCallbacks()
        }
    }

    private fun removePageDetectionCallbacks() {
        if (mPageDetectionOverlayHandler != null && mPageDetectionOverlayRunnable != null) {
            mPageDetectionOverlayHandler!!.removeCallbacks(mPageDetectionOverlayRunnable)
            mPageDetectionOverlayHandler = null
            mPageDetectionOverlayRunnable = null
        }
    }

    private fun unbindCaptureData() {
        deInitPreview()
        removePageDetectionCallbacks()
        if (ConstValues.mAppObject != null) {
            ConstValues.mAppObject!!.mCaptureActivityDispalyed = false
        }
    }

    private fun deInitPreview() {
        if (mExperience != null) {
            mExperience!!.removeOnImageCapturedEventListener(this)
            mExperience!!.removePageDetectionListener(this)
        }
        if (mPageDetectionOverlay != null) {
            binding.rlCaptureLayout.removeView(mPageDetectionOverlay)
            mPageDetectionOverlay = null
        }
        binding.cpMainparentLayout.invalidate()
        if (null != timer) {
            timer!!.cancel()
            timer = null
        }
        binding.imageCaptureView.removeCameraInitializationListener(this)
        binding.imageCaptureView.removeCameraInitializationFailedListener(this)
        binding.imageCaptureView.sessionDismiss()

    }

    override fun imageOut(imageOutEvent: ImageOutEvent) {
        imageProcessor!!.removeImageOutEventListener(this)
        binding.progressLayout.visibility = View.GONE
        if (imageOutEvent.status == ErrorInfo.KMC_SUCCESS) {
            if (imageOutEvent.image != null) {
                try {
                    Log.d("========*******", "MICR:::" + imageOutEvent.image.micrData)
                    if (TextUtils.isEmpty(imageOutEvent.image.micrData)) {
                        if(ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.FRONT){
                            Toast.makeText(this, resources.getString(R.string.invalid_front_scan_error), Toast.LENGTH_LONG).show()
                            initCaptureState()
                            return
                        }
                        ConstValues.mAppObject!!.mCaptureState = Enums.CaptureState.BACK
                    } else {
                        if(ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.BACK
                                && CapturedImages.validateMICRLine(imageOutEvent.image.micrData)){
                            Toast.makeText(this, resources.getString(R.string.invalid_scan_error), Toast.LENGTH_LONG).show()
                            initCaptureState()
                            return
                        }
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    if(ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.FRONT){
                        Toast.makeText(this, resources.getString(R.string.invalid_front_scan_error), Toast.LENGTH_LONG).show()
                        initCaptureState()
                        return
                    }
                    ConstValues.mAppObject!!.mCaptureState = Enums.CaptureState.BACK
                }
                captureProcessedImage(imageOutEvent.image)
            } else {
                failedProcessImage()
            }
        } else {
            failedProcessImage()
        }
        reviewImage(imageOutEvent.image)
    }

    private fun captureProcessedImage(image: Image){
        if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.FRONT) {
            cleanupImageObj(InMemoryKfxImages.mFrontRawImage)
            cleanupImageObj(InMemoryKfxImages.mFrontProcessedImage)
            System.gc()
            InMemoryKfxImages.mFrontProcessedImage = image
            InMemoryKfxImages.mFrontProcessedImage?.imageMimeType =
                    Image.ImageMimeType.MIMETYPE_JPEG
            ConstValues.mAppObject!!.mFrontState = Enums.ImageStatus.PROCESSED
            InMemoryKfxImages.mMicrData = image.micrData
            Log.d(TAG, "MetaData:::" + InMemoryKfxImages.mFrontProcessedImage?.imageMetaData)
        } else {
            cleanupImageObj(InMemoryKfxImages.mBackRawImage)
            cleanupImageObj(InMemoryKfxImages.mBackProcessedImage)
            System.gc()
            InMemoryKfxImages.mBackProcessedImage = image
            InMemoryKfxImages.mBackProcessedImage?.imageMimeType =
                    Image.ImageMimeType.MIMETYPE_JPEG
            ConstValues.mAppObject!!.mBackState = Enums.ImageStatus.PROCESSED
            Log.d(TAG, "MetaData:::" + InMemoryKfxImages.mBackProcessedImage?.imageMetaData)
        }
    }

    private fun failedProcessImage(){
        if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.FRONT) {
            ConstValues.mAppObject!!.mFrontState = Enums.ImageStatus.NONE
        } else {
            ConstValues.mAppObject!!.mBackState = Enums.ImageStatus.NONE
        }
    }

    private fun doQuickAnalysis(analysisImage: Image): Boolean {
        imageProcessor = ImageProcessor()
        try {
            registerQuickAnalysisListener()
            val settings = QuickAnalysisSettings()
            settings.shadowDetection = CapturedImages.shadowDetection
            settings.glareDetection = CapturedImages.glareDetection
            settings.blurDetection = CapturedImages.blurDetection
            settings.missingBordersDetection = CapturedImages.missingBordersDetection
            settings.skewDetection = CapturedImages.skewDetection
            settings.saturationDetection = CapturedImages.saturationDetection
            settings.lowContrastBackgroundDetection = CapturedImages.lowContrastBackgroundDetection
            settings.glareDetectedThreshold = CapturedImages.glareDetectionThreshold
            settings.glareDetectionIntensityFraction = CapturedImages.glareDetectionIntensityFraction
            settings.glareDetectionIntensityThreshold = CapturedImages.glareDetectionIntensityThreshold
            settings.glareDetectionMinimumGlareAreaFraction = CapturedImages.glareDetectionMinimumGlareAreaFraction
            settings.glareDetectionNumberOfTiles = CapturedImages.glareDetectionNumberOfTiles
            imageProcessor?.doQuickAnalysis(analysisImage, false, settings)
        } catch (e: KmcException) {
            unRegisterQuickAnalysisListener()
            e.printStackTrace()
        } catch (e: KmcRuntimeException) {
            unRegisterQuickAnalysisListener()
            e.printStackTrace()
        }
        return true
    }

    private fun registerQuickAnalysisListener() {
        if (imageProcessor != null) {
            imageProcessor?.addAnalysisCompleteEventListener(this)
        }
    }

    private fun unRegisterQuickAnalysisListener() {
        if (imageProcessor != null) {
            imageProcessor!!.removeAnalysisCompleteEventListener(this)
        }
    }

    override fun analysisComplete(arg0: AnalysisCompleteEvent) {
        if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.FRONT) {
            if (InMemoryKfxImages.mFrontRawImage != null &&
                    InMemoryKfxImages.mFrontRawImage!!.imageQuickAnalysisFeedBack != null) {
                ConstValues.mFrontImageFeedback = InMemoryKfxImages.mFrontRawImage!!.imageQuickAnalysisFeedBack
            } else {
                ConstValues.mFrontImageFeedback = null
            }
            processAnalysisResult(InMemoryKfxImages.mFrontRawImage)
        } else if (ConstValues.mAppObject!!.mCaptureState == Enums.CaptureState.BACK) {
            if (InMemoryKfxImages.mBackRawImage != null &&
                    InMemoryKfxImages.mBackRawImage!!.imageQuickAnalysisFeedBack != null) {
                ConstValues.mBackImageFeedback = InMemoryKfxImages.mBackRawImage!!.imageQuickAnalysisFeedBack
            } else {
                ConstValues.mBackImageFeedback = null
            }
            processAnalysisResult(InMemoryKfxImages.mBackRawImage)
        }
    }

    private fun processAnalysisResult(image: Image?) {
        var txt = getString(R.string.THE_IMAGE_IS) + " "
        if (image != null && image.imageQuickAnalysisFeedBack != null) {
            txt = formatAnalysisMessage(this, image.imageQuickAnalysisFeedBack,
                    CapturedImages.lowContrastBackgroundDetection)
        }
        unRegisterQuickAnalysisListener()
        if (!txt.contentEquals(getString(R.string.THE_IMAGE_IS) + " ")) {
            Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
            initCaptureState()
        } else {
            processImage(image)
        }
    }

    private fun initCaptureState(){
        val criteriaHolder = initCheckCaptureExpCriteriaHolder()
        mExperience!!.captureCriteria = criteriaHolder
        prepareBaseCaptureExperience()
    }

    private fun sendSuccessResult(){
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun sendFailedResult(){
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    override fun onBackPressed() {
        if (finishReview()) {
            return
        }

        sendFailedResult()
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindCaptureData()
        if (mExperience != null) {
            mExperience!!.destroy()
        }
    }

    companion object {
        private val TAG = CheckCaptureActivity::class.java.simpleName
    }
}

