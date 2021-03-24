package com.fab.kofax

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.NonNull
import com.fab.kofax.utils.Enums
import com.fab.kofax.utils.InMemoryKfxImages
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.InputStream


/** KofaxPlugin */
class KofaxPlugin : FlutterPlugin,
        MethodCallHandler,
        PluginRegistry.ActivityResultListener,
        ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var applicationContext: Context? = null
    private var activity: Activity? = null
    private var result: Result? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.applicationContext = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "kofax")
        channel.setMethodCallHandler(this)
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "kofax")
            channel.setMethodCallHandler(KofaxPlugin())
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        this.result = result
        when (call.method) {
            CapturedImages.CHEQUE_SCAN -> {
                InMemoryKfxImages.scanType = if(call.hasArgument(CapturedImages.SCAN_TYPE))
                        call.argument<String>(CapturedImages.SCAN_TYPE) else Enums.ScanType.BOTH.name
                loadScanView()
            }
            CapturedImages.MOCK_SCAN -> mockScan()
            else -> result.notImplemented()
        }
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
        channel.setMethodCallHandler(null)
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        Log.d("====", "onReattachedToActivityForConfigChanges") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDetachedFromActivityForConfigChanges() {
        Log.d("====", "onDetachedFromActivityForConfigChanges") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?): Boolean {
        if (resultCode == Activity.RESULT_OK) {
            val front: String? = getFrontBase64Image()
            val back: String? = getBackBase64Image()

            result?.success(CapturedImages(front, back,
                    InMemoryKfxImages.mMicrData).toString())
            InMemoryKfxImages.scanType = Enums.ScanType.NONE.name
        } else if(resultCode == Activity.RESULT_CANCELED) {
            result?.error("CANCELLED",
                    activity?.resources?.getString(R.string.cancelled_error),
                    "CANCELLED"
            )
        }
        return true
    }

    private fun loadScanView() {
        if(InMemoryKfxImages.scanType == Enums.ScanType.BOTH.name){
            InMemoryKfxImages.clearImages()
        }
        KofaxManager().setContextAndLicense(applicationContext)
        activity?.startActivityForResult(Intent(activity,
                CheckCaptureActivity::class.java), 1111)
    }

    private fun mockScan() {
        val inputStream: InputStream? =
                applicationContext?.assets?.open(CapturedImages.MOCK_CHEQUE_FRONT)
        val frontBitmap = BitmapFactory.decodeStream(inputStream)
        val inputStreamBack: InputStream? =
                applicationContext?.assets?.open(CapturedImages.MOCK_CHEQUE_BACK)
        val backBitmap = BitmapFactory.decodeStream(inputStreamBack)
        result?.success(CapturedImages(CapturedImages.convertImageToBase64(applicationContext,
                frontBitmap, false),
                CapturedImages.convertImageToBase64(applicationContext, backBitmap, false),
                CapturedImages.MOCK_MICR).toString())
        if (!frontBitmap.isRecycled) frontBitmap.recycle()
        if (!backBitmap.isRecycled) backBitmap.recycle()
        inputStream?.close()
        inputStreamBack?.close()
    }

    private fun getFrontBase64Image(): String?{
        if (InMemoryKfxImages.mFrontProcessedImage != null &&
                InMemoryKfxImages.mFrontProcessedImage!!.imageBitmap != null &&
                !InMemoryKfxImages.mFrontProcessedImage!!.imageBitmap.isRecycled) {
            return CapturedImages.convertImageToBase64(applicationContext,
                    InMemoryKfxImages.mFrontProcessedImage!!.imageBitmap, false)
        }
        return ""
    }

    private fun getBackBase64Image(): String?{
        if (InMemoryKfxImages.mBackProcessedImage != null &&
                InMemoryKfxImages.mBackProcessedImage!!.imageBitmap != null &&
                !InMemoryKfxImages.mBackProcessedImage!!.imageBitmap.isRecycled) {
            return CapturedImages.convertImageToBase64(applicationContext,
                    InMemoryKfxImages.mBackProcessedImage!!.imageBitmap, false)
        }
        return ""
    }

}
