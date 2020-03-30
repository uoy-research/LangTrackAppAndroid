package se.lu.humlab.langtrackapp.screen.surveyContainer

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import androidx.lifecycle.ViewModel
import se.lu.humlab.langtrackapp.data.Repository
import se.lu.humlab.langtrackapp.data.model.User

class SurveyContainerViewModel (private var repo: Repository): ViewModel() {

    fun getCurrentUser() : User {
        return repo.getCurrentUser()
    }
}