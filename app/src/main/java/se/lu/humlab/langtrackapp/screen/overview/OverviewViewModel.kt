package se.lu.humlab.langtrackapp.screen.overview

import androidx.lifecycle.ViewModel
import se.lu.humlab.langtrackapp.data.Repository

class OverviewViewModel(repo: Repository): ViewModel() {
    var mRepository: Repository = repo

}