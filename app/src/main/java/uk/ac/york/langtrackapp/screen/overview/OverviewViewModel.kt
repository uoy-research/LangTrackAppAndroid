package uk.ac.york.langtrackapp.screen.overview

import androidx.lifecycle.ViewModel
import uk.ac.york.langtrackapp.data.Repository

class OverviewViewModel(repo: Repository): ViewModel() {
    var mRepository: Repository = repo

}