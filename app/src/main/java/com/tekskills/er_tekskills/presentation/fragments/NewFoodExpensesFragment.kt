package com.tekskills.er_tekskills.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.AddFoodExpenceRequest
import com.tekskills.er_tekskills.databinding.FragmentNewFoodExpensesBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.adapter.AddImageAdapter
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.AppUtil.showSnackBar
import com.tekskills.er_tekskills.utils.FileCompressor
import com.tekskills.er_tekskills.utils.RestApiStatus
import com.tekskills.er_tekskills.utils.SmartDialog
import com.tekskills.er_tekskills.utils.SmartDialogBuilder
import com.tekskills.er_tekskills.utils.SmartDialogClickListener
import com.tekskills.er_tekskills.utils.SuccessResource
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class NewFoodExpensesFragment : Fragment() {
    private lateinit var binding: FragmentNewFoodExpensesBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActivityViewModel
    private var purposeID: String = ""

    private val args: NewFoodExpensesFragmentArgs by navArgs()

    private var listImage: MutableList<File> = ArrayList()
    private var selectedSelectImage: Int = 0
    private val listSelectImage = arrayOf("Take Photo", "Choose from Gallery")

    private lateinit var addImageAdapter: AddImageAdapter
    private lateinit var fileCompressor: FileCompressor
    private lateinit var fileImage: File
    private lateinit var dialog: AlertDialog
    private var validated:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_food_expenses, container, false)
        fileCompressor = FileCompressor(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        navController = findNavController()
        showSnackBar(binding.root)
        purposeID = args.opportunityID
        viewModel._resAddFoodExpence.postValue(SuccessResource.loading(null))

//        viewModel.getClientNameList()

        viewModel.resNewClientResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.getMeetingPurposeByID(purposeID)


        binding.apply {
            llFoodExpensesInfo.setOnClickListener(View.OnClickListener {
                if (llFoodExpenses.isVisible) {
                    llFoodExpenses.visibility = View.GONE
                    ivViewFoodExpensesInfo.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    llFoodExpenses.visibility = View.VISIBLE
                    ivViewFoodExpensesInfo.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
            ivViewFoodExpensesInfo.setOnClickListener(View.OnClickListener {
                if (llFoodExpenses.isVisible) {
                    llFoodExpenses.visibility = View.GONE
                    ivViewFoodExpensesInfo.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    llFoodExpenses.visibility = View.VISIBLE
                    ivViewFoodExpensesInfo.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
        }


        viewModel.resMeetingPurposeIDItems.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.status) {
                    RestApiStatus.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        if (it.data != null) {
                            it.data.let { list ->
                                binding.taskCategoryInfo = list

//                                binding.priority.text = if (list.status == "Active")
//                                    "Active" else "InActive"
                            }
                        } else {
                            Snackbar.make(
                                binding.root,
                                "No Data Found",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }

                    RestApiStatus.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                    }

                    RestApiStatus.ERROR -> {
                        binding.progress.visibility = View.GONE
                        Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                            .show()
                    }

                    else -> {
                        binding.progress.visibility = View.GONE
                        Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            })

        viewModel.resAddFoodExpence.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            if(validated)
                            requireActivity().onBackPressed()
//                            val intent = Intent(requireActivity(), MainActivity::class.java)
//                            startActivity(intent)
//                            requireActivity().finish()
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        binding.ivAddImage.setOnClickListener {
            selectImage()
        }
        initAdapter()

        binding.btnSave.setOnClickListener {
            if (isValidate()) {

                Log.d("TAG", "onViewCreated: validated successfully")

//                if (binding.edtFoodAmt.text.toString().toDouble() >
//                    binding.taskCategoryInfo!!.allowncesLimit.foodLimit.toString()
//                        .toDouble()
//                ) {
//                    Log.d("TAG", "onViewCreated: showing alert dialog exceeds")

//                    SmartDialogBuilder(requireContext())
//                        .setTitle("Note")
//                        .setSubTitle("Manager Approval Mandatory")
//                        .setCancalable(false)
//                        .setCustomIcon(R.drawable.icon2)
//                        .setTitleColor(resources.getColor(R.color.black))
//                        .setSubTitleColor(resources.getColor(R.color.black))
//                        .setNegativeButtonHide(true)
//                        .useNeutralButton(true)
//                        .setPositiveButton("Okay", object : SmartDialogClickListener {
//                            override fun onClick(smartDialog: SmartDialog?) {
//                                Log.d("TAG", "onViewCreated: okay for alert dialog exceeds")
                                addHotelExpenseDetails()
//                                smartDialog!!.dismiss()
//                            }
//                        })
//                        .setNegativeButton("Cancel", object : SmartDialogClickListener {
//                            override fun onClick(smartDialog: SmartDialog?) {
//                                smartDialog!!.dismiss()
//                            }
//                        })
//                        .setNeutralButton("Cancel", object : SmartDialogClickListener {
//                            override fun onClick(smartDialog: SmartDialog?) {
//                                smartDialog!!.dismiss()
//                            }
//                        })
//                        .build().show()
//                } else {
//
//                    Log.d("TAG", "onViewCreated: not exceeds")
//                    addHotelExpenseDetails()
//                    val hotelExpense = AddHotelExpenceRequest(
//                        purposeId = purposeID.toInt(),
//                        hotelFrom = binding.srcLoc.text.toString(),
//                        hotelTo = binding.desLoc.text.toString(),
//                        hotelDate = DateToString.convertDateStringToNormalFormat(
//                            binding.clientName.text.toString()
//                        ),
//                        modeOfHotel = selectMOTPos,
//                        amount = binding.edtHotelExpenses.text.toString().toDouble(),
////                    noOfDays = binding.edtNoOfDays.text.toString(),
//                        expensesUser = "Hotel",
//                    )
//
//                    viewModel.addHotelExpense(hotelExpense, listImage)
//                }


//                viewModel.addProject(
//                    binding.edtFoodAmt.text.toString(),
//                    binding.edtFoodComment.text.toString(),
//                    "","",""
//                )
//                Toast.makeText(requireActivity(), "Added", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initAdapter() {
        addImageAdapter = AddImageAdapter(listImage)
        binding.rvImage.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = addImageAdapter

            addImageAdapter.setOnCustomClickListener(object :
                AddImageAdapter.OnCustomClickListener {
                override fun onDeleteClicked(position: Int) {
                    listImage.removeAt(position)
                    addImageAdapter.notifyDataSetChanged()
                }
            })
        }
    }

    private fun selectImage() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setItems(listSelectImage) { _, item ->
            when {
                listSelectImage[item] == "Take Photo" -> {
                    selectedSelectImage = 0
//                    if (checkPersmission()) {
                    takePhoto()
//                    } else {
//                        requestPermission()
//                    }
                }

                listSelectImage[item] == "Choose from Gallery" -> {
                    selectedSelectImage = 1
//                    if (checkPersmission()) {
                    openGallery()
//                    } else {
//                        requestPermission()
//                    }
                }
            }
        }
        builder.show()
    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                if (selectedSelectImage == 0) {
                    takePhoto()
                } else {
                    openGallery()
                }

            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileImage = createFile()
        val uri =
            FileProvider.getUriForFile(
                requireContext(), "com.tekskills.er_tekskills.fileprovider",
                fileImage
            )

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_TAKE_PHOTO)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_OPEN_GALLERY)
    }



    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("IMG_${timeStamp}", ".jpg", storageDir)
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        return try {
            fileImage = createFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapdata = bos.toByteArray()

            val fos = FileOutputStream(fileImage)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            fileImage
        } catch (e: Exception) {
            e.printStackTrace()
            fileImage
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                fileImage = fileCompressor.compressToFile(fileImage)!!
                listImage.add(fileImage)
                addImageAdapter.notifyDataSetChanged()
            }
        } else if (requestCode == REQUEST_OPEN_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            data!!.data!!
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        data!!.data!!
                    )
                }
                val tempFile = fileCompressor.compressToFile(bitmapToFile(bitmap))
                listImage.add(tempFile!!)
                addImageAdapter.notifyDataSetChanged()
            }
        }
    }

    fun addHotelExpenseDetails() {

        try {

            val hotelExpense = AddFoodExpenceRequest(
                purposeId = purposeID.toInt(),
                foodComments = binding.edtFoodComment.text.toString(),
                foodAmount = binding.edtFoodAmt.text.toString()
                    .toDouble(),
//                    noOfDays = binding.edtNoOfDays.text.toString(),
                expensesUser = "foodexpence",
            )
            validated = true
            viewModel.addFoodExpense(hotelExpense,listImage)
        } catch (e: Exception) {
            Log.d("TAG", "addHotelExpenseDetails: ${e.message}")
        }
    }

    private fun isValidate(): Boolean =
        validateFoodAmount() && validateFoodComment() &&validateAttachment()

    /**
     * field must not be empty
     */
    private fun validateFoodAmount(): Boolean {
        if (binding.edtFoodAmt.text.toString().trim().isEmpty()) {
            binding.edtFoodAmt.error = "Required Field!"
            binding.edtFoodAmt.requestFocus()
            return false
        } else {
            binding.edtFoodAmt.error = null
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateFoodComment(): Boolean {
        if (binding.edtFoodComment.text.toString().trim().isEmpty()) {
            binding.edtFoodComment.error = "Required Field!"
            binding.edtFoodComment.requestFocus()
            return false
        } else {
            binding.edtFoodComment.error = null
        }
        return true
    }

    private fun validateAttachment(): Boolean {

        if (listImage.size == 0) {
//        if (binding.tvFileName.text.toString().trim().isEmpty()) {
            binding.tvFileName.error = "Required Field!"
            binding.tvFileName.requestFocus()
            return false
        } else {
            binding.tvFileName.error = null
        }
        return true
    }

    companion object {
        private const val REQUEST_TAKE_PHOTO = 1
        private const val REQUEST_OPEN_GALLERY = 2
        private const val PERMISSION_REQUEST_CODE = 100
    }

}