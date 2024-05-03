package com.tekskills.er_tekskills.utils


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tekskills.er_tekskills.R

class SmartDialog(
    context: Context?,
    backgroundColor: Int,
    title: String?,
    subTitle: String?,
    titleFont: Typeface?,
    subtitleFont: Typeface?,
    isCancelable: Boolean,
    private val isNegativeBtnHide: Boolean,
    private val hasNeutralBtn: Boolean,
    icon: Int,
    headerTextColor: Int,
    descriptionTextColor: Int
) {
    private val dialog: Dialog
    private var container: LinearLayout? = null
    private var titleTV: TextView? = null
    private var subTitleTv: TextView? = null
    private var tvOK: TextView? = null
    private var tvNeutral: TextView? = null
    private var tvCancel: TextView? = null
    private var viewIcon: ImageView? = null
    private var okButtonTv: TextView? = null
    private var cancelButtonTV: TextView? = null
    private var neutralButtonTv: TextView? = null
    private var okButtonClickListener: SmartDialogClickListener? = null
    private var cancelButtonClickListener: SmartDialogClickListener? = null
    private var neutralButtonClickListener: SmartDialogClickListener? = null

    init {
        dialog = Dialog(context!!)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCancelable(isCancelable)
        if (dialog.window != null) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initDialogViw()
        setBackgroundColor(context, backgroundColor)
        setTitle(title)
        setSubtitle(subTitle)
        setSubTitleFont(subtitleFont)
        setTitleFont(titleFont)
        initClickEvents()
        setIcon(icon)
        setHeaderTextColor(headerTextColor)
        setDescriptionTextColor(descriptionTextColor)
    }

    private fun setHeaderTextColor(headerTextColor: Int) {
        titleTV!!.setTextColor(headerTextColor)
    }

    private fun setDescriptionTextColor(descriptionTextColor: Int) {
        subTitleTv!!.setTextColor(descriptionTextColor)
    }

    fun setPositive(okLabel: String, listener: SmartDialogClickListener?) {
        okButtonClickListener = listener
        dismiss()
        setPositiveLabel(okLabel)
    }

    fun setNegative(cancelLabel: String, listener: SmartDialogClickListener?) {
        if (listener != null) {
            cancelButtonClickListener = listener
            dismiss()
            setNegativeLabel(cancelLabel)
        }
    }

    fun setNeutral(neutralBtnLabel: String, listener: SmartDialogClickListener?) {
        if (listener != null) {
            neutralButtonClickListener = listener
            dismiss()
            setNeutralBtnLabel(neutralBtnLabel)
        }
    }

    fun show() {
        if (isNegativeBtnHide) {
            cancelButtonTV!!.visibility = View.GONE
        }
        if (hasNeutralBtn) {
            neutralButtonTv!!.visibility = View.GONE
        }
        dialog.show()
    }

    fun setBackgroundColor(context: Context?, color: Int) {
        val background = container!!.background
        val shapeDrawable = background as GradientDrawable
        shapeDrawable.setColor(ContextCompat.getColor(context!!, color))
    }

    fun setTitle(title: String?) {
        titleTV!!.text = title
    }

    fun setIcon(drawable: Int) {
        viewIcon!!.setImageResource(drawable)
    }

    fun setSubtitle(subtitle: String?) {
        subTitleTv!!.text = subtitle
    }

    private fun setPositiveLabel(positive: String) {
        tvOK!!.text = positive
    }

    private fun setNegativeLabel(negative: String) {
        tvCancel!!.text = negative
    }

    private fun setNeutralBtnLabel(neutralBtnLabel: String) {
        tvNeutral!!.text = neutralBtnLabel
    }

    private fun setSubTitleFont(appleFont: Typeface?) {
        if (appleFont != null) {
            subTitleTv!!.typeface = appleFont
            tvOK!!.typeface = appleFont
            tvNeutral!!.typeface = appleFont
            tvCancel!!.typeface = appleFont
        }
    }

    private fun setTitleFont(appleFont: Typeface?) {
        if (appleFont != null) {
            titleTV!!.typeface = appleFont
        }
    }

    fun dismiss() {
        dialog.dismiss()
    }

    //positive and negative button initialise here
    private fun initClickEvents() {
        okButtonTv!!.setOnClickListener {
            if (okButtonClickListener != null) {
                okButtonClickListener!!.onClick(this@SmartDialog)
            }
        }
        cancelButtonTV!!.setOnClickListener {
            if (cancelButtonClickListener != null) {
                cancelButtonClickListener!!.onClick(this@SmartDialog)
            }
        }
        neutralButtonTv!!.setOnClickListener {
            if (neutralButtonClickListener != null) {
                neutralButtonClickListener!!.onClick(this@SmartDialog)
            }
        }
    }

    //init all view here
    private fun initDialogViw() {
        container = dialog.findViewById<LinearLayout>(R.id.container)
        titleTV = dialog.findViewById<TextView>(R.id.tv1)
        viewIcon = dialog.findViewById<ImageView>(R.id.viewIcon)
        subTitleTv = dialog.findViewById<TextView>(R.id.tv2)
        okButtonTv = dialog.findViewById<TextView>(R.id.tvok)
        cancelButtonTV = dialog.findViewById<TextView>(R.id.tvCan)
        neutralButtonTv = dialog.findViewById<TextView>(R.id.tvNeutral)
        tvOK = dialog.findViewById<TextView>(R.id.tvok)
        tvNeutral = dialog.findViewById<TextView>(R.id.tvNeutral)
        tvCancel = dialog.findViewById<TextView>(R.id.tvCan)
    }
}

interface SmartDialogClickListener {
    fun onClick(smartDialog: SmartDialog?)
}