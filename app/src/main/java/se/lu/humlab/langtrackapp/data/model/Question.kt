package se.lu.humlab.langtrackapp.data.model

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.os.Parcel
import android.os.Parcelable

data class Question (
    var type: String = "",
    var id: String = "",
    var previous: Int = 0,
    var index: Int = 0,
    var next: Int = 0,
    var title: String = "",
    var text: String = "",
    var description: String = "",
    var likertScale: MutableList<String>? = null,
    var fillBlanksChoises: MutableList<String>? = null,
    var multipleChoisesAnswers: MutableList<String>? = null,
    var singleMultipleAnswers: MutableList<String>? = null,
    var skip: SkipLogic? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.readParcelable(SkipLogic::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(id)
        parcel.writeInt(previous)
        parcel.writeInt(index)
        parcel.writeInt(next)
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(description)
        parcel.writeStringList(likertScale)
        parcel.writeStringList(fillBlanksChoises)
        parcel.writeStringList(multipleChoisesAnswers)
        parcel.writeStringList(singleMultipleAnswers)
        parcel.writeParcelable(skip, 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }

}