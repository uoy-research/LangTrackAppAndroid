package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnMultipleChoiceItemClickedListener {
    fun goToNextItem()
    fun goToPrevoiusItem()
}