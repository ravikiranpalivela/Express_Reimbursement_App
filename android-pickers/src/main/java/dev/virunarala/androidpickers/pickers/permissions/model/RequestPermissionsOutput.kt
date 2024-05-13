package dev.virunarala.androidpickers.pickers.permissions.model

data class RequestPermissionsOutput(
    val isPermissionGranted: Map<String,Boolean>
) {
    fun arePermissionsGranted() = isPermissionGranted.values.all{
        it == true
    }
}
