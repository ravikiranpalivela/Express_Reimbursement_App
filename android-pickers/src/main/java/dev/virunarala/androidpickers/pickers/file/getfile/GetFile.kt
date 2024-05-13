package dev.virunarala.androidpickers.pickers.file.getfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import dev.virunarala.androidpickers.pickers.file.getfile.model.GetFileInput
import dev.virunarala.androidpickers.pickers.file.getfile.model.GetFileOutput

class GetFile: ActivityResultContract<GetFileInput, Uri?>() {

    private lateinit var onSuccess: (GetFileOutput) -> Unit
    private lateinit var onFailure: (message: String) -> Unit

    override fun createIntent(context: Context, input: GetFileInput): Intent {
        onSuccess = input.onSuccess
        onFailure = input.onFailure

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = input.fileType
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        val uri = intent?.data

        when(resultCode) {
            Activity.RESULT_OK -> {
                if(uri==null) {
                    onFailure("Failed to get the file. Check your permissions and try again")
                } else {
                    onSuccess(GetFileOutput(uri))
                }
            }
            Activity.RESULT_CANCELED -> onFailure("Get Files cancelled by user")
            else -> onFailure("Failed to get files.")
        }

        return uri
    }
}