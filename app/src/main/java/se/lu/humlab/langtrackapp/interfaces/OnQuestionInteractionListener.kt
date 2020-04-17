package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnQuestionInteractionListener {
    fun nextQuestion(current: Question)
    //fun goToNextItemWithSkipLogic(currentQuestion: Question)
    fun prevoiusQuestion(current: Question)
    fun closeSurvey()
    fun sendInSurvey()
    fun setSingleMultipleAnswer(selected: Int)
    fun setMultipleAnswersAnswer(selected: List<Int>?)
    fun setLikertAnswer(selected: Int)
    fun setOpenEndedAnswer(text: String?)
    fun setFillBlankAnswer(selected: Int?)
    fun setTimeDurationAnswer(selected: Int)
}