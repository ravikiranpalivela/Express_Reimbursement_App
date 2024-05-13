package dev.virunarala.androidpickers.pickers.photo.capture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import dev.virunarala.androidpickers.pickers.photo.capture.model.PhotoCaptureInput
import dev.virunarala.androidpickers.pickers.photo.capture.model.PhotoCaptureOutput

class PhotoCapture: ActivityResultContract<PhotoCaptureInput, Uri?>() {

    private var uri: Uri? = null
    private lateinit var onSuccess: (PhotoCaptureOutput) -> Unit
    private lateinit var onFailure: (message: String) -> Unit

    override fun createIntent(context: Context, input: PhotoCaptureInput): Intent {
        uri = input.uri
        onSuccess = input.onSuccess
        onFailure = input.onFailure

        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT,input.uri)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {

        if(uri==null) {
            onFailure("Failed to capture image. Check Camera permissions")
            return uri
        }

        when(resultCode) {
            Activity.RESULT_OK -> onSuccess(PhotoCaptureOutput(uri!!))
            Activity.RESULT_CANCELED -> onFailure("Photo Capture cancelled")
            else -> onFailure("Photo Capture Failed")
        }

        return uri
    }
}