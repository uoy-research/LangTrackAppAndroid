package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnQuestionInteractionListener {
    fun goToNextItem(currentQuestion: Question)
    fun goToNextItemWithSkipLogic(currentQuestion: Question, nextIndex: Int)
    fun goToPrevoiusItem(currentQuestion: Question)
    fun sendInSurvey(currentQuestion: Question)
    fun cancelSurvey()
}