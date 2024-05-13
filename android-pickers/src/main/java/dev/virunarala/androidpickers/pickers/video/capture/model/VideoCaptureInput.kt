package dev.virunarala.androidpickers.pickers.video.capture.model

import android.net.Uri

data class VideoCaptureInput(

    val uri: Uri,

    val onSuccess: (VideoCaptureOutput) -> Unit,

    val onFailure: (message: String) -> Unit
)