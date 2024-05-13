package dev.virunarala.androidpickers.pickers.file.openfiles

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContract
import dev.virunarala.androidpickers.pickers.file.openfiles.model.OpenFilesInput
import dev.virunarala.androidpickers.pickers.file.openfiles.model.OpenFilesOutput
import dev.virunarala.androidpickers.pickers.utils.getClipDataUris

class OpenFiles: ActivityResultContract<OpenFilesInput, List<Uri>>() {

    private lateinit var onSuccess: (OpenFilesOutput) -> Unit
    private lateinit var onFailure: (message: String) -> Unit

    override fun createIntent(context: Context, input: OpenFilesInput): Intent {
        onSuccess = input.onSuccess
        onFailure = input.onFailure

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_MIME_TYPES,input.mimeTypes)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                input.initialUri?.let {
                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, it)
                }
            }
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        val uris = intent?.getClipDataUris() ?: emptyList()

        when(resultCode) {
            Activity.RESULT_OK -> {
                if(uris.isEmpty()) {
                    onFailure("Failed to open files. Check your permissions and try again")
                } else {
                    onSuccess(OpenFilesOutput(uris))
                }
            }
            Activity.RESULT_CANCELED -> onFailure("Open Files cancelled by user")
            else -> onFailure("Failed to open files.")
        }

        return uris
    }
}