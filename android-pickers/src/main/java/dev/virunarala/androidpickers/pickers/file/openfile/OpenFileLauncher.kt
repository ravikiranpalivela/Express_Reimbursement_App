package dev.virunarala.androidpickers.pickers.file.openfile

import android.content.ActivityNotFoundException
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.file.openfile.model.OpenFileInput
import dev.virunarala.androidpickers.pickers.file.openfile.model.OpenFileOutput

class OpenFileLauncher private constructor(activity: ComponentActivity? = null,
                                           fragment: Fragment? = null
) {

    constructor(activity: ComponentActivity): this(activity,null)
    constructor(fragment: Fragment): this(null,fragment)

    private val activity = activity ?: fragment!!.requireActivity()

    private val openFileLauncher = this.activity.registerForActivityResult(
        OpenFile()
    ) { uri ->

    }

    fun launch(mimeTypes: String,
               onSuccess: (OpenFileOutput) -> Unit,
               onFailure: (message: String) -> Unit) {

        val openFileInput = OpenFileInput(
            mimeTypes,
            onSuccess = onSuccess,
            onFailure = onFailure
        )

        try {
            openFileLauncher.launch(openFileInput)
        } catch (e: ActivityNotFoundException) {
            onFailure("No apps found in your device to perform this action.")
        }
    }
}