package com.tekskills.er_tekskills.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.AddReturnTravelExpenceRequest
import com.tekskills.er_tekskills.data.model.AddTravelExpenceRequest
import com.tekskills.er_tekskills.data.util.Constants
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.data.util.DateToString.Companion.convertStringToDateformat
import com.tekskills.er_tekskills.databinding.FragmentNewTravelExpensesBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.adapter.AddImageAdapter
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.FileCompressor
import com.tekskills.er_tekskills.utils.RestApiStatus
import com.tekskills.er_tekskills.utils.SmartDialog
import com.tekskills.er_tekskills.utils.SmartDialogBuilder
import com.tekskills.er_tekskills.utils.SmartDialogClickListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class NewTravelExpensesFragment : Fragment() {

    private lateinit var binding: FragmentNewTravelExpensesBinding
    private lateinit var viewModel: MainActivityViewModel
    private var selectMOTPos = "Bike"
    private var mClientNames: ArrayList<String> = arrayListOf(
        "Train", "Bus", "Bike", "Car", "Flight"
    )

    private lateinit var addImageAdapter: AddImageAdapter
    private lateinit var fileCompressor: FileCompressor
    private lateinit var fileImage: File
    private lateinit var dialog: AlertDialog
    private var listImage: MutableList<File> = ArrayList()
    private var selectedSelectImage: Int = 0


    private var purposeID: String = ""
    private val args: NewTravelExpensesFragmentArgs by navArgs()
    var travelDate: Date = Date(Constants.MAX_TIMESTAMP)
    private var selectReturnMOTPos = 0

    private val listSelectImage = arrayOf("Take Photo", "Choose from Gallery")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_travel_expenses,
            container,
            false
        )
        fileCompressor = FileCompressor(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        purposeID = args.opportunityID

        viewModel.resMeetingPurposeIDItems.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.status) {
                    RestApiStatus.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        if (it.data != null) {
                            it.data.let { list ->
                                binding.taskCategoryInfo = list

                                if (list.modeOfTravel != null) {
                                    selectMOTPos = list.modeOfTravel
                                    binding.sModeOfTravel.setSelection(list.modeOfTravel)
                                    if (!(list.modeOfTravel == "Train" || list.modeOfTravel == "Flight")) {
                                        binding.llDistance.visibility = View.VISIBLE
                                        binding.llAllottedExpenses.visibility = View.VISIBLE
                                    } else {
                                        binding.llDistance.visibility = View.GONE
                                        binding.llAllottedExpenses.visibility = View.GONE
                                    }
                                }

                                binding.priority.text = if (list.status == "Active")
                                    "Active" else "InActive"
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

        binding.ivAddImage.setOnClickListener {
            selectImage()
        }

        viewModel.getMeetingPurposeByID(purposeID)


//        viewModel.getClientNameList()

//        val spinnerData: ArrayList<String> = arrayListOf(mClientNames)
//        val spinnerData = arrayListOf("Kotlin", "Java", "Python", "Php", "Swift")
//        binding.sJourneyTypes.buildCheckedSpinner(spinnerData) { selectedPositionList, displayString ->
////            tvSelectedPosition.text = "Selected position:  $selectedPositionList"
////            tvDispString.text = "Display String:  $displayString"
//        }

//        binding.sModeOfTravel.setItems(mClientNames)
//
//        binding.sModeOfTravel.setOnItemSelectListener(object :
//            SearchableSpinner.SearchableItemListener {
//            override fun onItemSelected(view: View?, position: Int) {
//                selectMOTPos = mClientNames[position]
//            }
//
//            override fun onSelectionClear() {
//
//            }
//        })

        binding.tvReturnTravelDate.setOnClickListener {
            showToDatePicker()
        }

//        viewModel.resClientNameList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            when (it.status) {
//                RestApiStatus.SUCCESS -> {
//                    binding.progress.visibility = View.GONE
//                    if (it.data != null)
//                        it.data.let { res ->
//                            mClientNames = res
//                            binding.sJourneyTypes.setItems(res)
//                        }
//                    else {
//                        Snackbar.make(
//                            binding.root,
//                            "Login Failed",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                RestApiStatus.LOADING -> {
//                    binding.progress.visibility = View.VISIBLE
//                }
//
//                RestApiStatus.ERROR -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//
//                else -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        })

//        binding.edtModeOfTravel.setOn{
//
//        }


//        { adapterView, view, position, id ->
//            val selectedMode = adapterView.getItemAtPosition(position).toString()
//            println("Selected Mode of Travel: $selectedMode")
//        }

        viewModel.resAddTravelExpence.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
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
        initAdapter()

        binding.apply {
            llTravelExpenses.setOnClickListener(View.OnClickListener {
                if (clVistPurpose.isVisible) {
                    clVistPurpose.visibility = View.GONE
                    ivViewTravelExpenses.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clVistPurpose.visibility = View.VISIBLE
                    ivViewTravelExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
            ivViewTravelExpenses.setOnClickListener(View.OnClickListener {
                if (clVistPurpose.isVisible) {
                    clVistPurpose.visibility = View.GONE
                    ivViewTravelExpenses.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clVistPurpose.visibility = View.VISIBLE
                    ivViewTravelExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
        }

        binding.chkRoundTrip.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.llRoundTripDetails.visibility = View.VISIBLE
            } else {
                binding.llRoundTripDetails.visibility = View.GONE
            }
        })

        binding.btnSave.setOnClickListener {
            if (isValidate()) {

                Log.d("TAG", "onViewCreated: validated successfully")

                if (binding.edtTravelExpenses.text.toString().toDouble() >
                    binding.taskCategoryInfo!!.allowncesLimit.travelLimit.toString()
                        .toDouble()
                ) {
                    Log.d("TAG", "onViewCreated: showing alert dialog exceeds")

                    SmartDialogBuilder(requireContext())
                        .setTitle("Note")
                        .setSubTitle("Manager Approval Mandatory")
                        .setCancalable(false)
                        .setCustomIcon(R.drawable.icon2)
                        .setTitleColor(resources.getColor(R.color.black))
                        .setSubTitleColor(resources.getColor(R.color.black))
                        .setNegativeButtonHide(true)
                        .useNeutralButton(true)
                        .setPositiveButton("Okay", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                Log.d("TAG", "onViewCreated: okay for alert dialog exceeds")
                                addTravelExpenseDetails()
                                smartDialog!!.dismiss()
                            }
                        })
                        .setNegativeButton("Cancel", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                smartDialog!!.dismiss()
                            }
                        })
                        .setNeutralButton("Cancel", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                smartDialog!!.dismiss()
                            }
                        })
                        .build().show()
                } else {

                    Log.d("TAG", "onViewCreated: not exceeds")
                    addTravelExpenseDetails()
//                    val travelExpense = AddTravelExpenceRequest(
//                        purposeId = purposeID.toInt(),
//                        travelFrom = binding.srcLoc.text.toString(),
//                        travelTo = binding.desLoc.text.toString(),
//                        travelDate = DateToString.convertDateStringToNormalFormat(
//                            binding.clientName.text.toString()
//                        ),
//                        modeOfTravel = selectMOTPos,
//                        amount = binding.edtTravelExpenses.text.toString().toDouble(),
////                    noOfDays = binding.edtNoOfDays.text.toString(),
//                        expensesUser = "Travel",
//                    )
//
//                    viewModel.addTravelExpense(travelExpense, listImage)
                }


//                viewModel.addProject(
//                    binding.edtFromLoc.text.toString(),
//                    selectMOTPos.toString(),
//                    binding.edtToLoc.text.toString(),
//                    binding.tvTravelDate.text.toString(),
//                    if (binding.chkRoundTrip.isChecked) "Active" else "InActive"
//                )
//                Toast.makeText(requireActivity(), "Added", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivAddFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
                type = "*/*"
            }
            pdfLauncher.launch(intent)
//            documentPick.launch(
//                arrayOf(
//                    "application/pdf",
//                    "application/msword",
//                    "application/ms-doc",
//                    "application/doc",
//                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
//                    "text/plain"
//                )
//            )
        }
    }

    fun addTravelExpenseDetails() {
        val travelExpense = AddTravelExpenceRequest(
            purposeId = purposeID.toInt(),
            travelFrom = binding.srcLoc.text.toString(),
            travelTo = binding.desLoc.text.toString(),
            travelDate = DateToString.convertDateStringToNormalFormat(
                binding.clientName.text.toString()
            ),
            modeOfTravel = selectMOTPos,
            amount = binding.edtTravelExpenses.text.toString()
                .toDouble(),
//                    noOfDays = binding.edtNoOfDays.text.toString(),
            expensesUser = "Travel",
        )
        if (!binding.chkRoundTrip.isChecked) {
            viewModel.addTravelExpense(travelExpense, null, listImage)
        } else {
            val returnTravelExpense = AddReturnTravelExpenceRequest(
                returnFrom = binding.desLoc.text.toString(),
                returnTo = binding.srcLoc.text.toString(),
                returnTravelDate = DateToString.convertDateToString(travelDate),
                returnModeOfTravel = selectMOTPos,
            )
            viewModel.addTravelExpense(travelExpense, returnTravelExpense, listImage)
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

//            if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(
                requireContext(), "com.tekskills.er_tekskills.fileprovider",
                fileImage
            )
//        } else {
//            Uri.fromFile(fileImage)
//        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_TAKE_PHOTO)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.startActivityForResult(intent, REQUEST_OPEN_GALLERY)
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

    private fun isValidate(): Boolean =
        if (binding.chkRoundTrip.isChecked)
            validateAttachment() && validateTravelAmount() && validateReturnTravelDate()
        else
            validateAttachment() && validateTravelAmount()

    /**
     * field must not be empty
     */
//    private fun validateFromLocation(): Boolean {
//        if (binding.edtFromLoc.text.toString().trim().isEmpty()) {
//            binding.edtFromLoc.error = "Required Field!"
//            binding.edtFromLoc.requestFocus()
//            return false
//        } else {
//            binding.edtFromLoc.error = null
//        }
//        return true
//    }

    private fun validateModeOfTravelLocation(): Boolean {
        if (selectMOTPos == "") {
//            binding.edtModeOfTravel.error = "Required Field!"
//            binding.edtModeOfTravel.requestFocus()
            return false
        } else {
//            binding.edtModeOfTravel.error = null
        }
        return true
    }

    /**
     * field must not be empty
     */
//    private fun validateToLocation(): Boolean {
//        if (binding.edtToLoc.text.toString().trim().isEmpty()) {
//            binding.edtToLoc.error = "Required Field!"
//            binding.edtToLoc.requestFocus()
//            return false
//        } else {
//            binding.edtToLoc.error = null
//        }
//        return true
//    }

    /**
     * field must not be empty
     */
//    private fun validateTravelDate(): Boolean {
//        if (binding.tvTravelDate.text.toString().trim().isEmpty()) {
//            binding.tvTravelDate.error = "Required Field!"
//            binding.tvTravelDate.requestFocus()
//            return false
//        } else {
//            binding.tvTravelDate.error = null
//        }
//        return true
//    }


    private fun showToDatePicker() {
        val date = binding.taskCategoryInfo!!.visitDate
//        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
//        val dateString = convertStringToDateformat(date)
        val myCal: Calendar = GregorianCalendar()
        try {
            myCal.time = convertStringToDateformat(date)!!
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        val _date: DatePickerDialog = object : DatePickerDialog(
            requireContext(), mDateToSetListener,
            myCal[Calendar.YEAR], myCal[Calendar.MONTH],
            myCal[Calendar.DAY_OF_MONTH]
        ) {
            override fun onDateChanged(
                view: DatePicker,
                year: Int,
                monthOfYear: Int,
                dayOfMonth: Int
            ) {
                if (year < myCal[Calendar.YEAR]) view.updateDate(
                    myCal[Calendar.YEAR],
                    myCal[Calendar.MONTH], myCal[Calendar.DAY_OF_MONTH]
                )
                if (monthOfYear < myCal[Calendar.MONTH] && year == myCal[Calendar.YEAR]) view.updateDate(
                    myCal[Calendar.YEAR], myCal[Calendar.MONTH], myCal[Calendar.DAY_OF_MONTH]
                )
                if (dayOfMonth < myCal[Calendar.DAY_OF_MONTH] && year == myCal[Calendar.YEAR] && monthOfYear == myCal[Calendar.MONTH]) view.updateDate(
                    myCal[Calendar.YEAR], myCal[Calendar.MONTH], myCal[Calendar.DAY_OF_MONTH]
                )
            }
        }
        _date.show()
    }

    private val mDateToSetListener =
        DatePickerDialog.OnDateSetListener { view, yearSelected, monthOfYear, dayOfMonth ->

            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = it
            calendar.set(Calendar.YEAR, yearSelected)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            travelDate = calendar.time
            binding.tvReturnTravelDate.setText(DateToString.convertDateToStringDateWise(travelDate))

//            year = yearSelected
//            month = monthOfYear + 1
//            day = dayOfMonth
//
//
//            binding.edtDate.setText("" + year + "/" + month + "/" + day)
//            if (day < 10) {
//                StartDate =
//                    "0" + year + "/" + month + "/" + day
//            } else {
//                StartDate =
//                    "" + year + "/" + month + "/" + day
//            }
        }

    private fun showDatePicker() {
        val date = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val dateString = sdf.format(date)
        val myCal: Calendar = GregorianCalendar()
        try {
            val theDate = sdf.parse(dateString)
            myCal.time = theDate
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        val _date: DatePickerDialog = object : DatePickerDialog(
            requireContext(), mDateSetListener,
            myCal[Calendar.YEAR], myCal[Calendar.MONTH],
            myCal[Calendar.DAY_OF_MONTH]
        ) {
            override fun onDateChanged(
                view: DatePicker,
                year: Int,
                monthOfYear: Int,
                dayOfMonth: Int
            ) {
                if (year < myCal[Calendar.YEAR]) view.updateDate(
                    myCal[Calendar.YEAR],
                    myCal[Calendar.MONTH], myCal[Calendar.DAY_OF_MONTH]
                )
                if (monthOfYear < myCal[Calendar.MONTH] && year == myCal[Calendar.YEAR]) view.updateDate(
                    myCal[Calendar.YEAR], myCal[Calendar.MONTH], myCal[Calendar.DAY_OF_MONTH]
                )
                if (dayOfMonth < myCal[Calendar.DAY_OF_MONTH] && year == myCal[Calendar.YEAR] && monthOfYear == myCal[Calendar.MONTH]) view.updateDate(
                    myCal[Calendar.YEAR], myCal[Calendar.MONTH], myCal[Calendar.DAY_OF_MONTH]
                )
            }
        }
        _date.show()
    }

    private val mDateSetListener =
        DatePickerDialog.OnDateSetListener { view, yearSelected, monthOfYear, dayOfMonth ->

            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = it
            calendar.set(Calendar.YEAR, yearSelected)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            travelDate = calendar.time
//            binding.tvTravelDate.setText(DateToString.convertDateToStringDateWise(travelDate))

//            year = yearSelected
//            month = monthOfYear + 1
//            day = dayOfMonth
//
//
//            binding.edtDate.setText("" + year + "/" + month + "/" + day)
//            if (day < 10) {
//                StartDate =
//                    "0" + year + "/" + month + "/" + day
//            } else {
//                StartDate =
//                    "" + year + "/" + month + "/" + day
//            }
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

    private fun validateTravelAmount(): Boolean {
        if (binding.edtTravelExpenses.text.toString().trim().isEmpty()) {
            binding.edtTravelExpenses.error = "Required Field!"
            binding.edtTravelExpenses.requestFocus()
            return false
        } else {
            binding.edtTravelExpenses.error = null
        }
        return true
    }

//    /**
//     * field must not be empty
//     */
//    private fun validateReturnFromLocation(): Boolean {
//        if (binding.edtReturnFromLoc.text.toString().trim().isEmpty()) {
//            binding.edtReturnFromLoc.error = "Required Field!"
//            binding.edtReturnFromLoc.requestFocus()
//            return false
//        } else {
//            binding.edtReturnFromLoc.error = null
//        }
//        return true
//    }

//    private fun validateReturnAttachment(): Boolean {
//        if (binding.tvReturnFileName.text.toString().trim().isEmpty()) {
//            binding.tvReturnFileName.error = "Required Field!"
//            binding.tvReturnFileName.requestFocus()
//            return false
//        } else {
//            binding.tvReturnFileName.error = null
//        }
//        return true
//    }

//    private fun validateReturnModeOfTravelLocation(): Boolean {
//        if (selectReturnMOTPos == 0) {
//            binding.edtReturnModeOfTravel.error = "Required Field!"
//            binding.edtReturnModeOfTravel.requestFocus()
//            return false
//        } else {
//            binding.edtReturnModeOfTravel.error = null
//        }
//        return true
//    }

    /**
     * field must not be empty
     */
//    private fun validateReturnToLocation(): Boolean {
//        if (binding.edtReturnToLoc.text.toString().trim().isEmpty()) {
//            binding.edtReturnToLoc.error = "Required Field!"
//            binding.edtReturnToLoc.requestFocus()
//            return false
//        } else {
//            binding.edtReturnToLoc.error = null
//        }
//        return true
//    }

    /**
     * field must not be empty
     */
    private fun validateReturnTravelDate(): Boolean {
        if (binding.tvReturnTravelDate.text.toString().trim().isEmpty()) {
            binding.tvReturnTravelDate.error = "Required Field!"
            binding.tvReturnTravelDate.requestFocus()
            return false
        } else {
            binding.tvReturnTravelDate.error = null
        }
        return true
    }


    private val pdfLauncher: ActivityResultLauncher<Intent> =
        if (activity != null) {
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result?.data?.data != null) {
                    result.data?.data?.let {
                        viewModel.file =
                            requireContext().contentResolver.openInputStream(it)?.readBytes()
                    }
                }
            }
        } else {
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result?.data?.data != null) {
                    result.data?.data?.let {
                        viewModel.file =
                            requireContext().contentResolver.openInputStream(it)?.readBytes()
                    }
                }
            }
        }

    companion object {
        private const val REQUEST_TAKE_PHOTO = 1
        private const val REQUEST_OPEN_GALLERY = 2
        private const val PERMISSION_REQUEST_CODE = 100
    }

}