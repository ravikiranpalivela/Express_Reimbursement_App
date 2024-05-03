package com.tekskills.er_tekskills.utils

import android.content.Context
import android.graphics.Typeface
import com.tekskills.er_tekskills.R

class SmartDialogBuilder(context: Context) {
    private var titleTf: Typeface? = null
    private var subTitleTf: Typeface? = null
    private var isCancelable = false
    private var backgroundColor = R.color.white
    private var title: String? = null
    private var subtitle: String? = null
    private var okButtonLable: String? = null
    private var cancelButtonLable: String? = null
    private var neutralButtonLabel: String? = null
    private val context: Context
    private var okListener: SmartDialogClickListener? = null
    private var cancelListener: SmartDialogClickListener? = null
    private var nutralBtnListener: SmartDialogClickListener? = null
    private var isNegativeBtnHide = false
    private var hasNeutralBtn = false
    private var customIcon = 0
    private var headerTextColor = 0
    private var descriptionTextColor = 0

    init {
        this.context = context
    }

    fun setBackgroundColor(backgroundColor: Int): SmartDialogBuilder {
        this.backgroundColor = backgroundColor
        return this
    }

    fun setTitleColor(headerTextColor: Int): SmartDialogBuilder {
        this.headerTextColor = headerTextColor
        return this
    }

    fun setSubTitleColor(descriptionTextColor: Int): SmartDialogBuilder {
        this.descriptionTextColor = descriptionTextColor
        return this
    }

    fun setTitle(title: String?): SmartDialogBuilder {
        this.title = title
        return this
    }

    fun setCustomIcon(icon: Int): SmartDialogBuilder {
        customIcon = icon
        return this
    }

    fun setSubTitle(subTitle: String?): SmartDialogBuilder {
        subtitle = subTitle
        return this
    }

    fun setTitleFont(titleFont: Typeface?): SmartDialogBuilder {
        titleTf = titleFont
        return this
    }

    fun setSubTitleFont(subTFont: Typeface?): SmartDialogBuilder {
        subTitleTf = subTFont
        return this
    }

    fun setPositiveButton(lable: String?, listener: SmartDialogClickListener?): SmartDialogBuilder {
        okListener = listener
        okButtonLable = lable
        return this
    }

    fun setNegativeButton(lable: String?, listener: SmartDialogClickListener?): SmartDialogBuilder {
        cancelListener = listener
        cancelButtonLable = lable
        return this
    }

    fun setNeutralButton(label: String?, listener: SmartDialogClickListener?): SmartDialogBuilder {
        nutralBtnListener = listener
        neutralButtonLabel = label
        return this
    }

    fun setCancalable(isCancelable: Boolean): SmartDialogBuilder {
        this.isCancelable = isCancelable
        return this
    }

    fun setNegativeButtonHide(isHide: Boolean): SmartDialogBuilder {
        isNegativeBtnHide = isHide
        return this
    }

    fun useNeutralButton(use: Boolean): SmartDialogBuilder {
        hasNeutralBtn = use
        return this
    }

    fun build(): SmartDialog {
        val dialog = SmartDialog(
            context,
            backgroundColor,
            title,
            subtitle,
            titleTf,
            subTitleTf,
            isCancelable,
            isNegativeBtnHide,
            hasNeutralBtn,
            customIcon,
            headerTextColor,
            descriptionTextColor
        )
        dialog.setNegative(cancelButtonLable!!, cancelListener)
        dialog.setPositive(okButtonLable!!, okListener)
        dialog.setNeutral(neutralButtonLabel!!, nutralBtnListener)
        return dialog
    }
}