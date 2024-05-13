package dev.virunarala.androidpickers.pickers.file.openfiles

import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.file.openfiles.model.OpenFilesInput
import dev.virunarala.androidpickers.pickers.file.openfiles.model.OpenFilesOutput

class OpenFilesLauncher private constructor(activity: ComponentActivity? = null,
                                            fragment: Fragment? = null
) {

    constructor(activity: ComponentActivity): this(activity,null)
    constructor(fragment: Fragment): this(null,fragment)

    private val activity = activity ?: fragment!!.requireActivity()

    val openFilesLauncher = this.activity.registerForActivityResult(
        OpenFiles()
    ) { uri ->

    }

    fun launch(mimeTypes: Array<String>,
               initialUri: Uri?,
               onSuccess: (OpenFilesOutput) -> Unit,
               onFailure: (message: String) -> Unit) {

        val openFilesInput = OpenFilesInput(
            mimeTypes,
            initialUri,
            onSuccess = onSuccess,
            onFailure = onFailure
        )

        try {
            openFilesLauncher.launch(openFilesInput)
        } catch (e: ActivityNotFoundException) {
            onFailure("No apps found in your device to perform this action.")
        }
    }
}