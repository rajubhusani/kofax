//
//  CheckViewController.swift
//  SwiftCheckCaptureSample
//
//  Copyright © 2017 Kofax. All rights reserved.
//

import UIKit

class CheckViewController: UIViewController {
    
    var checkData: kfxCheckData!
    
    @IBOutlet weak var textView: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if checkData == nil {
            return
        }
        
        var text = "[Check Results]\n"
        text += String.init(format: "micr: %@ (%1.1f)\n", checkData.micr.value, checkData.micr.confidence)
        text += String.init(format: "amount: %@ (%1.1f)\n", checkData.amount.value, checkData.amount.confidence)
        text += String.init(format: "checkNumber: %@ (%1.1f)\n", checkData.checkNumber.value, checkData.checkNumber.confidence)
        text += String.init(format: "date: %@ (%1.1f)\n", checkData.date.value, checkData.date.confidence)
        text += String.init(format: "payeeName: %@ (%1.1f)\n", checkData.payeeName.value, checkData.payeeName.confidence)
        text += String.init(format: "lar: %@ (%1.1f)\n", checkData.lar.value, checkData.lar.confidence)
        text += String.init(format: "car: %@ (%1.1f)\n", checkData.car.value, checkData.car.confidence)
        text += String.init(format: "usable: %@ (%1.1f)\n", (checkData.usable.value as NSString).boolValue == true ? "yes" : "no", checkData.usable.confidence)
        text += String.init(format: "transit: %@ (%1.1f)\n", checkData.transit.value, checkData.transit.confidence)
        text += String.init(format: "onUs1: %@ (%1.1f)\n", checkData.onUs1.value, checkData.onUs1.confidence)
        text += String.init(format: "onUs2: %@ (%1.1f)\n", checkData.onUs2.value, checkData.onUs2.confidence)
        text += String.init(format: "auxiliary on-us: %@ (%1.1f)\n", checkData.auxiliaryOnUs.value, checkData.auxiliaryOnUs.confidence)
        text += String.init(format: "epc: %@ (%1.1f)\n", checkData.epc.value, checkData.epc.confidence)
        text += String.init(format: "micrAmount: %@ (%1.1f)\n", checkData.micrAmount.value, checkData.micrAmount.confidence)
        
        text += "\n[Check IQA]\n"
        if let iqa = checkData.iqa {
            text += String.init(format: "undersizeImage: %@\n", iqa.undersizeImage.value)
            text += String.init(format: "foldedOrTornDocumentCorners: %@\n", iqa.foldedOrTornDocumentCorners.value)
            text += String.init(format: "foldedOrTornDocumentEdges: %@\n", iqa.foldedOrTornDocumentEdges.value)
            text += String.init(format: "documentFramingError: %@\n", iqa.documentFramingError.value)
            text += String.init(format: "documentSkew: %@\n", iqa.documentSkew.value)
            text += String.init(format: "oversizeImage: %@\n", iqa.oversizeImage.value)
            text += String.init(format: "piggybackDocument: %@\n", iqa.piggybackDocument.value)
            text += String.init(format: "imageTooLight: %@\n", iqa.imageTooLight.value)
            text += String.init(format: "imageTooDark: %@\n", iqa.imageTooDark.value)
            text += String.init(format: "horizontalStreaks: %@\n", iqa.horizontalStreaks.value)
            text += String.init(format: "belowMinFrontImageSize: %@\n", iqa.belowMinFrontImageSize.value)
            text += String.init(format: "aboveMaxFrontImageSize: %@\n", iqa.aboveMaxFrontImageSize.value)
            text += String.init(format: "belowMinRearImageSize: %@\n", iqa.belowMinRearImageSize.value)
            text += String.init(format: "aboveMaxRearImageSize: %@\n", iqa.aboveMaxRearImageSize.value)
            text += String.init(format: "spotNoise: %@\n", iqa.spotNoise.value)
            text += String.init(format: "imageDimensionMismatch: %@\n", iqa.imageDimensionMismatch.value)
            text += String.init(format: "carbonStrip: %@\n", iqa.carbonStrip.value)
            text += String.init(format: "outOfFocus: %@\n", iqa.outOfFocus.value)
        } else {
            text += "nil\n"
        }
        
        text += "\n[Check Usability]\n"
        if let usability = checkData.usability {
            text += String.init(format: "CAR: %@\n", usability.car.value)
            text += String.init(format: "LAR: %@\n", usability.lar.value)
            text += String.init(format: "signature: %@\n", usability.signature.value)
            text += String.init(format: "payeeName: %@\n", usability.payeeName.value)
            text += String.init(format: "date: %@\n", usability.date.value)
            text += String.init(format: "codeline: %@\n", usability.codeline.value)
            text += String.init(format: "payeeEndorsement: %@\n", usability.payeeEndorsement.value)
        } else {
            text += "nil\n"
        }
        textView.textColor = UIColor.black
        textView.text = text
    }
}
