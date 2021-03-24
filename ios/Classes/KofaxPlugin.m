#import "KofaxPlugin.h"
#if __has_include(<kofax/kofax-Swift.h>)
#import <kofax/kofax-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "kofax-Swift.h"
#endif

@implementation KofaxPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftKofaxPlugin registerWithRegistrar:registrar];
}
@end
