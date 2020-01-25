package se.lu.humlab.langtrackapp.data.model

data class Question (
    var type: Int = 99,
    var id: String = "",
    var previous: Int = 0,
    var index: Int = 0,
    var next: Int = 0,
    var title: String = "",
    var text: String = "",
    var description: String = "",
    var likertScale: List<String>? = null,
    var fillBlanksChoises: List<String>? = null,
    var miltipleChoisesAnswers: List<String>? = null,
    var singleMultipleAnswers: List<String>? = null
)