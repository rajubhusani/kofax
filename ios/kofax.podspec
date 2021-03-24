#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint kofax.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'kofax'
  s.version          = '0.0.1'
  s.summary          = 'A new flutter plugin project.'
  s.description      = <<-DESC
A new flutter plugin project.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => 'Frameworks/MobileSDK.framework' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.vendored_frameworks = 'MobileSDK.framework'
  s.ios.deployment_target = '10.0'
  s.public_header_files = 'Classes/**/*.h'

end


