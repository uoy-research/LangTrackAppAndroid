package uk.ac.york.langtrackapp.interfaces

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se

* Viktor Czyżewski
* RSE Team
* University of York
* */

import uk.ac.york.langtrackapp.data.model.Assignment

interface OnSurveyRowClickedListener {
    fun rowClicked(item: Assignment)
}