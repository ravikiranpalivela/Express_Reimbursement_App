package dev.virunarala.androidpickers.pickers.file.openfile.model

import android.net.Uri

data class OpenFileInput(
    private val mimeTypes: String,
    val onSuccess: (OpenFileOutput) -> Unit,
    val onFailure: (message: String) -> Unit
) {

    fun getMimeTypes(): Array<String> {
        return mimeTypes.split(";").toTypedArray()
    }
}
