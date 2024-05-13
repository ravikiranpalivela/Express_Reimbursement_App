package dev.virunarala.androidpickers.pickers.file.getdirectory.model

import android.net.Uri
import dev.virunarala.androidpickers.pickers.file.openfiles.model.OpenFilesOutput

data class GetDirectoryInput(
    val initialUri: Uri? = null,
    val onSuccess: (GetDirectoryOutput) -> Unit,
    val onFailure: (message: String) -> Unit
)
