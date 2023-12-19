import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'event_channel_example.dart';
import 'nv_exmaple.dart';
import 'utils.dart';

class MyHomePage extends StatefulWidget {

   const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  List<String> selectedFiles = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.blue,
        title: const Text("Flutter's platform channels"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: () {
                showToast("Show Toast Method Channel");
              },
              child: const Text('Show Toast'),
            ),
            const SizedBox(
              height: 30,
            ),
            ElevatedButton(
              onPressed: () {
                requestCameraPermission();
              },
              child: const Text('Check Permission'),
            ),
            const SizedBox(
              height: 30,
            ),
            ElevatedButton(
              onPressed: () {
                openCamera();
              },
              child: const Text('OPEN ALBUM'),
            ),
            const SizedBox(
              height: 30,
            ),
            ElevatedButton(
              onPressed: () {
                openEmailApp();
              },
              child: const Text('OPEN EMAIL'),
            ),
            const SizedBox(
              height: 30,
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context)
                    .push(MaterialPageRoute(builder: (context) {
                  return EventChannelExample();
                }));
              },
              child: const Text('Event Channel'),
            ),
            const SizedBox(
              height: 30,
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (context) {
                      return const NativeViewExample();
                    },
                  ),
                );
              },
              child: const Text('Native View Example'),
            ),
            const SizedBox(
              height: 30,
            ),
            if(selectedFiles.isNotEmpty)
            Image.file(File(selectedFiles[0]))
          ],
        ),
      ),
    );
  }

  void showToast(String content) async {
    await const MethodChannel(methodName)
        .invokeMethod(showToasts, {"content": content});
  }

  void openCamera() async {
        await const MethodChannel(methodName).invokeMethod<String>(openCameras);
        await setupMethodCallHandler();

  }

  void openEmailApp() async {
    await const MethodChannel(methodName).invokeMethod<String>(openEmails);
  }

  void requestCameraPermission() async {
    await const MethodChannel(methodName)
        .invokeMethod<String>(checkPermissions);
  }

  Future<void> setupMethodCallHandler() async {
    const MethodChannel(methodName).setMethodCallHandler((call) async {
      if (call.method == "onFilesPicked") {
        List<String> selectedFiles = List<String>.from(call.arguments);
        print("Received selected files: $selectedFiles");
      }
    });
  }
}
