package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnMultipleChoiceInteractionListener {
    fun multipleChoiceGoToNextItem(currentQuestion: Question)
    fun multipleChoiceGoToPrevoiusItem(currentQuestion: Question)
}