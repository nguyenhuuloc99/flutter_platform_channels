import 'dart:io';

import 'package:flutter/material.dart';

import 'native_view_controller.dart';

class NativeView extends StatefulWidget {
  final DisplayViewCallBack? displayViewCallBack;

  const NativeView({super.key, this.displayViewCallBack});

  @override
  State<NativeView> createState() => _NativeViewState();
}

class _NativeViewState extends State<NativeView> {
  @override
  Widget build(BuildContext context) {
    if (Platform.isAndroid) {
      return AndroidView(
        viewType: 'plugins/native_view',
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    }
    return const Center(
      child: Text('PlatFormView'),
    );
  }

  void _onPlatformViewCreated(int id) {
    if (widget.displayViewCallBack == null) {
      return;
    }
    NativeViewController controller = NativeViewController.call(id);

    widget.displayViewCallBack!(controller);
  }
}
