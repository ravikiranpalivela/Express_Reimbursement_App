package com.tekskills.er_tekskills.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.UtilsConstants

class CheckoutFragment : Fragment() {
    private var mViewModel: MainActivityViewModel? = null
    private var transportationType: String? = null
    private var distanceInKmString: String? = null
    private var priceInVNDString: String? = null

    //View variables
    private var distanceBikeTextView: TextView? = null
    private var priceBikeTextView: TextView? = null
    private var bookBtn: Button? = null
    private var originTextView: TextView? = null
    private var destinationTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_customer_checkout, container, false)
        linkViewElements(view)
        setActionHandlers()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as MainActivity).viewModel
    }

    /**
     * Link view elements from xml file
     * @param rootView
     */
    private fun linkViewElements(rootView: View) {
        distanceBikeTextView = rootView.findViewById(R.id.distanceBikeTextView)
        priceBikeTextView = rootView.findViewById(R.id.priceBikeTextView)
        bookBtn = rootView.findViewById(R.id.bookBtn)
        originTextView = rootView.findViewById<TextView>(R.id.originTextView)
        destinationTextView = rootView.findViewById<TextView>(R.id.destinationTextView)
    }

    private fun setActionHandlers() {
        bookBtn!!.setOnClickListener {
            mViewModel!!.setBookBtnPressed(true)
            mViewModel!!.setCancelBookingBtnPressed(true)
        }
    }

    /**
     * Display booking information
     */
    private fun setCheckoutInfo() {
        distanceBikeTextView!!.text = distanceInKmString
        priceBikeTextView!!.text = priceInVNDString

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel!!.setDistanceInKmString(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Get customer currently chosen transportation type
        mViewModel!!.getTransportationType()!!.observe(viewLifecycleOwner, Observer<String?> { s ->
            if (s == null) return@Observer
            transportationType = s
        })
        mViewModel!!.getDistanceInKmString()!!.observe(viewLifecycleOwner, Observer<String?> { s ->
            if (s == null) return@Observer
            distanceInKmString = s
            setCheckoutInfo()
        })
        mViewModel!!.getPriceInVNDString()!!.observe(viewLifecycleOwner, Observer<String?> { s ->
            if (s == null) return@Observer
            priceInVNDString = s
        })
        mViewModel!!.getCustomerSelectedDropOffPlace().observe(
            viewLifecycleOwner
        ) { s -> destinationTextView!!.text = s!!.address?.toString() ?: "" }

        mViewModel!!.getCustomerSelectedPickupPlace().observe(
            viewLifecycleOwner
        ) { s -> originTextView!!.text = s!!.address?.toString() ?: "" }
    }

    companion object {
        fun newInstance(): CheckoutFragment {
            return CheckoutFragment()
        }
    }
}