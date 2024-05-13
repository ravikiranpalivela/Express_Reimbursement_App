package dev.virunarala.androidpickers.pickers.photo.picker

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.photo.picker.model.VisualMediaPickerInput
import dev.virunarala.androidpickers.pickers.photo.picker.model.VisualMediaPickerOutput

class VisualMediaPickerLauncher private constructor(activity: ComponentActivity? = null,
                                                    fragment: Fragment? = null
) {
    constructor(activity: ComponentActivity): this(activity,null)
    constructor(fragment: Fragment): this(null,fragment)

    private val activity = activity ?: fragment!!.requireActivity()

    private val visualMediaPickerLauncher = this.activity.registerForActivityResult(
        VisualMediaPicker()
    ) { uri ->

    }

    fun launch(mediaType: ActivityResultContracts.PickVisualMedia.VisualMediaType,
               onSuccess: (VisualMediaPickerOutput) -> Unit,
               onFailure: (message: String) -> Unit) {
        visualMediaPickerLauncher.launch(
            VisualMediaPickerInput(
                mediaType,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        )
    }
}