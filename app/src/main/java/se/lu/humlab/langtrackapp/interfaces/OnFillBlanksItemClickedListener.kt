package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnFillBlanksItemClickedListener {
    fun goToNextItem()
    fun goToPrevoiusItem()
}