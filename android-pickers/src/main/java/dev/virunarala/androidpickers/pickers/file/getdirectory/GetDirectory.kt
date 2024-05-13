package dev.virunarala.androidpickers.pickers.file.getdirectory

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContract
import dev.virunarala.androidpickers.pickers.file.getdirectory.model.GetDirectoryInput
import dev.virunarala.androidpickers.pickers.file.getdirectory.model.GetDirectoryOutput

class GetDirectory: ActivityResultContract<GetDirectoryInput, Uri?>() {

    private lateinit var onSuccess: (GetDirectoryOutput) -> Unit
    private lateinit var onFailure: (message: String) -> Unit

    override fun createIntent(context: Context, input: GetDirectoryInput): Intent {
        onSuccess = input.onSuccess
        onFailure = input.onFailure

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, input.initialUri)
            }
        }

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        val uri = intent?.data

        when(resultCode) {
            Activity.RESULT_OK -> {
                if(uri == null) {
                    onFailure("Failed to open the folder")
                } else {
                    onSuccess(GetDirectoryOutput(uri))
                }
            }

            Activity.RESULT_CANCELED -> {
                onFailure("Cancelled")
            }

            else -> {
                onFailure("Failed to open the folder")
            }
        }

        return uri
    }
}