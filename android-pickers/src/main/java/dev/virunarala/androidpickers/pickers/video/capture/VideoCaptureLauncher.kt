package dev.virunarala.androidpickers.pickers.video.capture

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.video.capture.model.VideoCaptureInput
import dev.virunarala.androidpickers.pickers.video.capture.model.VideoCaptureOutput

class VideoCaptureLauncher private constructor(activity: ComponentActivity? = null,
                                               fragment: Fragment? = null
) {

    constructor(activity: ComponentActivity): this(activity,null)
    constructor(fragment: Fragment): this(null,fragment)

    private val activity = activity ?: fragment!!.requireActivity()

    private val videoCaptureLauncher = this.activity.registerForActivityResult(
        VideoCapture()
    ) { uri ->

    }

    fun launch(uri: Uri,
               onSuccess: (VideoCaptureOutput) -> Unit,
               onFailure: (message: String) -> Unit) {

        videoCaptureLauncher.launch(
            VideoCaptureInput(
                uri,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        )
    }
}