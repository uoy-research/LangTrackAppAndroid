package se.lu.humlab.langtrackapp.screen.about

import androidx.lifecycle.ViewModel
import se.lu.humlab.langtrackapp.data.Repository
import se.lu.humlab.langtrackapp.data.model.ContactInfo
import se.lu.humlab.langtrackapp.data.model.TeamMember

class TeamViewModel(repo: Repository): ViewModel(){

    var mRepository: Repository = repo

    fun getContactInfo(callback: (result: List<ContactInfo>) -> Unit) {
        mRepository.getContactInfo(callback)
    }

    fun getTeamsText(callback: (result: List<TeamMember>) -> Unit) {
        mRepository.getTeamsText(callback)
    }
}