package dev.virunarala.androidpickers.pickers.file.getdirectory

import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.file.getdirectory.model.GetDirectoryInput
import dev.virunarala.androidpickers.pickers.file.getdirectory.model.GetDirectoryOutput

class GetDirectoryLauncher private constructor(activity: ComponentActivity? = null,
                           fragment: Fragment? = null
) {

    constructor(activity: ComponentActivity): this(activity,null)
    constructor(fragment: Fragment): this(null,fragment)

    private val activity = activity ?: fragment!!.requireActivity()

    private val getDirectoryLauncher = this.activity.registerForActivityResult(
        GetDirectory()
    ) { uri ->

    }

    fun launch(initialUri: Uri? = null,
               onSuccess: (GetDirectoryOutput) -> Unit,
               onFailure: (message: String) -> Unit) {

        val getDirectoryInput = GetDirectoryInput(
            initialUri = initialUri,
            onSuccess = onSuccess,
            onFailure = onFailure
        )

        try {
            getDirectoryLauncher.launch(getDirectoryInput)
        } catch (e: ActivityNotFoundException) {
            onFailure("No apps found in your device to perform this action.")
        }
    }
}