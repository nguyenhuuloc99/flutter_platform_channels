import 'package:flutter/material.dart';

import 'native_view.dart';

class NativeViewExample extends StatelessWidget {
  const NativeViewExample({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Native View Example"),
      ),
      body: const Center(
        child: SizedBox(
          height: 100,
          child: NativeView(),
        ),
      ),
    );
  }
}
