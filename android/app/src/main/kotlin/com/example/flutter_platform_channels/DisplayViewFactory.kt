package com.example.flatform_view_flutter

import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class DisplayViewFactory(private val message: BinaryMessenger) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {


    override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
        return DisplayViewWidget(context!!, viewId, message);
    }

}