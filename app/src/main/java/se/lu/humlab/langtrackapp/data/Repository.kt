package se.lu.humlab.langtrackapp.data

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import se.lu.humlab.langtrackapp.data.model.*
import java.lang.Exception


class Repository(val context: Context) {



    var surveyList = mutableListOf<Survey>()
    var surveyListLiveData = MutableLiveData<MutableList<Survey>>()

    private var currentUser = User()
    var currentUserLiveData = MutableLiveData<User>()
    var selectedAssignment: Assignment? = null
    var idToken = ""
    var assignmentList = mutableListOf<Assignment>()
    var assignmentListLiveData = MutableLiveData<MutableList<Assignment>>()
    private val dropboxUrl = "https://www.dropbox.com/s/n2l1vssqm2pfaqp/survey_json.txt?dl=1"
    private val mockUrl = "https://e3777de6-509b-46a9-a996-ea2708cc0192.mock.pstmn.io/"


    fun setCurrentUser(user: User){
        currentUser = user
        currentUserLiveData.postValue(currentUser)
    }
    fun getCurrentUser(): User{
        return currentUser
    }

    fun getAssignments(){
        val assigmnentUrl = "${mockUrl}user/u123/assignments"
        Fuel.get(assigmnentUrl)
            .header(mapOf("token" to idToken))
            .response { request, response, result ->
                println(request)
                println(response)
                val (bytes, error) = result
                if (error == null) {
                    if (bytes != null) {
                        val gson = Gson()
                        val itemType = object : TypeToken<List<Assignment>>() {}.type
                        val itemList = gson.fromJson<List<Assignment>>(String(bytes), itemType)

                        println("")
                    }
                }else{
                    println("Repository getAssignmens ERROR: ${error.localizedMessage}")
                }
            }
    }

    //***************'gammalt!**************

    fun getSurveysFromDropbox(){
        Fuel.get(dropboxUrl)
            .header(mapOf("token" to "nil"))// skicka med firebase token
            .response { request, response, result ->
                println(request)
                println(response)
                val (bytes, error) = result
                if (error == null) {
                    if (bytes != null) {
                        surveyList = convertJsonToSurveyList(String(bytes)).toMutableList()
                        surveyListLiveData.value = surveyList
                    }
                }else{
                    println("Repository getSurveysFromDropbox ERROR: ${error.localizedMessage}")
                }
            }
    }

    private fun convertJsonToSurveyList(jsonString: String): List<Survey>{
        //val gson = Gson()
        //val listType = object : TypeToken<List<Survey>>() { }.type
        //return gson.fromJson(jsonString, listType))

        return setOrder(setSurveyObjectsManually(jsonString))
    }


    private fun tryGetBoolean(id: String, item: JSONObject, aDefault: Boolean = false
    ) : Boolean {

        var result: Boolean = aDefault

        try {
            result = item.get(id) as? Boolean ?: aDefault
        } catch (e: Exception) {
            println("e: ${e.localizedMessage}")
        }

        return result//optBoolean
    }

    private fun setSurveyObjectsManually(json: String): List<Survey>{

        val theListWithSurveys = mutableListOf<Survey>()
        val jsonObj = JSONArray(json.substring(json.indexOf("["), json.lastIndexOf("]") + 1))

        for (i in 0 until jsonObj.length()) {
            val item = jsonObj.getJSONObject(i)
            val tempSurvey = Survey()

            tempSurvey.active = tryGetBoolean("active", item)

            try {
                tempSurvey.title = item.get("title") as? String ?: ""
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}

            try {
                tempSurvey.id = item.get("id") as? String ?: ""
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}

            try {
                tempSurvey.date = item.get("date") as? Long ?: 0
            }catch (e: Exception){
                try {
                    tempSurvey.date = (item.get("date") as? Int ?: 0).toLong()
                }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            }

            try {
                tempSurvey.respondeddate = item.get("respondeddate") as? Long ?: 0
            }catch (e: Exception){ try {
                tempSurvey.respondeddate = (item.get("respondeddate") as? Int ?: 0).toLong()
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}}

            try {
                tempSurvey.expiry = item.get("expiry") as? Long ?: 0
            }catch (e: Exception){
                try {
                    tempSurvey.expiry = (item.get("expiry") as? Int ?: 0).toLong()
                }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            }

            try {
                tempSurvey.published = item.get("published") as? Long ?: 0
            }catch (e: Exception){
                try {
                    tempSurvey.published = (item.get("published") as? Int ?: 0).toLong()
                }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            }

            try {
                val answer = item.getJSONObject("answer")
                tempSurvey.answer = getAnswer(answer).toMutableList()
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}

            try {
                val questions = item.getJSONArray("questions")
                val thequestions = mutableListOf<Question>()
                for (q in 0 until questions.length()){
                    val tempQuestion = Question()
                    val question = questions.getJSONObject(q)

                    try {
                        tempQuestion.type = question.get("type") as? String ?: ""
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    try {
                        tempQuestion.index = question.get("index") as? Int ?: 0
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    try {
                        tempQuestion.text = question.get("text") as? String ?: ""
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    try {
                        tempQuestion.title = question.get("title") as? String ?: ""
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    try {
                        tempQuestion.description = question.get("description") as? String ?: ""
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    try {
                        tempQuestion.id = question.get("id") as? String ?: ""
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    try {
                        val tempskip = question.getJSONObject("skip")
                        val theSkip = SkipLogic()
                        for (s in tempskip.keys()){
                            theSkip.ifChosen = tempskip.getInt("ifChosen")
                            theSkip.goto = tempskip.getInt("goto")
                        }
                        tempQuestion.skip = theSkip
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    try {
                        val tempsingleMultipleAnswers = question.getJSONArray("singleMultipleAnswers")
                        val thesingleMultipleAnswers = mutableListOf<String>()
                        for (singleMultipleAnswerIndex in 0 until tempsingleMultipleAnswers.length()){
                            thesingleMultipleAnswers.add(tempsingleMultipleAnswers.getString(singleMultipleAnswerIndex))
                        }
                        tempQuestion.singleMultipleAnswers = thesingleMultipleAnswers
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    try {
                        val tempfillBlanksChoises = question.getJSONArray("fillBlanksChoises")
                        val thefillBlanks = mutableListOf<String>()
                        for (fillBlanksIndex in 0 until tempfillBlanksChoises.length()){
                            thefillBlanks.add(tempfillBlanksChoises.getString(fillBlanksIndex))
                        }
                        tempQuestion.fillBlanksChoises = thefillBlanks
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    try {
                        val tempmultipleChoisesAnswers = question.getJSONArray("multipleChoisesAnswers")
                        val multipleChoisesAnswers = mutableListOf<String>()
                        for (multipleChoisesAnswersIndex in 0 until tempmultipleChoisesAnswers.length()){
                            multipleChoisesAnswers.add(tempmultipleChoisesAnswers.getString(multipleChoisesAnswersIndex))
                        }
                        tempQuestion.multipleChoisesAnswers = multipleChoisesAnswers
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}

                    thequestions.add(tempQuestion)
                }
                tempSurvey.questions = thequestions
                println("")
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}

            theListWithSurveys.add(tempSurvey)
        }
        return theListWithSurveys
    }

    private fun getAnswer(jObj: JSONObject): List<Answer>{

        val tempListWithAnswers = mutableListOf<Answer>()
        for (a in jObj.keys()) {
            val theObject = jObj.getJSONObject(a)
            val tempAnswer = Answer()
            tempAnswer.index = a.toInt()
            try {
                tempAnswer.likertAnswer = theObject.get("likertAnswer") as? Int
            }catch (e: Exception){ println("getAnswer, e: ${e.localizedMessage}")}
            try {
                tempAnswer.fillBlankAnswer = theObject.get("fillBlankAnswer") as? Int
            }catch (e: Exception){ println("getAnswer, e: ${e.localizedMessage}")}
            try {
                val tempMultipleObj = theObject.get("multipleChoiceAnswer") as? JSONArray
                if (tempMultipleObj != null) {
                    val tempMultipleArray = mutableListOf<Int>()
                    for (m in 0 until tempMultipleObj.length()) {
                        tempMultipleArray.add(tempMultipleObj.getInt(m))
                    }
                    tempAnswer.multipleChoiceAnswer = tempMultipleArray
                }
            }catch (e: Exception){ println("getAnswer, e: ${e.localizedMessage}")}
            try {
                tempAnswer.singleMultipleAnswer = theObject.get("singleMultipleAnswer") as? Int
            }catch (e: Exception){ println("getAnswer, e: ${e.localizedMessage}")}
            try {
                tempAnswer.openEndedAnswer = theObject.get("OpenEndedAnswer") as? String
            }catch (e: Exception){ println("getAnswer, e: ${e.localizedMessage}")}
            tempListWithAnswers.add(tempAnswer)
        }

        return tempListWithAnswers
    }



    private fun setOrder(inList: List<Survey>): List<Survey>{
        val sortedList = inList.sortedBy { it.date }
        for (survey in sortedList){
            if (survey.questions != null) {
                val sortedQuestionList = survey.questions!!.sortedBy { it.index }
                for ((index, question) in sortedQuestionList.withIndex()) {
                    when {
                        index == sortedQuestionList.last().index -> {
                            //last question
                            question.next = -1
                            question.previous = index - 1
                        }
                        question.index == 0 -> {
                            //first question
                            question.previous = 0
                            question.next = 1
                        }
                        else -> {
                            //every other question
                            question.next = question.index + 1
                            question.previous = question.index - 1
                        }
                    }
                }
            }
        }
        return sortedList
    }

}