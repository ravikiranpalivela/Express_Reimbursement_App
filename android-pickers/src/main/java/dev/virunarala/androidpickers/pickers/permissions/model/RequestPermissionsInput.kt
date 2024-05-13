package dev.virunarala.androidpickers.pickers.permissions.model

data class RequestPermissionsInput(

    val permissions: Array<String>,

    val onSuccess: (RequestPermissionsOutput) -> Unit,

    val onFailure: (message: String) -> Unit
)
