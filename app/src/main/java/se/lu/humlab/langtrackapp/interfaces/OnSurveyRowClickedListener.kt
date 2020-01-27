package se.lu.humlab.langtrackapp.interfaces

import se.lu.humlab.langtrackapp.data.model.Survey

interface OnSurveyRowClickedListener {
    fun rowClicked(item: Survey)
}