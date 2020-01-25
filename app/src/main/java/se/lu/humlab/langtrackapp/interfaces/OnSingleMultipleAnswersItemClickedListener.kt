package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnSingleMultipleAnswersItemClickedListener {
    fun goToNextItem()
    fun goToPrevoiusItem()
}