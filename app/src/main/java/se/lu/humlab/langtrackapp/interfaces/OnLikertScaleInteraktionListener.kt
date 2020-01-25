package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Question

interface OnLikertScaleInteraktionListener {
    fun likertScaleGoToNextItem(currentQuestion: Question)
    fun likertScaleGoToPrevoiusItem(currentQuestion: Question)
}