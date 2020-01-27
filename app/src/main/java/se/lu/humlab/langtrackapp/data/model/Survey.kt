package se.lu.humlab.langtrackapp.data.model

data class Survey (

    var id: String = "",
    var date: Long = -1,
    var responded: Boolean = false,
    var title: String = "",
    var headerText: String = "",
    var questions: List<Question>? = null,
    var footerText: String = ""
)