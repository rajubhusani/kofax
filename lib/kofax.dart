import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:kofax/kfx_images.dart';

class Kofax {
  static const MethodChannel _channel = const MethodChannel('kofax');

//BASE64 string will return from the cheque scan
  static Future<InMemoryKfxImages> chequeScan({String scanType}) async {
    final String scanResponseData =
        await _channel.invokeMethod('chequeScan', {"scanType": scanType});
    InMemoryKfxImages images = InMemoryKfxImages.fromJson(
        JsonDecoder().convert(scanResponseData) as Map<String, dynamic>);
    return images;
  }

  //BASE64 string will return from the mock scan
  static Future<InMemoryKfxImages> mockScan() async {
    try {
      final String scanResponseData =
          await _channel.invokeMethod('mockScan', {});
      InMemoryKfxImages images = InMemoryKfxImages.fromJson(
          JsonDecoder().convert(scanResponseData) as Map<String, dynamic>);

      return images;
    } on PlatformException catch (e) {
      throw e;
    }
  }
}
