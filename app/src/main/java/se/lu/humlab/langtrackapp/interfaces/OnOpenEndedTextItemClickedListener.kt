package se.lu.humlab.langtrackapp.interfaces

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.widget.EditText
import se.lu.humlab.langtrackapp.data.model.Question

interface OnOpenEndedTextItemClickedListener {
    fun goToNextItem()
    fun goToPrevoiusItem()
    fun hideTheKeyboard(editText: EditText)
}