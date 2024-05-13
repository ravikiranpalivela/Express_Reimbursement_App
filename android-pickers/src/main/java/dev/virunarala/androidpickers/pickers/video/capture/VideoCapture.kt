package dev.virunarala.androidpickers.pickers.video.capture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import dev.virunarala.androidpickers.pickers.video.capture.model.VideoCaptureInput
import dev.virunarala.androidpickers.pickers.video.capture.model.VideoCaptureOutput

class VideoCapture: ActivityResultContract<VideoCaptureInput, Uri?>() {

    private var uri: Uri? = null
    private lateinit var onSuccess: (VideoCaptureOutput) -> Unit
    private lateinit var onFailure: (message: String) -> Unit

    override fun createIntent(context: Context, input: VideoCaptureInput): Intent {
        uri = input.uri
        onSuccess = input.onSuccess
        onFailure = input.onFailure

        return Intent(MediaStore.ACTION_VIDEO_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT,input.uri)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {

        if(uri==null) {
            onFailure("Failed to capture video. Check Camera permissions")
            return uri
        }

        when(resultCode) {
            Activity.RESULT_OK -> onSuccess(VideoCaptureOutput(uri!!))
            Activity.RESULT_CANCELED -> onFailure("Video Capture cancelled.")
            else -> onFailure("Video capture failed.")
        }

        return uri
    }
}