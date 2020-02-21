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
            val activelist = mutableListOf<Survey>()
            val inactivelist = mutableListOf<Survey>()
            for (survey in it){
                if (survey.active){
                    activelist.add(survey)
                }else{
                    inactivelist.add(survey)
                }
            }
            val finallist = mutableListOf<Survey>()
            finallist.addAll(activelist)
            finallist.addAll(inactivelist)
            surveyList = finallist
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