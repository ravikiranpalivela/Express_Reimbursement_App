package dev.virunarala.androidpickers.pickers.file.getfile.model

import android.net.Uri

data class GetFileInput(
    val fileType: String = "*/*",
    val initialUri: Uri? = null,
    val onSuccess: (GetFileOutput) -> Unit,
    val onFailure: (message: String) -> Unit
)
