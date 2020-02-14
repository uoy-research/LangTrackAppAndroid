package se.lu.humlab.langtrackapp.data

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import se.lu.humlab.langtrackapp.data.model.Survey
import se.lu.humlab.langtrackapp.data.model.User

class Repository(val context: Context) {

    private var currentUser = User()
    var currentUserLiveData = MutableLiveData<User>()
    var surveyList = mutableListOf<Survey>()
    var surveyListLiveData = MutableLiveData<MutableList<Survey>>()
    private val theUrl = "https://www.dropbox.com/s/kulz0i0gink3ad8/survey_json.txt?dl=1"


    fun setCurrentUser(user: User){
        currentUser = user
        currentUserLiveData.postValue(currentUser)
    }
    fun getCurrentUser(): User{
        return currentUser
    }

    fun getSurveysFromDropbox(){
        Fuel.get(theUrl)
            .header(mapOf("token" to "nil"))// skicka med firebase token
            .response { request, response, result ->
                println(request)
                println(response)
                val (bytes, error) = result
                if (error == null) {
                    if (bytes != null) {
                        println("[response bytes] ${String(bytes)}")
                        surveyList = convertJsonToSurveyList(String(bytes)).toMutableList()
                        surveyListLiveData.value = surveyList
                    }
                }else{
                    println("Repository getSurveysFromDropbox ERROR: ${error.localizedMessage}")
                }
            }
    }

    private fun convertJsonToSurveyList(jsonString: String): List<Survey>{
        val gson = Gson()
        val listType = object : TypeToken<List<Survey>>() { }.type
        return gson.fromJson(jsonString, listType)
    }
}