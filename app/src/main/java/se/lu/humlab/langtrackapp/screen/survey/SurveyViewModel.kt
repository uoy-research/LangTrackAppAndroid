package se.lu.humlab.langtrackapp.screen.survey

import androidx.lifecycle.ViewModel
import se.lu.humlab.langtrackapp.data.Repository

class SurveyViewModel (repo: Repository): ViewModel() {

    var mRepository: Repository = repo
}