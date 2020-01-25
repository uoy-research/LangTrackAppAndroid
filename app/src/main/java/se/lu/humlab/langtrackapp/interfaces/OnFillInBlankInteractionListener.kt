package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnFillInBlankInteractionListener {
    fun fillInBlankGoToNextItem(currentQuestion: Question)
    fun fillInBlankGoToPrevoiusItem(currentQuestion: Question)
}