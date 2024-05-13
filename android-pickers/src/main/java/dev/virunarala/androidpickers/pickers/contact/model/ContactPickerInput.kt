package dev.virunarala.androidpickers.pickers.contact.model

data class ContactPickerInput(
    val onSuccess: (ContactPickerOutput) -> Unit,

    val onFailure: (message: String) -> Unit
)
