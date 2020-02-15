package se.lu.humlab.langtrackapp.data.model

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.os.Parcel
import android.os.Parcelable

data class Survey (
    var active: Boolean = false,
    var id: String = "",
    var date: Long = -1,
    var respondeddate: Long = -1,
    var responded: Boolean = false,
    var title: String = "",
    var headerText: String = "",
    var questions: List<Question>? = null,
    var footerText: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readBoolean(),
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Question),
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeBoolean(active)
        parcel.writeString(id)
        parcel.writeLong(date)
        parcel.writeLong(respondeddate)
        parcel.writeByte(if (responded) 1 else 0)
        parcel.writeString(title)
        parcel.writeString(headerText)
        parcel.writeTypedList(questions)
        parcel.writeString(footerText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Survey> {
        override fun createFromParcel(parcel: Parcel): Survey {
            return Survey(parcel)
        }

        override fun newArray(size: Int): Array<Survey?> {
            return arrayOfNulls(size)
        }
    }
}