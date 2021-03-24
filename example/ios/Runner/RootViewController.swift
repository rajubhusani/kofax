//
//  RootViewController.swift
//  SwiftCheckCaptureSample
//
//  Copyright © 2017 Kofax. All rights reserved.
//

import UIKit

public class RootViewController:kfxCheckCaptureViewController, kfxCheckCaptureViewControllerDelegate, UINavigationControllerDelegate, UIAlertViewDelegate {

    let kTitle = "kTitle"
    let kCheckSide = "kCheckSide"
    let kCellID = "kCellID"
    
    let rttiUrl = "https://mobiledemo.kofax.com:443/mobilesdk/api/checkdeposit1_2?customer=kofax"
    let ktaUrl = ""   //Provide KTA URL.
    
    let ktaUsername = "KMDUser"
    let ktaPassword = "DemoPassword"
    
    var data: Array<Dictionary<String, String>>!
    var checkSide = "Front"
    var frontSideData: kfxCheckData!
    
    // Check capture VC
    var captureViewController: CustomCaptureViewController!
    
    public override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationController?.delegate = self
        setLicence();
        startScan();
    }
    
    func setLicence() {
    
        // Set Kofax SDK license. Replace the MyLicenseString below with your license string.
        if !kfxLicense.setMobileSDK("jV[0tUzfdlzjcN,#^9(G4VNm^(U%80[KTA`j^vrPfl4j6cIkDfd5FqB,n!Z8lF0gd!k5,&N!!tL@Iv=!BDUEOb!hhWjZ;,5h[7?#") {
            print("Error: Kofax license is not valid or expired!")
        }
    }
    
    func startScan() {
        
        data = [[kTitle : "Front Side", kCheckSide : "Front"],
                [kTitle : "Back Side", kCheckSide : "Back"]]
        
        let item = data[0]
        checkSide = item[kCheckSide]!
        
        switch checkSide {
            case "Front":
                // Load check capture VC from storyboard
                let storyboard = UIStoryboard.init(name: "Main", bundle: nil)
                captureViewController = storyboard.instantiateViewController(withIdentifier: "CheckCaptureViewControllerID") as? CustomCaptureViewController
                
                // Setup front check capture
                captureViewController.parameters.checkSide = .front

                // Turn torch off
                captureViewController.parameters.lookAndFeelParameters.torch = .off

                // Show gallery button to pickup photos from device gallery
                captureViewController.parameters.lookAndFeelParameters.galleryEnabled = true

                // Show review captured image screen
                captureViewController.parameters.lookAndFeelParameters.reviewCapturedImageEnabled = true

                // Turn extraction off. Run extraction only when both sides are captured.
                captureViewController.parameters.extractionParameters.extractionType = .off
                
               // Setup delegate to get callbacks from capture VC
                captureViewController.delegate = self

                self.presentingViewController?.view.addSubview(captureViewController.view);
                
            case "Back":
                if frontSideData == nil {
                    // Don't run back capture until we don't have front side captured
            
                    break
                }
                
                // Create capture parameters object
                let param = kfxCheckCaptureParameters()
                
                // Setup back check capture
                param.checkSide = .back
                
                // Turn torch off
                param.lookAndFeelParameters.torch = .off
                
                // Show gallery button to pickup photos from device gallery
                param.lookAndFeelParameters.galleryEnabled = true
                
                // Show review captured image screen
                param.lookAndFeelParameters.reviewCapturedImageEnabled = true
                
                // Show force capture button after 5 seconds
                param.lookAndFeelParameters.manualCaptureTimer = 5
                
                // Feed front data to use both sides for extraction
                param.reverseSideCheck = frontSideData
                
                // Turn RTTI extraction on
                param.extractionParameters.extractionType = .RTTI
                
                // Setup some custom extraction parameters
                param.extractionParameters.serverURL = rttiUrl
                
                // Setup some custom extraction parameters
//                param.country = .unitesStates
//                param.extractionParameters.serverURL = ktaUrl
//                param.extractionParameters.extractionType = .KTA
//                let credentials = kfxExtractionCredentials()
//                credentials.username = ktaUsername
//                credentials.password = ktaPassword
//                param.extractionParameters.credentials = credentials
                
                // Create check capture VC programmatically
                captureViewController = kfxCheckCaptureViewController() as? CustomCaptureViewController
                
                // Setup capture parameters
                captureViewController.parameters = param
                
                // Setup delegate to get callbacks from capture VC
                captureViewController.delegate = self
                
                // Present capture VC modally
                captureViewController.modalPresentationStyle = UIModalPresentationStyle.fullScreen
                present(captureViewController, animated: true, completion: nil)
            
            default: break
        }
    }
    
    // This callback gets called on finishing check capture
    public func checkCaptureViewController(_ checkCaptureViewController: kfxCheckCaptureViewController!, didExtractCheck check: kfxCheckData!, error: Error!) {
        // Add code to handle check data and error if such occured...
        if captureViewController != checkCaptureViewController {
            return
        }
        
        if error != nil {
            let alert = UIAlertView()
            alert.title = "Error"
            alert.message = error.localizedDescription
            alert.delegate = self
            alert.addButton(withTitle: "Cancel")
            alert.show()
        } else {
            if checkSide == "Front" {
                // Store front side data
                frontSideData = check
            } else {
                // Present check VC with result data
                let storyboard = UIStoryboard.init(name: "Main", bundle: nil)
                let checkViewController = storyboard.instantiateViewController(withIdentifier: "CheckViewControllerID") as! CheckViewController
                checkViewController.checkData = check
                
                let backButton = UIBarButtonItem.init(title: "Back", style: .plain, target: nil, action: nil)
                navigationItem.backBarButtonItem = backButton
                navigationController?.pushViewController(checkViewController, animated: true)
                
                frontSideData = nil
            }
            
            dismissCaptureViewController(animated: true)
        }
    }
    
    // This callback gets called on pressing close button in capture VC
    public func captureViewControllerDidClose(_ captureViewController: kfxCaptureViewController!) {
        // Add code to dismiss capture VC...
        dismissCaptureViewController(animated: true)
    }
    
    public func navigationController(_ navigationController: UINavigationController, didShow viewController: UIViewController, animated: Bool) {
        // Remove capture VC from the navigation stack
        if checkSide == "Front" && type(of: viewController) == CheckViewController.self {
            navigationController.viewControllers.remove(at: navigationController.viewControllers.count - 2)
            captureViewController = nil
        }
    }
    
    public func alertView(_ alertView: UIAlertView, clickedButtonAt: Int) {
        dismissCaptureViewController(animated: true)
    }
    
    func dismissCaptureViewController(animated: Bool) {
        if captureViewController == nil {
            return
        }
        
        if checkSide == "Front" {
            _ = navigationController?.popViewController(animated: animated)
        } else if checkSide == "Back" {
            captureViewController.dismiss(animated: animated, completion: nil)
        }
        
        captureViewController = nil
    }
}
