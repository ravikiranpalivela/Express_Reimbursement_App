package dev.virunarala.androidpickers.pickers.file.getfiles

import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.file.getfiles.model.GetFilesInput
import dev.virunarala.androidpickers.pickers.file.getfiles.model.GetFilesOutput

class GetFilesLauncher private constructor(activity: ComponentActivity? = null,
                                           fragment: Fragment? = null
) {

    constructor(activity: ComponentActivity): this(activity,null)
    constructor(fragment: Fragment): this(null,fragment)

    private val activity = activity ?: fragment!!.requireActivity()

    private val getFilesLauncher = this.activity.registerForActivityResult(
        GetFiles()
    ) { uri ->

    }

    fun launch(mimeType: String,
               initialUri: Uri?,
               onSuccess: (GetFilesOutput) -> Unit,
               onFailure: (message: String) -> Unit) {

        val getFilesInput = GetFilesInput(
            mimeType,
            initialUri,
            onSuccess = onSuccess,
            onFailure = onFailure
        )

        try {
            getFilesLauncher.launch(getFilesInput)
        } catch (e: ActivityNotFoundException) {
            onFailure("No apps found in your device to perform this action.")
        }
    }
}