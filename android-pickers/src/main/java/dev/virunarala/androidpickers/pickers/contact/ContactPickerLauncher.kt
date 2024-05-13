package dev.virunarala.androidpickers.pickers.contact

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import dev.virunarala.androidpickers.pickers.contact.model.ContactPickerInput
import dev.virunarala.androidpickers.pickers.contact.model.ContactPickerOutput

class ContactPickerLauncher {

    fun launch(activity: ComponentActivity,
               onSuccess: (ContactPickerOutput) -> Unit,
               onFailure: (message: String) -> Unit) {
        val contactPickerLauncher = activity.registerForActivityResult(
            ContactPicker()
        ) { uri ->

        }

        contactPickerLauncher.launch(
            ContactPickerInput(
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        )
    }

    fun launch(fragment: Fragment,
               onSuccess: (ContactPickerOutput) -> Unit,
               onFailure: (message: String) -> Unit) {
        val contactPickerLauncher = fragment.registerForActivityResult(
            ContactPicker()
        ) { uri ->

        }

        contactPickerLauncher.launch(
            ContactPickerInput(
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        )
    }
}