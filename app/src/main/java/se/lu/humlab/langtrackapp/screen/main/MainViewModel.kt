package se.lu.humlab.langtrackapp.screen.main

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import androidx.lifecycle.ViewModel
import se.lu.humlab.langtrackapp.data.Repository

class MainViewModel(repo: Repository): ViewModel() {

    var mRepository: Repository = repo

}