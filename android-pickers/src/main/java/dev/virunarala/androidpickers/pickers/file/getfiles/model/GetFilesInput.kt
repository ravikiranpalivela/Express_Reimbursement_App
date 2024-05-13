package dev.virunarala.androidpickers.pickers.file.getfiles.model

import android.net.Uri

data class GetFilesInput(
    val fileType: String = "*/*",
    val initialUri: Uri? = null,
    val onSuccess: (GetFilesOutput) -> Unit,
    val onFailure: (message: String) -> Unit
)
