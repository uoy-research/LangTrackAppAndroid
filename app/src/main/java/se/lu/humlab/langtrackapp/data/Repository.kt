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
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import se.lu.humlab.langtrackapp.data.model.*
import se.lu.humlab.langtrackapp.screen.surveyContainer.SurveyContainerActivity
import se.lu.humlab.langtrackapp.util.IO
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
    //private val ltaUrl = "http://ht-lang-track.ht.lu.se:443/"
    var client = OkHttpClient()


    fun setCurrentUser(user: User){
        currentUser = user
        currentUserLiveData.postValue(currentUser)
    }
    fun getCurrentUser(): User{
        return currentUser
    }

    fun putDeviceToken(deviceToken: String, versionNumber: String){
        //TODO: Fix as postAnswer
        val localTimeZoneIdentifier = TimeZone.getDefault().id
        val deviceTokenUrl = "${ltaUrl}users/${currentUser.id}"
        if (deviceToken != "" && localTimeZoneIdentifier != ""){

            val formBody: RequestBody = FormBody.Builder()
                .add("timezone", localTimeZoneIdentifier)
                .add("deviceToken", deviceToken)
                .build()

            IO.execute {
                val request = Request.Builder()
                    .header("token", idToken)
                    .url(deviceTokenUrl)
                    .put(formBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        println("putDeviceToken ERROR: ${response.body}")
                    }
                }
            }
        }
    }

    fun postAnswer(answerDict: Map<Int,Answer>){
        if (currentUser.id.isNotEmpty()){
            val answers = mutableListOf<AnswerBody>()
            var stringValue: String? = null
            var intValue: Int? = null
            var multiValue: MutableList<Int>? = null
            for (answer in answerDict.values){
                when(answer.type){
                    SurveyContainerActivity.LIKERT_SCALES ->
                        intValue = answer.likertAnswer
                    SurveyContainerActivity.SINGLE_MULTIPLE_ANSWERS ->
                        intValue = answer.singleMultipleAnswer
                    SurveyContainerActivity.FILL_IN_THE_BLANK ->
                        intValue = answer.fillBlankAnswer
                    SurveyContainerActivity.MULTIPLE_CHOICE ->
                        multiValue = answer.multipleChoiceAnswer
                    SurveyContainerActivity.OPEN_ENDED_TEXT_RESPONSES ->
                        stringValue = answer.openEndedAnswer
                    SurveyContainerActivity.TIME_DURATION ->
                        intValue = answer.timeDurationAnswer
                }
                val body = AnswerBody(
                    index = answer.index,
                    type = answer.type,
                    intValue = intValue,
                    multiValue = multiValue,
                    stringValue = stringValue
                )
                if (body.index != -99){
                    answers.add(body)
                }

            }
            val answerUrl = "${ltaUrl}users/${currentUser.id}/assignments/${selectedAssignment!!.id}/datasets"
            val gson = Gson()
            val jsonAnswer: String = gson.toJson(answers)
            val jsonAnswer2 = "{\"answers\":${jsonAnswer}}"

            IO.execute {
                val httpUrl = answerUrl.toHttpUrl()
                val httpUrlBuilder = httpUrl.newBuilder()
                val requestBuilder = Request.Builder().url(httpUrlBuilder.build())
                val mediaTypeJson = "application/json; charset=utf-8".toMediaType()
                requestBuilder.post(
                    jsonAnswer2.toRequestBody(mediaTypeJson)
                )
                val call = client.newCall(requestBuilder.build())
                call.execute().use {
                    if (it.isSuccessful){
                        println("postAnswer SUCCESS: ${it.body}")
                    }else{
                        println("postAnswer ERROR: ${it.body}")
                    }
                }
            }
        }
    }

    fun getAssignments(){
        val assigmnentUrl = "${ltaUrl}users/${currentUser.userName}/assignments"
        Fuel.get(assigmnentUrl)
            .header(mapOf("token" to idToken))
            .response { request, response, result ->

                val (bytes, error) = result
                if (error == null) {
                    if (bytes != null) {
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
            }catch (e: Exception){ }
            try {
                newupdatedAt = assignment.get("updatedAt") as? String ?: ""
            }catch (e: Exception){ }
            try {
                newcreatedAt = assignment.get("createdAt") as? String ?: ""
            }catch (e: Exception){ }
            try {
                newuserId = assignment.get("userId") as? String ?: ""
            }catch (e: Exception){ }
            try {
                val datasetObj = assignment.get("dataset") as? JSONObject
                if (datasetObj != null){
                    newdataset = getDatasetFromJsonObj(datasetObj)
                }
            }catch (e: Exception){ }
            try {
                newpublishAt = assignment.get("publishAt") as? String ?: ""
            }catch (e: Exception){ }
            try {
                newexpireAt = assignment.get("expireAt") as? String ?: ""
            }catch (e: Exception){ }
            try {
                newid = assignment.get("_id") as? String ?: ""
            }catch (e: Exception){ }

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
        }catch (e: Exception){ }
        try {
            createdAt = jsonObj.get("createdAt") as? String ?: ""
        }catch (e: Exception){ }
        try {
            updatedAt = jsonObj.get("updatedAt") as? String ?: ""
        }catch (e: Exception){ }
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
                        }catch (e: Exception){ }
                        try {
                            type = answerObj.get("type") as? String ?: ""
                        }catch (e: Exception){ }
                        try {
                            intValue = answerObj.get("intValue") as? Int
                        }catch (e: Exception){ }
                        try {
                            stringValue = answerObj.get("stringValue") as? String
                        }catch (e: Exception){ }
                        try {
                            val multiArray = answerObj.get("multiValue") as? JSONArray
                            if (multiArray != null){
                                for (m in 0 until multiArray.length()){
                                    multiValue.add(multiArray.getInt(m))
                                }
                            }
                        }catch (e: Exception){ }
                        when(type){
                            SurveyContainerActivity.SINGLE_MULTIPLE_ANSWERS -> {
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
                            SurveyContainerActivity.MULTIPLE_CHOICE -> {
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
                            SurveyContainerActivity.LIKERT_SCALES -> {
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
                            SurveyContainerActivity.FILL_IN_THE_BLANK -> {
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
                            SurveyContainerActivity.OPEN_ENDED_TEXT_RESPONSES -> {
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
                            SurveyContainerActivity.TIME_DURATION -> {
                                answers.add(
                                    Answer(
                                        type = type,
                                        index = index,
                                        likertAnswer = null,
                                        fillBlankAnswer = null,
                                        multipleChoiceAnswer = null,
                                        singleMultipleAnswer = null,
                                        openEndedAnswer = null,
                                        timeDurationAnswer = intValue
                                    )
                                )
                            }
                        }
                    }catch (e: Exception){ }
                }
            }
        }catch (e: Exception){ }
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
        }catch (e: Exception){ }
        try {
            tempSurvey.name = jsonObj.get("name") as? String ?: ""
        }catch (e: Exception){ }
        try {
            tempSurvey.title = jsonObj.get("title") as? String ?: ""
        }catch (e: Exception){ }
        try {
            val tempquestions = jsonObj.get("questions") as? JSONArray
            if (tempquestions != null) {
                tempSurvey.questions = getQuestionsFromJsonArray(tempquestions)
            }
        }catch (e: Exception){ }
        try {
            tempSurvey.createdAt = jsonObj.get("createdAt") as? String ?: ""
        }catch (e: Exception){ }
        try {
            tempSurvey.updatedAt = jsonObj.get("updatedAt") as? String ?: ""
        }catch (e: Exception){ }
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
            }catch (e: Exception){ }

            try {
                tempQuestion.index = question.get("index") as? Int ?: 0
            }catch (e: Exception){ }

            try {
                tempQuestion.text = question.get("text") as? String ?: ""
            }catch (e: Exception){ }

            try {
                tempQuestion.title = question.get("title") as? String ?: ""
            }catch (e: Exception){ }

            try {
                tempQuestion.description = question.get("description") as? String ?: ""
            }catch (e: Exception){ }

            try {
                tempQuestion.likertMax = question.get("maxAnnotation") as? String ?: ""
            }catch (e: Exception){ }

            try {
                tempQuestion.likertMin = question.get("minAnnotation") as? String ?: ""
            }catch (e: Exception){ }

            try {
                tempQuestion.id = question.get("id") as? String ?: ""
            }catch (e: Exception){ }

            try {
                val tempskip = question.getJSONObject("skip")
                val theSkip = SkipLogic()
                for (s in tempskip.keys()){
                    theSkip.ifChosen = tempskip.getInt("ifChosen")
                    theSkip.goto = tempskip.getInt("goto")
                }
                tempQuestion.skip = theSkip
            }catch (e: Exception){ }
            try {
                val includeIfObject = question.getJSONObject("includeIf")
                val tempIncludeIf = IncludeIf()
                for (s in includeIfObject.keys()){
                    tempIncludeIf.ifIndex = includeIfObject.getInt("ifIndex")
                    tempIncludeIf.ifValue = includeIfObject.getInt("ifValue")
                }
                tempQuestion.includeIf = tempIncludeIf
            }catch (e: Exception){ }

            try {
                val valuesArray = question.get("values") as? JSONArray
                if (valuesArray != null){
                    for (v in 0 until valuesArray.length()){
                        try{
                            values.add(valuesArray.getString(v))
                        }catch (e: Exception){ }
                    }
                }
            }catch (e: Exception){ }

            when (tempQuestion.type){
                SurveyContainerActivity.SINGLE_MULTIPLE_ANSWERS -> {
                    tempQuestion.singleMultipleAnswers = values
                }
                SurveyContainerActivity.MULTIPLE_CHOICE -> {
                    tempQuestion.multipleChoisesAnswers = values
                }
                SurveyContainerActivity.FILL_IN_THE_BLANK -> {
                    tempQuestion.fillBlanksChoises = values
                }
            }
            thequestions.add(tempQuestion)
        }
        return thequestions
    }
}