package dev.virunarala.androidpickers.pickers.photo.capture.model

import android.net.Uri

data class PhotoCaptureInput(

    val uri: Uri,

    val onSuccess: (PhotoCaptureOutput) -> Unit,

    val onFailure: (message: String) -> Unit
)