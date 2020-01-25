package se.lu.humlab.langtrackapp.interfaces

import android.widget.EditText
import se.lu.humlab.langtrackapp.data.model.Question

interface OnOpenEndedTextItemClickedListener {
    fun goToNextItem()
    fun goToPrevoiusItem()
    fun hideTheKeyboard(editText: EditText)
}