package dev.virunarala.androidpickers.pickers.file.openfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContract
import dev.virunarala.androidpickers.pickers.file.openfile.model.OpenFileInput
import dev.virunarala.androidpickers.pickers.file.openfile.model.OpenFileOutput

class OpenFile: ActivityResultContract<OpenFileInput, Uri?>() {

    private lateinit var onSuccess: (OpenFileOutput) -> Unit
    private lateinit var onFailure: (message: String) -> Unit

    override fun createIntent(context: Context, input: OpenFileInput): Intent {
        onSuccess = input.onSuccess
        onFailure = input.onFailure

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            .putExtra(Intent.EXTRA_MIME_TYPES, input.getMimeTypes())
            .setType("*/*")
        return intent
    }

    override fun getSynchronousResult(
        context: Context,
        input: OpenFileInput
    ): SynchronousResult<Uri?>? {
        return null
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        val uri = intent?.data

        when(resultCode) {
            Activity.RESULT_OK -> {
                if(uri==null) {
                    onFailure("Failed to open file. Check your permissions and try again")
                } else {
                    onSuccess(OpenFileOutput(uri))
                }
            }
            Activity.RESULT_CANCELED -> onFailure("Cancelled")
            else -> onFailure("Failed to open file")
        }

        return uri
    }
}