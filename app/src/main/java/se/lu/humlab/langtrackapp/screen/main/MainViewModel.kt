package se.lu.humlab.langtrackapp.screen.main

import androidx.lifecycle.ViewModel
import se.lu.humlab.langtrackapp.data.Repository

class MainViewModel(repo: Repository): ViewModel() {

    var mRepository: Repository = repo

}