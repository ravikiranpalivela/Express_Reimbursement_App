package dev.virunarala.androidpickers.pickers.photo.capture

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.photo.capture.model.PhotoCaptureInput
import dev.virunarala.androidpickers.pickers.photo.capture.model.PhotoCaptureOutput

class PhotoCaptureLauncher private constructor(activity: ComponentActivity? = null,
                                               fragment: Fragment? = null
) {
    constructor(activity: ComponentActivity): this(activity,null)
    constructor(fragment: Fragment): this(null,fragment)

    private val activity = activity ?: fragment!!.requireActivity()

    val photoCaptureLauncher = this.activity.registerForActivityResult(
        PhotoCapture()
    ) { uri ->

    }

    fun launch(uri: Uri,
               onSuccess: (PhotoCaptureOutput) -> Unit,
               onFailure: (message: String) -> Unit) {
        photoCaptureLauncher.launch(
            PhotoCaptureInput(
                uri,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        )
    }
}