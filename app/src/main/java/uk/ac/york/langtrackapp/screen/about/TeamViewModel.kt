package uk.ac.york.langtrackapp.screen.about

import androidx.lifecycle.ViewModel
import uk.ac.york.langtrackapp.data.Repository
import uk.ac.york.langtrackapp.data.model.ContactInfo
import uk.ac.york.langtrackapp.data.model.TeamMember

class TeamViewModel(repo: Repository): ViewModel(){

    var mRepository: Repository = repo

    fun getContactInfo(callback: (result: List<ContactInfo>) -> Unit) {
        mRepository.getContactInfo(callback)
    }

    fun getTeamsText(callback: (result: List<TeamMember>) -> Unit) {
        mRepository.getTeamsText(callback)
    }
}