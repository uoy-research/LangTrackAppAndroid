package uk.ac.york.langtrackapp.data.model

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Survey(

    var name: String = "",
    var id: String = "",
    var title: String = "",
    var questions: List<Question>? = null,
    var answer: MutableList<Answer>? = null,
    var updatedAt: String = "",
    var createdAt: String = ""
    ) : Parcelable

