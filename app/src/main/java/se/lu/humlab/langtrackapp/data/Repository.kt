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
import org.json.JSONArray
import org.json.JSONObject
import se.lu.humlab.langtrackapp.data.model.*
import se.lu.humlab.langtrackapp.util.toDate
import java.lang.Exception
import java.util.*


class Repository(val context: Context) {



    var surveyList = mutableListOf<Survey>()
    var surveyListLiveData = MutableLiveData<MutableList<Survey>>()

    private var currentUser = User()
    var currentUserLiveData = MutableLiveData<User>()
    var selectedAssignment: Assignment? = null
    var idToken = ""
    var assignmentList = mutableListOf<Assignment>()
    var assignmentListLiveData = MutableLiveData<MutableList<Assignment>>()
    //private val dropboxUrl = "https://www.dropbox.com/s/n2l1vssqm2pfaqp/survey_json.txt?dl=1"
    //private val mockUrl = "https://e3777de6-509b-46a9-a996-ea2708cc0192.mock.pstmn.io/"
    private val ltaUrl = "http://ht-lang-track.ht.lu.se/api/"


    fun setCurrentUser(user: User){
        currentUser = user
        currentUserLiveData.postValue(currentUser)
    }
    fun getCurrentUser(): User{
        return currentUser
    }

    fun getAssignments(){
        val assigmnentUrl = "${ltaUrl}users/${currentUser.userName}/assignments"
        Fuel.get(assigmnentUrl)
            .header(mapOf("token" to idToken))
            .response { request, response, result ->
                println(request)
                println(response)
                val (bytes, error) = result
                if (error == null) {
                    if (bytes != null) {
                        /*val gson = Gson()
                        val itemType = object : TypeToken<List<Assignment>>() {}.type
                        val itemList = gson.fromJson<List<Assignment>>(String(bytes), itemType)*/

                        //TODO: sort and set livedata...
                        assignmentList = sortList(getAssignmentsFromJson(String(bytes))).toMutableList()
                        assignmentListLiveData.value = assignmentList
                    }
                }else{
                    println("Repository getAssignmens ERROR: ${error.localizedMessage}")
                }
            }
    }

    fun sortList(theListWithSurveys: List<Assignment>): List<Assignment>{

        //if no dataset and not expired
        val activeList = theListWithSurveys.filter {
            it.isActive()
        }.sortedByDescending { it.publishAt }

        //if dataset or expired
        val inactiveList = theListWithSurveys.filter {
            !it.isActive()
        }.sortedByDescending { it.publishAt }

        val returnlist = activeList.toMutableList()
        returnlist.addAll(inactiveList)
        return returnlist
    }

    fun getAssignmentsFromJson(json: String): List<Assignment>{
        val theListWithSurveys = mutableListOf<Assignment>()
        val jsonObj = JSONArray(json.substring(json.indexOf("["), json.lastIndexOf("]") + 1))
        for (i in 0 until jsonObj.length()) {
            var newsurvey: Survey? = null
            var newupdatedAt = ""
            var newcreatedAt = ""
            var newuserId = ""
            var newdataset: Dataset? = null
            var newpublishAt = ""
            var newexpireAt = ""
            var newid = ""
            val assignment = jsonObj.getJSONObject(i)
            try {
                val tempSurveyObj = assignment.get("survey") as? JSONObject
                if (tempSurveyObj != null) {
                    newsurvey = getSurveyFromJsonObj(tempSurveyObj)
                }
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            try {
                newupdatedAt = assignment.get("updatedAt") as? String ?: ""
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            try {
                newcreatedAt = assignment.get("createdAt") as? String ?: ""
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            try {
                newuserId = assignment.get("userId") as? String ?: ""
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            try {
                val datasetObj = assignment.get("dataset") as? JSONObject
                if (datasetObj != null){
                    newdataset = getDatasetFromJsonObj(datasetObj)
                }
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            try {
                newpublishAt = assignment.get("publishAt") as? String ?: ""
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            try {
                newexpireAt = assignment.get("expireAt") as? String ?: ""
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}
            try {
                newid = assignment.get("_id") as? String ?: ""
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}

            if (newsurvey != null){
                theListWithSurveys.add(
                    Assignment(
                        survey = newsurvey,
                        updatedAt = newupdatedAt,
                        createdAt = newcreatedAt,
                        userId = newuserId,
                        dataset = newdataset,
                        publishAt = newpublishAt,
                        expireAt = newexpireAt,
                        id = newid
                    )
                )
            }
        }
        return theListWithSurveys
    }

    fun getDatasetFromJsonObj(jsonObj: JSONObject): Dataset?{
        var id: String = ""
        var createdAt: String = ""
        var updatedAt: String = ""
        var answers = mutableListOf<Answer>()

        try {
            id = jsonObj.get("_id") as? String ?: ""
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        try {
            createdAt = jsonObj.get("createdAt") as? String ?: ""
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        try {
            updatedAt = jsonObj.get("updatedAt") as? String ?: ""
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        try {
            val answersArray = jsonObj.get("answers") as? JSONArray
            if (answersArray != null){

                for (a in 0 until answersArray.length()){
                    try{
                        val answerObj = answersArray.getJSONObject(a)
                        var index = -99
                        var type = ""
                        var intValue: Int? = -99
                        var stringValue: String? = ""
                        val multiValue = mutableListOf<Int>()
                        try {
                            index = answerObj.get("index") as? Int ?: -99
                        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
                        try {
                            type = answerObj.get("type") as? String ?: ""
                        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
                        try {
                            intValue = answerObj.get("intValue") as? Int
                        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
                        try {
                            stringValue = answerObj.get("stringValue") as? String
                        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
                        try {
                            val multiArray = answerObj.get("multiValue") as? JSONArray
                            if (multiArray != null){
                                for (m in 0 until multiArray.length()){
                                    multiValue.add(multiArray.getInt(m))
                                }
                            }
                        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
                        when(type){
                            "single" -> {
                                answers.add(
                                    Answer(
                                        type = type,
                                        index = index,
                                        likertAnswer = null,
                                        fillBlankAnswer = null,
                                        multipleChoiceAnswer = null,
                                        singleMultipleAnswer = intValue,
                                        openEndedAnswer = null
                                    )
                                )
                            }
                            "multi" -> {
                                answers.add(
                                    Answer(
                                        type = type,
                                        index = index,
                                        likertAnswer = null,
                                        fillBlankAnswer = null,
                                        multipleChoiceAnswer = multiValue,
                                        singleMultipleAnswer = null,
                                        openEndedAnswer = null
                                    )
                                )
                            }
                            "likert" -> {
                                answers.add(
                                    Answer(
                                        type = type,
                                        index = index,
                                        likertAnswer = intValue,
                                        fillBlankAnswer = null,
                                        multipleChoiceAnswer = null,
                                        singleMultipleAnswer = null,
                                        openEndedAnswer = null
                                    )
                                )
                            }
                            "blanks" -> {
                                answers.add(
                                    Answer(
                                        type = type,
                                        index = index,
                                        likertAnswer = null,
                                        fillBlankAnswer = intValue,
                                        multipleChoiceAnswer = null,
                                        singleMultipleAnswer = null,
                                        openEndedAnswer = null
                                    )
                                )
                            }
                            "open" -> {
                                answers.add(
                                    Answer(
                                        type = type,
                                        index = index,
                                        likertAnswer = null,
                                        fillBlankAnswer = null,
                                        multipleChoiceAnswer = null,
                                        singleMultipleAnswer = null,
                                        openEndedAnswer = stringValue
                                    )
                                )
                            }
                        }
                    }catch (e: Exception){ println("e: ${e.localizedMessage}")}
                }
            }
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        if (id != "" && answers.isNotEmpty()) {
            return Dataset(
                id = id,
                createdAt = createdAt,
                updatedAt = updatedAt,
                answers = answers
            )
        }else{
            return null
        }
    }
    fun getSurveyFromJsonObj(jsonObj: JSONObject): Survey{
        val tempSurvey = Survey()
        try {
            tempSurvey.id = jsonObj.get("id") as? String ?: ""
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        try {
            tempSurvey.name = jsonObj.get("name") as? String ?: ""
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        try {
            tempSurvey.title = jsonObj.get("title") as? String ?: ""
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        try {
            val tempquestions = jsonObj.get("questions") as? JSONArray
            if (tempquestions != null) {
                tempSurvey.questions = getQuestionsFromJsonArray(tempquestions)
            }
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        try {
            tempSurvey.createdAt = jsonObj.get("createdAt") as? String ?: ""
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        try {
            tempSurvey.updatedAt = jsonObj.get("updatedAt") as? String ?: ""
        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
        if (tempSurvey.questions != null) {
            for (question in tempSurvey.questions!!) {
                if (question.index == 0){
                    question.previous = 0
                    question.next = question.index + 1
                }else if (question.index < tempSurvey.questions!!.size - 1){
                    question.next = question.index + 1
                    question.previous = question.index - 1
                }else{
                    question.next = 0
                    question.previous = question.index - 1
                }
            }
        }
        return tempSurvey
    }

    fun getQuestionsFromJsonArray(questionsObj: JSONArray): List<Question>{
        val thequestions = mutableListOf<Question>()
        for (q in 0 until questionsObj.length()){
            val values = mutableListOf<String>()
            val tempQuestion = Question()
            val question = questionsObj.getJSONObject(q)

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
                val includeIfObject = question.getJSONObject("includeIf")
                val tempIncludeIf = IncludeIf()
                for (s in includeIfObject.keys()){
                    tempIncludeIf.ifIndex = includeIfObject.getInt("ifIndex")
                    tempIncludeIf.ifValue = includeIfObject.getInt("ifValue")
                }
                tempQuestion.includeIf = tempIncludeIf
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}

            try {
                val valuesArray = question.get("values") as? JSONArray
                if (valuesArray != null){
                    for (v in 0 until valuesArray.length()){
                        try{
                            values.add(valuesArray.getString(v))
                        }catch (e: Exception){ println("e: ${e.localizedMessage}")}
                    }
                }
            }catch (e: Exception){ println("e: ${e.localizedMessage}")}

            when (tempQuestion.type){
                "single" -> {
                    tempQuestion.singleMultipleAnswers = values
                }
                "multi" -> {
                    tempQuestion.multipleChoisesAnswers = values
                }
                "blanks" -> {
                    tempQuestion.fillBlanksChoises = values
                }
            }
            thequestions.add(tempQuestion)
        }
        return thequestions
    }

    //***************'gammalt!**************

    /*fun getSurveysFromDropbox(){
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
    }*/

    /*private fun convertJsonToSurveyList(jsonString: String): List<Survey>{
        //val gson = Gson()
        //val listType = object : TypeToken<List<Survey>>() { }.type
        //return gson.fromJson(jsonString, listType))

        return setOrder(setSurveyObjectsManually(jsonString))
    }*/


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

    /*private fun setSurveyObjectsManually(json: String): List<Survey>{

        val theListWithSurveys = mutableListOf<Survey>()
        val jsonObj = JSONArray(json.substring(json.indexOf("["), json.lastIndexOf("]") + 1))

        for (i in 0 until jsonObj.length()) {
            val item = jsonObj.getJSONObject(i)
            val tempSurvey = Survey()

            //tempSurvey.active = tryGetBoolean("active", item)

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
    }*/

    /*private fun getAnswer(jObj: JSONObject): List<Answer>{

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
    }*/



    /*private fun setOrder(inList: List<Survey>): List<Survey>{
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
    }*/

}