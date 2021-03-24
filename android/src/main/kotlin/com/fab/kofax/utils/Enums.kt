package com.fab.kofax.utils

class Enums {
    enum class CaptureState {
        FRONT, BACK
    }

    /* Image status types. */
    enum class ImageStatus {
        NONE, CAPTURED, PROCESSING, PROCESSED, EXTRACTED, SAVERAWDATA
    }

    enum class CaptureMode {
        IMAGE, VIDEO
    }

    enum class Detector {
        ISG, GPU
    }

    enum class CaptureExperienceMode {
        DOCUMENTCAPTURE, CHECKCAPTURE
    }

    enum class Orientation {
        LANDSCAPE, PORTRAIT
    }

    enum class ProcessType {
        DEVICE, SERVER, NONE, SDK
    }

    enum class PreviewStates {
        SUMMARY, CAPTURE
    }

    enum class ScanType{
        NONE, BOTH, FRONT, BACK
    }
}