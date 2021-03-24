import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:kofax/kofax.dart';

void main() {
  const MethodChannel channel = MethodChannel('kofax');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('chequeScan', () async {
    expect(await Kofax.chequeScan(scanType: "BOTH"), '');
  });
}
