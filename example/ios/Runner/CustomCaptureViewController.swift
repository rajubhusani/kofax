//
//  CustomCaptureViewController.swift
//  SwiftCheckCaptureSample
//
//  Created by Vimlesh Bhatt on 25/11/2020.
//  Copyright © 2020 kofax. All rights reserved.
//

import Foundation
import UIKit
import kofax

class CustomCaptureViewController: kfxCheckCaptureViewController, kfxCheckCaptureViewControllerDelegate {
    
    var checkData: kfxCheckData!
    // The Image Capture Control
//    var imageCaptureControl: kfxKUIImageCaptureControl;
    
    
    @IBOutlet weak var textView: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Set Kofax SDK license. Replace the MyLicenseString below with your license string.
        if !kfxLicense.setMobileSDK("jV[0tUzfdlzjcN,#^9(G4VNm^(U%80[KTA`j^vrPfl4j6cIkDfd5FqB,n!Z8lF0gd!k5,&N!!tL@Iv=!BDUEOb!hhWjZ;,5h[7?#") {
            print("Error: Kofax license is not valid or expired!")
        }
        
        // Configure Capture Conroller
        configureCaptureSettings()
        
    }
    
    private func configureCaptureSettings() {
        
        
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
    
//    public func navigationController(_ navigationController: UINavigationController, didShow viewController: UIViewController, animated: Bool) {
//        // Remove capture VC from the navigation stack
//        if checkSide == "Front" && type(of: viewController) == CheckViewController.self {
//            navigationController.viewControllers.remove(at: navigationController.viewControllers.count - 2)
//            captureViewController = nil
//        }
//    }
    
//    public func alertView(_ alertView: UIAlertView, clickedButtonAt: Int) {
//        dismissCaptureViewController(animated: true)
//    }
    
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
