package se.lu.humlab.langtrackapp.data.model

data class Survey (

    var id: String = "",
    var date: Double = -1.0,
    var responded: Boolean = false,
    var title: String = "",
    var headerText: String = "",
    var questions: List<String>?,
    var footerText: String = ""
)