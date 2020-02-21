package se.lu.humlab.langtrackapp.data.model

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.os.Parcel
import android.os.Parcelable


class Survey() : Parcelable {

    var active: Boolean = false
    var id: String = ""
    var date: Long = -1
    var respondeddate: Long = -1
    var title: String = ""
    var headerText: String = ""
    var questions: List<Question>? = null
    var footerText: String = ""
    var answer: MutableMap<Int,Int>? = null

    constructor(parcel: Parcel) : this() {
        active = parcel.readByte() != 0.toByte()
        id = parcel.readString() ?: ""
        date = parcel.readLong()
        respondeddate = parcel.readLong()
        title = parcel.readString() ?: ""
        headerText = parcel.readString() ?: ""
        questions = parcel.createTypedArrayList(Question)
        footerText = parcel.readString() ?: ""
        answer = buildTheMap(parcel)

    }

    fun buildTheMap(parcel: Parcel): MutableMap<Int,Int>? {

         val size = parcel.readInt()
         val map = mutableMapOf<Int, Int>()

         for (i in 1..size) {
             val key = parcel.readInt()
             val value = parcel.readInt()
             map[key] = value
         }
         return if (map.isNullOrEmpty()) null else map
     }
    fun writeToMap(parcel: Parcel) {
        if (answer != null) {
            parcel.writeInt(answer!!.size ?: 0)
            for ((key, value) in answer!!) {
                parcel.writeInt(key)
                parcel.writeInt(value)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (active) 1 else 0)
        parcel.writeString(id)
        parcel.writeLong(date)
        parcel.writeLong(respondeddate)
        parcel.writeString(title)
        parcel.writeString(headerText)
        parcel.writeTypedList(questions)
        parcel.writeString(footerText)
        if (answer != null) {
            writeToMap(parcel)
        }
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