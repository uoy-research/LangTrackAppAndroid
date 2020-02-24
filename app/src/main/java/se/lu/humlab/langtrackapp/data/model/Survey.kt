package se.lu.humlab.langtrackapp.data.model

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

    var active: Boolean = false,
    var id: String = "",
    var date: Long = -1,
    var respondeddate: Long = -1,
    var published: Long = -1,
    var expiry: Long = -1,
    var title: String = "",
    var headerText: String = "",
    var questions: List<Question>? = null,
    var footerText: String = "",
    var answer: MutableList<Answer>? = null
    ) : Parcelable
