package se.lu.humlab.langtrackapp.screen.survey

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.lu.humlab.langtrackapp.data.RepositoryFactory

class SurveyViewModelFactory(var context: Context): ViewModelProvider.Factory {

    private val mMainViewModel =  SurveyViewModel(RepositoryFactory.getRepository(context))

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = mMainViewModel as T
}