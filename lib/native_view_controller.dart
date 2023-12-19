import 'package:flutter/services.dart';

typedef DisplayViewCallBack = void Function(NativeViewController controller);

class NativeViewController {
  NativeViewController.call(int id)
      : _channel = MethodChannel('plugins/native_view_$id');

  final MethodChannel _channel;

  Future<Future<List?>> draggable(bool value) async {
    return _channel.invokeListMethod('draggable', value);
  }
}
