package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnHeaderInteractionListener {
    fun headerGoToNextItem(currentQuestion: Question)
    fun headerCancelPressed(currentQuestion: Question)
}