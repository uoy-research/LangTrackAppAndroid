package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnHeaderInteractionListener {
    fun participantClickedStart(currentQuestion: Question)
    fun participantClickedCancel(currentQuestion: Question)
}