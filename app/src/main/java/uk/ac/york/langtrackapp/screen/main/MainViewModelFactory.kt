package uk.ac.york.langtrackapp.screen.main

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.york.langtrackapp.data.RepositoryFactory

class MainViewModelFactory(var context: Context): ViewModelProvider.Factory {

    val mMainViewModel =  MainViewModel(RepositoryFactory.getRepository(context))

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(RepositoryFactory.getRepository(context)) as T
}