package com.example.flutter_platform_channels

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.flatform_view_flutter.DisplayViewFactory
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : FlutterActivity() {

    companion object {
        private const val CAMERA_PERMISSION_CODE = 101;
        private const val CAMERA_IMG_CODE = 100;
        private const val showToasts = "showToast"
        private const val openCameras = "openCamera"
        private const val openEmails = "openEmail"
        private const val checkPermissions = "checkPermission"
        private const val METHOD_CHANGE_INTERNET_CONNECTIVITY = "CHANGE_INTERNET"
        private const val methodName = "demo"
        private const val eventChannel = "timeHandlerEvent"
        private const val eventChannel2 = "timeHandlerEvent2"
    }

    private lateinit var resultss: MethodChannel.Result


    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        setUpEventChannel(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger, methodName
        ).setMethodCallHandler { call, result ->
            resultss = result
            when (call.method) {
                showToasts -> {
                    val arguments = call.argument<String>("content")
                    Toast.makeText(context, arguments.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                openCameras -> {
                    openCamera()
                }

                openEmails -> {
                    openEmail()
                }

                checkPermissions -> {
                    checkPermission(
                        Manifest.permission.CAMERA,
                        CAMERA_PERMISSION_CODE
                    )
                }
                METHOD_CHANGE_INTERNET_CONNECTIVITY -> {
                    changeInternetConnectivity(call, result)
                }

                else -> {
                    result.notImplemented()
                }
            }
        }


        flutterEngine.platformViewsController.registry.registerViewFactory(
            "plugins/native_view",
            DisplayViewFactory(flutterEngine.dartExecutor.binaryMessenger)
        )

        EventChannel(flutterEngine.dartExecutor.binaryMessenger, eventChannel).setStreamHandler(
            TimeHandler
        )

        ///

    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CAMERA_PERMISSION_CODE)
    }

    private fun openEmail() {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("pirago.test@gmail.com"))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "TITle")
        mIntent.putExtra(Intent.EXTRA_TEXT, "BODY")
        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this@MainActivity, "Da cap quyen", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Camera Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this@MainActivity, "Camera Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    object TimeHandler : EventChannel.StreamHandler {
        private var handler = Handler(Looper.getMainLooper())

        private var eventSink: EventChannel.EventSink? = null

        override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
            eventSink = events
            // every second send the time
            val r: Runnable = object : Runnable {
                override fun run() {
                    handler.post {
                        val dateFormat = SimpleDateFormat("HH:mm:ss")
                        val time = dateFormat.format(Date())
                        eventSink?.success(time)
                    }
                    handler.postDelayed(this, 1000)
                }
            }
            handler.postDelayed(r, 1000)
        }

        override fun onCancel(arguments: Any?) {
            eventSink = null
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedFiles = mutableListOf<String>()

        val uriPathHelper = URIPathHelper()
        if (data?.data != null) {
            val filePath = uriPathHelper.getPath(this, data.data!!)
            selectedFiles.add(filePath!!)
            val channel =
                flutterEngine?.dartExecutor?.binaryMessenger?.let { MethodChannel(it, methodName) }
            channel?.invokeMethod("onFilesPicked", selectedFiles)
        }

    }

    private fun setUpEventChannel(flutterEngine: FlutterEngine) {
        val eventChannel = EventChannel(flutterEngine.dartExecutor.binaryMessenger, eventChannel2)
        this.streamHandler = this.streamHandler
            ?: FlutterStreamHandler()
        eventChannel.setStreamHandler(this.streamHandler)
    }
    /*
    *
    * */
    private var streamHandler: FlutterStreamHandler? = null

    private fun changeInternetConnectivity(methodCall: MethodCall,
                                           result: MethodChannel.Result) {
        val connectivity = methodCall.argument<Boolean>("connectivity")

        if (this.streamHandler == null || connectivity == null) return

        val intent = Intent()
        intent.putExtra("connectivity", connectivity)
        this.streamHandler?.handleIntent(this, intent)
    }

}
