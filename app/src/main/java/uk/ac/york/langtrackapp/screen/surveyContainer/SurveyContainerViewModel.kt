package uk.ac.york.langtrackapp.screen.surveyContainer

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se

* Viktor Czyżewski
* RSE Team
* University of York
* */

import androidx.lifecycle.ViewModel
import uk.ac.york.langtrackapp.data.Repository
import uk.ac.york.langtrackapp.data.model.Answer
import uk.ac.york.langtrackapp.data.model.User

class SurveyContainerViewModel (private var repo: Repository): ViewModel() {

    fun getCurrentUser() : User {
        return repo.getCurrentUser()
    }

    fun postAnswer(answers: Map<Int, Answer>){
        repo.postAnswer(answers)
    }
}