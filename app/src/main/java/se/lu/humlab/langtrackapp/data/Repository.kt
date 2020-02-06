package se.lu.humlab.langtrackapp.data

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.content.Context
import androidx.lifecycle.MutableLiveData
import se.lu.humlab.langtrackapp.data.model.User

class Repository(val context: Context) {

    private var currentUser = User()
    var currentUserLiveData = MutableLiveData<User>()


    fun setCurrentUser(user: User){
        currentUser = user
        currentUserLiveData.postValue(currentUser)
    }
}