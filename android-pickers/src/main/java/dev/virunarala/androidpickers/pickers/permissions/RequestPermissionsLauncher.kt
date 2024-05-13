package dev.virunarala.androidpickers.pickers.permissions

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.permissions.model.RequestPermissionsInput
import dev.virunarala.androidpickers.pickers.permissions.model.RequestPermissionsOutput

class RequestPermissionsLauncher private constructor(activity: ComponentActivity? = null,
                                                     fragment: Fragment? = null
) {
    constructor(activity: ComponentActivity): this(activity,null)
    constructor(fragment: Fragment): this(null,fragment)

    private val activity = activity ?: fragment!!.requireActivity()

    private val requestPermissionsLauncher = this.activity.registerForActivityResult(
        RequestPermissions()
    ) { arePermissionsGranted ->

    }

    fun launch(permissions: Array<String>,
               onSuccess: (RequestPermissionsOutput) -> Unit,
               onFailure: (message: String) -> Unit) {

        requestPermissionsLauncher.launch(
            RequestPermissionsInput(
                permissions,
                onSuccess,
                onFailure
            )
        )
    }
}