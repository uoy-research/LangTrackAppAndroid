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
import se.lu.humlab.langtrackapp.data.model.User

class MainViewModel(repo: Repository): ViewModel() {

    var mRepository: Repository = repo


    fun getUserLiveData(): MutableLiveData<User> {
        return mRepository.currentUserLiveData
    }

    fun setCurrentUser(user: User){
        mRepository.setCurrentUser(user)
    }
}