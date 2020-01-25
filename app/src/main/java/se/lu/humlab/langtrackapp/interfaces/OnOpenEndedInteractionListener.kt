package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnOpenEndedInteractionListener {
    fun openEndedGoToNextItem(currentQuestion: Question)
    fun openEndedGoToPrevoiusItem(currentQuestion: Question)
}