import 'package:flutter/services.dart';

import 'utils.dart';

class NativeBridge {
  static const messageChannel = MethodChannel(methodName);
  static const methodHello = "HELLO";
  static const methodChangeInternetConnectivity = "CHANGE_INTERNET";

  static const eventChannel = EventChannel('timeHandlerEvent2');

  static bool currentValue = false;
  static Stream<bool>? eventStream;

  static Stream<bool> listenToNativeEventChannel() {
    if (eventStream == null) {
      return eventStream = eventChannel.receiveBroadcastStream().cast<bool>();
    } else {
      return eventStream!;
    }
  }

  static void changeInternetConnectivity() {
    Map<String, dynamic> params = {};

    currentValue = !currentValue;
    params["connectivity"] = currentValue;

    messageChannel.invokeMethod(methodChangeInternetConnectivity, params);
  }
}
