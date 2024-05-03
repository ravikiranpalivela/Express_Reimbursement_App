package com.tekskills.er_tekskills.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.ClientNamesResponseItem
import com.tekskills.er_tekskills.data.model.ManagementResponse
import com.tekskills.er_tekskills.data.model.ProjectListResponseItem
import com.tekskills.er_tekskills.data.model.SRManagementResponseItem
import com.tekskills.er_tekskills.databinding.FragmentNewAssignProjectBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.view.multi_spinner.KeyPairBoolData
import com.tekskills.er_tekskills.presentation.view.multi_spinner.MultiSpinnerListener
import com.tekskills.er_tekskills.presentation.view.spinner.SearchableSpinner
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.RestApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AssignProjectFragment : ParentFragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentNewAssignProjectBinding

    private var mProjectNames = ArrayList<ProjectListResponseItem>()
    private var selectProjectPos = 0

    private var mClientNames = ArrayList<ClientNamesResponseItem>()
    private var selectClientPos = 0

    private var mOpportunityType = ArrayList<String>()
    private var selectOpportunityTypePos = ""

    private var mManagementNames = ArrayList<SRManagementResponseItem>()

    //    private var selectManagementPos = 0
    private var selectManagementsPos = ArrayList<Int>()

    private var mProjectManagerNames = ArrayList<SRManagementResponseItem>()
    private var selectProjectManagerPos = 0

    private var mPracticeHeadNames = ArrayList<SRManagementResponseItem>()
    private var selectPracticeHeadPos = 0

    private var mAccountManagerNames = ArrayList<SRManagementResponseItem>()
    private var selectAccountManagerPos = 0

    val listArray1: ArrayList<KeyPairBoolData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_assign_project, container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

//        viewModel.getProjectList()
        viewModel.getManagementList("")
        viewModel.getAccountManagerList("")
        viewModel.getProjectManagerList("")
        viewModel.getPracticeHeadList("")
        viewModel.getClientNameList()

        mOpportunityType =
            arrayListOf(
                "SAP",
                "Application Developer",
                "Cyber Security",
                "Data Engineering",
                "Other"
            )
//        binding.sClients.buildCheckedSpinner(spinnerData) { selectedPositionList, displayString ->
////            tvSelectedPosition.text = "Selected position:  $selectedPositionList"
////            tvDispString.text = "Display String:  $displayString"
//        }


        binding.sOpportunityType.setItems(mOpportunityType)
        binding.sOpportunityType.setOnItemSelectListener(object :
            SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectOpportunityTypePos = mOpportunityType[position]
            }

            override fun onSelectionClear() {

            }
        })

        binding.sClients.setOnItemSelectListener(object : SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectClientPos = mClientNames[position].clientId
            }

            override fun onSelectionClear() {

            }
        })


//        binding.sMultiManagement.isShowSelectAllButton =

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.sMultiManagement.isSearchEnabled = true;

        binding.sMultiManagement.hintText = "Select Management";

        //A text that will display in clear text button
        binding.sMultiManagement.setClearText("Close & Clear");

        // A text that will display in search hint.
        binding.sMultiManagement.setSearchHint("Select Management");

        // Set text that will display when search result not found...
        binding.sMultiManagement.setEmptyTitle("Not Data Found!");

        // If you will set the limit, this button will not display automatically.
        binding.sMultiManagement.isShowSelectAllButton = true;

        binding.sMultiManagement.setItems(listArray1, object : MultiSpinnerListener {
            override fun onItemsSelected(selectedItems: List<KeyPairBoolData?>?) {
                selectManagementsPos.clear()
                selectedItems!!.filter {
                    it!!.isSelected
                }
                Log.i(
                    "TAG",
                    "selected Item position : " + selectManagementsPos.joinToString(separator = ",")
                )
                for (i in selectedItems.indices) {
//                    selectManagementPos = selectedItems[i]!!.id.toInt()
                    selectManagementsPos.add(selectedItems[i]!!.id.toInt())
                    Log.i(
                        "TAG",
                        "${selectedItems[i]!!.id} : " + selectManagementsPos.joinToString(separator = ",") + selectManagementsPos.toString() + selectedItems[i]!!.name + " : " + selectedItems[i]!!.isSelected
                    )
                }
            }
        })

        viewModel.resClientNameList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            mClientNames = res
                            binding.sClients.setItems(res)
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

//        binding.sProjects.setOnItemSelectListener(object :
//            SearchableSpinner.SearchableItemListener {
//            override fun onItemSelected(view: View?, position: Int) {
//                selectProjectPos = mProjectNames[position].projectId
//            }
//
//            override fun onSelectionClear() {
//
//            }
//        })

        binding.sProjectManager.setOnItemSelectListener(object :
            SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectProjectManagerPos = mProjectManagerNames[position].srManagerId
            }

            override fun onSelectionClear() {

            }
        })

        binding.sAccountHead.setOnItemSelectListener(object :
            SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectAccountManagerPos = mAccountManagerNames[position].srManagerId
            }

            override fun onSelectionClear() {

            }
        })

        binding.sProcticeHead.setOnItemSelectListener(object :
            SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectPracticeHeadPos = mPracticeHeadNames[position].srManagerId
            }

            override fun onSelectionClear() {

            }
        })

        binding.sManagement.setOnItemSelectListener(object :
            SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectManagementsPos.clear()
                selectManagementsPos.add(mManagementNames[position].srManagerId)
            }

            override fun onSelectionClear() {

            }
        })

        viewModel.resAddOpportunity.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            val action =
                                AssignProjectFragmentDirections.actionNewAssignProjectFragmentToViewOpportunityFragment()
                            binding.root.findNavController().navigate(action)
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


//        viewModel.resProjectList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            when (it.status) {
//                RestApiStatus.SUCCESS -> {
//                    binding.progress.visibility = View.GONE
//                    if (it.data != null) {
//                        it.data.let { res ->
//                            mProjectNames = res
//                            binding.sProjects.setItems(res)
//                        }
//                    } else {
//                        Snackbar.make(
//                            binding.root,
//                            "Projects null",
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

        viewModel.resManagementList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { res ->
                            setManagerListItems(res)
                            mManagementNames = res
                            binding.sManagement.setItems(res)
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Projects null",
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

        viewModel.resAccountHeadList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { res ->
                            mAccountManagerNames = res
                            binding.sAccountHead.setItems(res)
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Projects null",
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

        viewModel.resProjectManagerList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { res ->
                            mProjectManagerNames = res
                            binding.sProjectManager.setItems(res)
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Projects null",
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

        viewModel.resPracticeHeadList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { res ->
                            mPracticeHeadNames = res
                            binding.sProcticeHead.setItems(res)
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Projects null",
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

        binding.apply {
            fab.setOnClickListener {
                if (isValidate()) {
                    viewModel.addOpportunity(
                        selectManagementsPos.joinToString(separator = ","),
                        selectAccountManagerPos.toString(),
                        selectPracticeHeadPos.toString(),
                        selectProjectManagerPos.toString(),
                        binding.edtProjectName.text.toString(),
                        selectClientPos.toString(),
                        selectOpportunityTypePos.toString(),
                        binding.edtOpportunityDesc.text.toString(),
                        "Active"
                    )
                    Toast.makeText(requireActivity(), "Added", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setManagerListItems(res: ManagementResponse) {
        listArray1.clear()

        for (i in 0 until res.size) {
            val h = KeyPairBoolData()
            h.id = res[i].srManagerId.toLong()
            h.name = res[i].srManagerName
            h.`object` = res[i]
            h.isSelected = false
            listArray1.add(h)
        }
    }

    private fun isValidate(): Boolean =
        validateProjectName() && validateManagement() && validateAccountManagement() && validateProjectManagerName() && validatePracticeHead() && validateOpportunityType() && validateOpportunityDesc() && validateClientName()

    /**
     * field must not be empty
     */
    private fun validateProjectName(): Boolean {
        if (binding.edtProjectName.text.toString().trim().isEmpty()) {
            binding.edtlProjectName.error = "Required Field!"
            binding.edtProjectName.requestFocus()
            return false
        } else {
            binding.edtlProjectName.isErrorEnabled = false
        }
        return true
    }

    private fun validateClientName(): Boolean {
        return selectClientPos != 0
    }

    /**
     * field must not be empty
     */
    private fun validateOpportunityType(): Boolean {
        return !selectOpportunityTypePos.isNullOrEmpty()
    }

    /**
     * field must not be empty
     */
    private fun validateOpportunityDesc(): Boolean {
        if (binding.edtOpportunityDesc.text.toString().trim().isEmpty()) {
            binding.edtlOpportunityDesc.error = "Required Field!"
            binding.edtOpportunityDesc.requestFocus()
            return false
        } else {
            binding.edtlOpportunityDesc.isErrorEnabled = false
        }
        return true
    }

    private fun validateManagement(): Boolean {
        return selectManagementsPos.size != 0
    }

    private fun validateAccountManagement(): Boolean {
        return selectAccountManagerPos != 0
    }

    private fun validateProjectManagerName(): Boolean {
        return selectProjectManagerPos != 0
    }

    private fun validatePracticeHead(): Boolean {
        return selectPracticeHeadPos != 0
    }
}