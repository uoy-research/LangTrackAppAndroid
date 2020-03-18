package se.lu.humlab.langtrackapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Assignment(
    var survey: Survey,
    var updatedAt: String,
    var createdAt: String,
    var userId: String,
    var dataset: Dataset?,
    var publishAt: String,
    var expireAt: String,
    var id: String
) : Parcelable

/*
* var survey = Survey()
    var updatedAt = ""
    var createdAt = ""
    var userId = ""
    var dataset: Dataset? = nil
    var published = ""
    var expiry = ""
    var id = ""

    func timeLeftToExpiryInMilli() -> Int64{
        let now = Date()
        let exp = DateParser.getDate(dateString: expiry) ?? now
        let millisecondsLeft = exp.millisecondsSince1970 - now.millisecondsSince1970
        if millisecondsLeft <= 0{
            return 0
        }else{
            return millisecondsLeft
        }
    }
    * */