package se.lu.humlab.langtrackapp.screen.about

import androidx.lifecycle.ViewModel
import se.lu.humlab.langtrackapp.data.ContactInfo
import se.lu.humlab.langtrackapp.data.Repository

class TeamViewModel(repo: Repository): ViewModel(){

    var mRepository: Repository = repo

    fun getContactInfo(callback: (result: List<ContactInfo>) -> Unit) {
        mRepository.getContactInfo(callback)
    }
}