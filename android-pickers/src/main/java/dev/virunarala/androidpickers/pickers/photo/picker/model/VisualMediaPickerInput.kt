package dev.virunarala.androidpickers.pickers.photo.picker.model

import androidx.activity.result.contract.ActivityResultContracts

data class VisualMediaPickerInput(

    val mediaType: ActivityResultContracts.PickVisualMedia.VisualMediaType,

    val onSuccess: (VisualMediaPickerOutput) -> Unit,

    val onFailure: (message: String) -> Unit
)