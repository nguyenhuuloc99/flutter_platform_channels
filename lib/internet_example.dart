import 'dart:async';

import 'package:flutter/material.dart';

import 'bridget.dart';

class InternetExample extends StatefulWidget {
  const InternetExample({super.key});

  @override
  State<InternetExample> createState() => _InternetExampleState();
}

class _InternetExampleState extends State<InternetExample> {
  bool responseStream = false;
  late StreamSubscription<bool> internetConnectivityStream;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    listenToNativeStream();
  }

  void listenToNativeStream() {
    internetConnectivityStream =
        NativeBridge.listenToNativeEventChannel().listen((string) {
      setState(() {
        responseStream = string;
      });
    });
  }

  void changeInternetConnectivity() {
    NativeBridge.changeInternetConnectivity();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Check Internet'),
      ),
      body: Column(
        children: [
          const Text(
            'Is the internet connected?',
            textAlign: TextAlign.center,
          ),
          Text(
            responseStream ? "YES" : "NOPE",
            textAlign: TextAlign.center,
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          changeInternetConnectivity();
        },
      ),
    );
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    internetConnectivityStream.cancel();
  }
}
