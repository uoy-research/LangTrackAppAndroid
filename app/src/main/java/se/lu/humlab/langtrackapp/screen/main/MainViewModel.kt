package se.lu.humlab.langtrackapp.screen.main

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import se.lu.humlab.langtrackapp.data.Repository
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.data.model.User

class MainViewModel(repo: Repository): ViewModel() {

    var mRepository: Repository = repo
    var surveyList = mutableListOf<Survey>()
    var surveyListLiveData = MutableLiveData<MutableList<Survey>>()

    init {
        mRepository.surveyListLiveData.observeForever {
            surveyList = it
            surveyListLiveData.value = surveyList
        }
    }


    fun getUserLiveData(): MutableLiveData<User> {
        return mRepository.currentUserLiveData
    }

    fun setCurrentUser(user: User){
        mRepository.setCurrentUser(user)
    }

    fun getCurrentUser() : User{
        return mRepository.getCurrentUser()
    }

    fun getSurveys(){
        mRepository.getSurveysFromDropbox()
    }
}