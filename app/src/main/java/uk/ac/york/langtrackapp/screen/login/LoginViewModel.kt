package uk.ac.york.langtrackapp.screen.login

import androidx.lifecycle.ViewModel
import uk.ac.york.langtrackapp.data.Repository
import uk.ac.york.langtrackapp.data.model.User

class LoginViewModel(repo: Repository): ViewModel() {

    var mRepository: Repository = repo

    fun setCurrentUser(user: User){
        mRepository.setCurrentUser(user)
    }

    fun putDeviceToken(){
        mRepository.putDeviceToken()
    }
}