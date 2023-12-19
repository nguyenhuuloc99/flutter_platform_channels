package com.example.flatform_view_flutter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.flutter_platform_channels.R
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.platform.PlatformView

class DisplayViewWidget internal constructor(
    context: Context, id: Int, message: BinaryMessenger
) : PlatformView, MethodCallHandler {

    private val view: View = LayoutInflater.from(context).inflate(R.layout.custom_view, null)
    private val methodChannel: MethodChannel = MethodChannel(message, "plugins/native_view_$id")


    init {
        methodChannel.setMethodCallHandler(this)
    }

    override fun getView(): View {
        return view
    }

    override fun dispose() {

    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "draggable" -> draggable(call, result)
            else -> result.notImplemented()
        }
    }

    private fun draggable(call: MethodCall, result: MethodChannel.Result) {
        val isDraggable: Boolean = call.arguments as Boolean
        if (isDraggable) result.success(null)
    }
}