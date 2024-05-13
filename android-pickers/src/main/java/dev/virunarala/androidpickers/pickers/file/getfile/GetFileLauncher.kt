package dev.virunarala.androidpickers.pickers.file.getfile

import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.file.getfile.model.GetFileInput
import dev.virunarala.androidpickers.pickers.file.getfile.model.GetFileOutput

class GetFileLauncher private constructor(activity: ComponentActivity? = null,
                                         fragment: Fragment? = null
) {

    constructor(activity: ComponentActivity): this(activity,null)
    constructor(fragment: Fragment): this(null,fragment)

    private val activity = activity ?: fragment!!.requireActivity()

    private val getFileLauncher = this.activity.registerForActivityResult(
        GetFile()
    ) { uri ->

    }

    fun launch(mimeType: String,
               initialUri: Uri?,
               onSuccess: (GetFileOutput) -> Unit,
               onFailure: (message: String) -> Unit) {

        val getFileInput = GetFileInput(
            mimeType,
            initialUri,
            onSuccess = onSuccess,
            onFailure = onFailure
        )

        try {
            getFileLauncher.launch(getFileInput)
        } catch (e: ActivityNotFoundException) {
            onFailure("No apps found in your device to perform this action.")
        }
    }
}