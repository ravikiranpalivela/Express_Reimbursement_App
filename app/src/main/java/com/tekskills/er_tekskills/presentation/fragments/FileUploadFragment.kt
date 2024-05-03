package com.tekskills.er_tekskills.presentation.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.tekskills.er_tekskills.databinding.FragmentUploadFileBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel

class FileUploadFragment : ParentFragment() {

    private lateinit var binding: FragmentUploadFileBinding
    private lateinit var viewModel: MainActivityViewModel
    var activity: Activity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = if (context is Activity) context else null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /**
         *  Binding view into activity
         */
        binding = FragmentUploadFileBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleScope.launchWhenStarted {
                viewModel.mainEvent.collect {
                    when (it) {
                        is MainActivityViewModel.MainEvent.Error -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }

                        is MainActivityViewModel.MainEvent.UploadSuccess -> {
                            chooseBtn.isEnabled = true
                            uploadBtn.isEnabled = false
                            tvFileName.text = ""
                        }

                        is MainActivityViewModel.MainEvent.Uploading -> {
                        }

                        is MainActivityViewModel.MainEvent.FileSelected -> {
                            chooseBtn.isEnabled = false
                            uploadBtn.isEnabled = true
                            tvFileName.text = "File Selected"
                        }
                    }
                }
            }

            chooseBtn.isEnabled = true
            uploadBtn.isEnabled = false

            chooseBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
                    type = "*/*"
                }
                pdfLauncher.launch(intent)
            }
            uploadBtn.setOnClickListener {
                if (!editGenresNameInput.text.isNullOrEmpty() && !editGenresDescInput.text.isNullOrEmpty())
                    viewModel.uploadGenresFile(
                        editGenresDescInput.text.toString(),
                        editGenresDescInput.text.toString()
                    )
                else {
                    editGenresName.error = "Please Enter Valid Genres Name"
                    editGenresDesc.error = "Please Enter Valid Genres Description"
                }
            }
        }
        return binding.root
    }

    private val pdfLauncher: ActivityResultLauncher<Intent> =
        if (activity != null) {
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result?.data?.data != null) {
                    result.data?.data?.let {
                        viewModel.file =
                            requireContext().contentResolver.openInputStream(it)?.readBytes()
                    }
                }
            }
        } else {
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result?.data?.data != null) {
                    result.data?.data?.let {
                        viewModel.file =
                            requireContext().contentResolver.openInputStream(it)?.readBytes()
                    }
                }
            }
        }

    companion object {
        const val TAG: String = "myVideos"
    }
}
