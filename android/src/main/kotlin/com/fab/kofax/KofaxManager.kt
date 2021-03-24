package com.fab.kofax

import android.content.Context
import android.graphics.*
import android.util.Base64
import androidx.annotation.ColorInt
import com.kofax.kmc.kut.utilities.AppContextProvider
import com.kofax.kmc.kut.utilities.Licensing
import com.kofax.kmc.kut.utilities.error.ErrorInfo
import java.io.ByteArrayOutputStream

class KofaxManager {

    fun setContextAndLicense(applicationContext: Context?): Boolean {
        AppContextProvider.setContext(applicationContext)
        return when (Licensing.setMobileSDKLicense(BuildConfig.LICENSE)) {
            ErrorInfo.KMC_EV_LICENSE_EXPIRED -> false
            ErrorInfo.KMC_EV_LICENSING -> false
            else -> true
        }
    }
}

data class CapturedImages(val front: String?,
                          val back: String?,
                          val micr: String?) {
    override fun toString(): String {
        return "{\"${FRONT}\": \"$front\", \"${BACK}\": \"$back\", \"$MICR\": \"$micr\"}"
    }

    companion object {
        const val FRONT_IMG_PROCESS = "_DeviceType_2_DoSkewCorrectionPage__DoCropCorrection__Do90DegreeRotation_9_DoScaleImageToDPI_300_DoRecognizeTextMICR__DocDimLarge_8.27"
        const val MOCK_SCAN = "mockScan"
        const val CHEQUE_SCAN = "chequeScan"
        const val RETAKE_FRONT = "retakeFront"
        const val RETAKE_BACK = "retakeBack"
        const val MOCK_CHEQUE_FRONT = "cheque_front.jpg"
        const val MOCK_CHEQUE_BACK = "cheque_back.jpg"
        const val CAPTURE_STATUS = "CAPTURE_STATUS"
        const val FRONT = "front"
        const val BACK = "back"
        const val MICR = "micr"
        private const val micrLineLength = 30
        const val pitchThreshold = 15
        const val rollThreshold = 15
        const val stabilityThreshold = 85
        const val glareDetectionThreshold = 0.04
        const val glareDetectionIntensityFraction = 0.03
        const val glareDetectionIntensityThreshold = 230
        const val glareDetectionMinimumGlareAreaFraction = 0.03
        const val glareDetectionNumberOfTiles = 100
        const val shadowDetection = true
        const val glareDetection = true
        const val blurDetection = true
        const val missingBordersDetection = true
        const val skewDetection = true
        const val saturationDetection = true
        const val lowContrastBackgroundDetection = true
        const val isStabilityThresholdEnabled = true
        const val isPitchThresholdEnabled = true
        const val isRollThresholdEnabled = true
        const val isFocusEnabled = true
        const val autoTorch = false
        const val MOCK_MICR = "P212313P124512412CP4838383838P"
        const val FRONT_CHECK_ASPECT_RATIO = 2.18f
        const val CAPTURE_EXCEPTION = "Capture exception: "
        const val SCAN_TYPE = "scanType"

        fun convertImageToBase64(context: Context?, bitmap: Bitmap?, addEndorsement: Boolean): String? {
            val newBitmap = if(addEndorsement) addWatermark(bitmap, WatermarkOptions(), context) else bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            newBitmap?.compress(Bitmap.CompressFormat.JPEG, 95, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.NO_WRAP)
        }

        fun validateMICRLine(micrLine: String?): Boolean {
            return micrLine?.length == micrLineLength
        }

        private fun addWatermark(
                bitmap: Bitmap?,
                options: WatermarkOptions,
                context: Context?
        ): Bitmap? {
            val line1 = context?.resources?.getString(R.string.endorsement_line1)
            val line2 = context?.resources?.getString(R.string.endorsement_line2)
            val line3 = context?.resources?.getString(R.string.endorsement_line3)
            val result = bitmap!!.copy(bitmap.config, true)
            val canvas = Canvas(result!!)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
            paint.textAlign = Paint.Align.LEFT
            val textSize = result.width * options.textSizeToWidthRatio
            paint.textSize = textSize
            paint.color = options.textColor
            if (options.typeface != null) {
                paint.typeface = options.typeface
            }
            val padding = result.width * options.paddingToWidthRatio
            val coordinates =
                    calculateCoordinates(canvas.width, canvas.height, padding)
            canvas.drawText(line1!!, coordinates.x/3, coordinates.y - 120, paint)
            canvas.drawText(line2!!, coordinates.x/3, coordinates.y - 60, paint)
            canvas.drawText(line3!!, coordinates.x/3, coordinates.y, paint)
            return result
        }

        private fun calculateCoordinates(
                width: Int,
                height: Int,
                padding: Float
        ): PointF {
            val x = width - padding
            val y = height - padding
            return PointF(x, y)
        }
    }
}

data class WatermarkOptions(
        val textSizeToWidthRatio: Float = 0.02f,
        val paddingToWidthRatio: Float = 0.03f,
        @ColorInt val textColor: Int = Color.BLACK,
        val typeface: Typeface? = null
)