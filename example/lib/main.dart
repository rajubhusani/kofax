import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:kofax/kfx_images.dart';
import 'package:kofax/kofax.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _frontImage = '';

  @override
  void initState() {
    super.initState();
  }

  Image imageFromBase64String(String base64String) {
    return Image.memory(base64Decode(base64String));
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Kofax Plugin Example'),
        ),
        body: Center(
          child: Column(
            children: [
              RaisedButton(
                  child: Text("Scan Front"), onPressed: () => _retakeFront()),
              RaisedButton(
                  child: Text("Scan Back"), onPressed: () => _retakeBack()),
              RaisedButton(
                  child: Text("Scan Both"), onPressed: () => _scanBoth()),
              _frontImage.isNotEmpty
                  ? imageFromBase64String(_frontImage)
                  : Text("In Progress...")
            ],
          ),
        ),
      ),
    );
  }

  void _retakeFront() async {
    try {
      InMemoryKfxImages kfxImages =
          await Kofax.chequeScan(scanType: describeEnum(ScanType.FRONT));
      setState(() {
        _frontImage = kfxImages?.front;
      });
    } on PlatformException catch (e) {
      debugPrint("=====" + e.message);
    }
  }

  void _retakeBack() async {
    try {
      InMemoryKfxImages kfxImages =
          await Kofax.chequeScan(scanType: describeEnum(ScanType.BACK));
      setState(() {
        _frontImage = kfxImages?.back;
      });
    } on PlatformException catch (e) {
      debugPrint("=====" + e.message);
    }
  }

  void _scanBoth() async {
    try {
      InMemoryKfxImages kfxImages =
          await Kofax.chequeScan(scanType: describeEnum(ScanType.BOTH));
      setState(() {
        _frontImage = kfxImages?.front;
      });
    } on PlatformException catch (e) {
      debugPrint("=====" + e.message);
    }
  }
}

enum ScanType { BOTH, FRONT, BACK }
