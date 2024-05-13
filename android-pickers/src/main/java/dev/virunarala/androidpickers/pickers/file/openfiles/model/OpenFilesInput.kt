package dev.virunarala.androidpickers.pickers.file.openfiles.model

import android.net.Uri

data class OpenFilesInput(
    val mimeTypes: Array<String> = arrayOf("*/*"),
    val initialUri: Uri? = null,
    val onSuccess: (OpenFilesOutput) -> Unit,
    val onFailure: (message: String) -> Unit
)
