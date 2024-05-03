package com.tekskills.er_tekskills.presentation.view.customviews.text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.tekskills.er_tekskills.R

class TextViewRegular : AppCompatTextView {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val myTypeface = ResourcesCompat.getFont(context, R.font.poppins)
            this.typeface = myTypeface
        }
    }

}