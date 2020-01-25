package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnFooterInteractionListener {
    fun footerGoToNextItem(currentQuestion: Question)
    fun footerSendInSurvey(currentQuestion: Question)
}