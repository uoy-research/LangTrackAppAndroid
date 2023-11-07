package uk.ac.york.langtrackapp.interfaces

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import uk.ac.york.langtrackapp.data.model.Assignment

interface OnSurveyRowClickedListener {
    fun rowClicked(item: Assignment)
}