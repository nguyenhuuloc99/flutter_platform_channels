import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class EventChannelExample extends StatelessWidget {
  EventChannel eventChannel = const EventChannel('timeHandlerEvent');

  EventChannelExample({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Event Channel example"),
      ),
      body: Center(
        child: StreamBuilder(
          stream: streamTimeFromNative(),
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              return Text(
                  '${snapshot.data}',
                  style: const TextStyle(fontSize: 20)
              );
            } else {
              return const CircularProgressIndicator();
            }
          },
        ),
      ),
    );
  }

  Stream<String> streamTimeFromNative() {
    return eventChannel
        .receiveBroadcastStream()
        .map((event) => event.toString());
  }
}
