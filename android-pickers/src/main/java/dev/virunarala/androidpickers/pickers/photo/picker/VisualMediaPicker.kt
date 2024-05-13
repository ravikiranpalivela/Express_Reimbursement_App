package dev.virunarala.androidpickers.pickers.photo.picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.ext.SdkExtensions
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import dev.virunarala.androidpickers.pickers.photo.picker.model.VisualMediaPickerInput
import dev.virunarala.androidpickers.pickers.photo.picker.model.VisualMediaPickerOutput

private const val GMS_ACTION_PICK_IMAGES = "com.google.android.gms.provider.action.PICK_IMAGES"

class VisualMediaPicker: ActivityResultContract<VisualMediaPickerInput, Uri?>() {

    private lateinit var onSuccess: (VisualMediaPickerOutput) -> Unit
    private lateinit var onFailure: (message: String) -> Unit

    override fun createIntent(context: Context, input: VisualMediaPickerInput): Intent {

        onSuccess = input.onSuccess
        onFailure = input.onFailure

        return if (isSystemPickerAvailable()) {
            Intent(MediaStore.ACTION_PICK_IMAGES).apply {
                type = getVisualMimeType(input.mediaType)
            }
        } else if (isSystemFallbackPickerAvailable(context)) {
            val fallbackPicker = checkNotNull(
                getSystemFallbackPicker(
                    context
                )
            ).activityInfo
            Intent(ActivityResultContracts.PickVisualMedia.ACTION_SYSTEM_FALLBACK_PICK_IMAGES).apply {
                setClassName(fallbackPicker.applicationInfo.packageName, fallbackPicker.name)
                type = getVisualMimeType(input.mediaType)
            }
        } else if (isGmsPickerAvailable(context)) {
            val gmsPicker = checkNotNull(
                getGmsPicker(
                    context
                )
            ).activityInfo
            Intent(GMS_ACTION_PICK_IMAGES).apply {
                setClassName(gmsPicker.applicationInfo.packageName, gmsPicker.name)
                type = getVisualMimeType(input.mediaType)
            }
        } else {
            // For older devices running KitKat and higher and devices running Android 12
            // and 13 without the SDK extension that includes the Photo Picker, rely on the
            // ACTION_OPEN_DOCUMENT intent
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = getVisualMimeType(input.mediaType)

                if (type == null) {
                    // ACTION_OPEN_DOCUMENT requires to set this parameter when launching the
                    // intent with multiple mime types
                    type = "*/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
                }
            }
        }
    }

    private fun isSystemPickerAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // getExtension is seen as part of Android Tiramisu only while the SdkExtensions
            // have been added on Android R
            SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2
        } else {
            false
        }
    }

    private fun getVisualMimeType(input: ActivityResultContracts.PickVisualMedia.VisualMediaType): String? {
        return when (input) {
            is ActivityResultContracts.PickVisualMedia.ImageOnly -> "image/*"
            is ActivityResultContracts.PickVisualMedia.VideoOnly -> "video/*"
            is ActivityResultContracts.PickVisualMedia.SingleMimeType -> input.mimeType
            is ActivityResultContracts.PickVisualMedia.ImageAndVideo -> null
        }
    }

    private fun isSystemFallbackPickerAvailable(context: Context): Boolean {
        return getSystemFallbackPicker(context) != null
    }

    private fun getSystemFallbackPicker(context: Context): ResolveInfo? {
        return context.packageManager.resolveActivity(
            Intent(ActivityResultContracts.PickVisualMedia.ACTION_SYSTEM_FALLBACK_PICK_IMAGES),
            PackageManager.MATCH_DEFAULT_ONLY or PackageManager.MATCH_SYSTEM_ONLY
        )
    }

    private fun isGmsPickerAvailable(context: Context): Boolean {
        return getGmsPicker(context) != null
    }

    private fun getGmsPicker(context: Context): ResolveInfo? {
        return context.packageManager.resolveActivity(
            Intent(GMS_ACTION_PICK_IMAGES),
            PackageManager.MATCH_DEFAULT_ONLY or PackageManager.MATCH_SYSTEM_ONLY
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {

        val uri = intent.takeIf { resultCode == Activity.RESULT_OK }?.run {
            // Check both the data URI and ClipData since the GMS picker
            // only returns results through getClipDataUris()

            data ?: getClipDataUris().firstOrNull()
        }

        if(uri==null) {
            onFailure("Image not selected. Try again")
        } else {
            onSuccess(VisualMediaPickerOutput(uri))
        }

        return uri
    }

    private fun Intent.getClipDataUris(): List<Uri> {
        // Use a LinkedHashSet to maintain any ordering that may be
        // present in the ClipData
        val resultSet = LinkedHashSet<Uri>()
        data?.let { data ->
            resultSet.add(data)
        }
        val clipData = clipData
        if (clipData == null && resultSet.isEmpty()) {
            return emptyList()
        } else if (clipData != null) {
            for (i in 0 until clipData.itemCount) {
                val uri = clipData.getItemAt(i).uri
                if (uri != null) {
                    resultSet.add(uri)
                }
            }
        }
        return ArrayList(resultSet)
    }
}