import Flutter
import UIKit

public class SwiftKofaxPlugin: NSObject, FlutterPlugin {
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "kofax", binaryMessenger: registrar.messenger())
        let instance = SwiftKofaxPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "chequeScan":
            break;
        case "mockScan":
            break;
        default:
            break;
        }
    }
}
