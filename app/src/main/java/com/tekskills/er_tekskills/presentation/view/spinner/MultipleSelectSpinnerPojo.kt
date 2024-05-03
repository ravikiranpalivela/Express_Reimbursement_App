package com.tekskills.er_tekskills.presentation.view.spinner

data class MultipleSelectSpinnerPojo(
    var text:String,
    var isSelected:Boolean,
    var id:String
)
{
    override fun toString(): String {
        return text
    }
}