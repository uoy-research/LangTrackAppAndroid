package se.lu.humlab.langtrackapp.screen.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.lu.humlab.langtrackapp.data.RepositoryFactory

class MainViewModelFactory(var context: Context): ViewModelProvider.Factory {

    val mMainViewModel =  MainViewModel(RepositoryFactory.getRepository(context))

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel(RepositoryFactory.getRepository(context)) as T
}