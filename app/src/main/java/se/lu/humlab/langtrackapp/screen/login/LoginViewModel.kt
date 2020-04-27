package se.lu.humlab.langtrackapp.screen.login

import androidx.lifecycle.ViewModel
import se.lu.humlab.langtrackapp.data.Repository
import se.lu.humlab.langtrackapp.data.model.User
import se.lu.humlab.langtrackapp.util.getVersionNumber

class LoginViewModel(repo: Repository): ViewModel() {

    var mRepository: Repository = repo

    fun setCurrentUser(user: User){
        mRepository.setCurrentUser(user)
    }

    fun putDeviceToken(){
        mRepository.putDeviceToken()
    }
}