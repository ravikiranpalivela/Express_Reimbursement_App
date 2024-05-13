package dev.virunarala.androidpickers.pickers.file.getfiles

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import dev.virunarala.androidpickers.pickers.file.getfiles.model.GetFilesInput
import dev.virunarala.androidpickers.pickers.file.getfiles.model.GetFilesOutput
import dev.virunarala.androidpickers.pickers.utils.getClipDataUris

class GetFiles: ActivityResultContract<GetFilesInput, List<Uri>>() {

    private lateinit var onSuccess: (GetFilesOutput) -> Unit
    private lateinit var onFailure: (message: String) -> Unit

    override fun createIntent(context: Context, input: GetFilesInput): Intent {
        onSuccess = input.onSuccess
        onFailure = input.onFailure

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = input.fileType
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        val uris = intent?.getClipDataUris() ?: emptyList()

        when(resultCode) {
            Activity.RESULT_OK -> {
                if(uris.isEmpty()) {
                    onFailure("Failed to get files. Check your permissions and try again")
                } else {
                    onSuccess(GetFilesOutput(uris))
                }
            }
            Activity.RESULT_CANCELED -> onFailure("Get Files cancelled by user")
            else -> onFailure("Failed to get files.")
        }

        return uris
    }
}