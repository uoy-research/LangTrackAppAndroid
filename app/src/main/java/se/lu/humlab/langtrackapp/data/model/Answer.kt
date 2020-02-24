package se.lu.humlab.langtrackapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answer(
    var index: Int = 0,
    var likertAnswer: Int? = null,
    var fillBlankAnswer: Int? = null,
    var multipleChoiceAnswer: MutableList<Int>? = null,
    var singleMultipleAnswer: Int? = null,
    var openEndedAnswer: String? = null
    ): Parcelable