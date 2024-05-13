package dev.virunarala.androidpickers.pickers.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import dev.virunarala.androidpickers.pickers.permissions.model.RequestPermissionsInput
import dev.virunarala.androidpickers.pickers.permissions.model.RequestPermissionsOutput

class RequestPermissions: ActivityResultContract<RequestPermissionsInput, Map<String,Boolean>>() {

    private lateinit var onSuccess: (RequestPermissionsOutput) -> Unit
    private lateinit var onFailure: (message: String) -> Unit

    companion object {
        const val ACTION_REQUEST_PERMISSIONS = "androidx.activity.result.contract.action.REQUEST_PERMISSIONS"
        const val EXTRA_PERMISSIONS = "androidx.activity.result.contract.extra.PERMISSIONS"
        const val EXTRA_PERMISSION_GRANT_RESULTS = "androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS"

        internal fun createIntent(input: Array<String>): Intent {
            return Intent(ACTION_REQUEST_PERMISSIONS).putExtra(EXTRA_PERMISSIONS, input)
        }
    }

    override fun createIntent(context: Context, input: RequestPermissionsInput): Intent {
        onSuccess = input.onSuccess
        onFailure = input.onFailure
        return createIntent(input.permissions)
    }

    override fun getSynchronousResult(
        context: Context,
        input: RequestPermissionsInput
    ): SynchronousResult<Map<String, Boolean>>? {
        if(input.permissions.isEmpty()) {
            return SynchronousResult(emptyMap())
        }
        val allGranted = input.permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        return if(allGranted) {
            SynchronousResult(input.permissions.associate { it to true})
        } else null
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Map<String, Boolean> {
        if(resultCode != Activity.RESULT_OK) {
            onFailure("Error: Result Code: $resultCode.")
            return emptyMap()
        }
        if(intent == null) {
            onFailure("Error: Null Intent")
            return emptyMap()
        }

        val permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS)
        val grantResults = intent.getIntArrayExtra(EXTRA_PERMISSION_GRANT_RESULTS)
        if(grantResults==null || permissions==null) {
            return emptyMap()
        }

        val grantState = grantResults.map { result ->
            result == PackageManager.PERMISSION_GRANTED
        }
        return permissions.filterNotNull().zip(grantState).toMap()
    }
}