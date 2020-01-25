package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnSingleMultipleInteractionListener {
    fun singleMultipleGoToNextItem(currentQuestion: Question)
    fun singleMultipleGoToPrevoiusItem(currentQuestion: Question)
}