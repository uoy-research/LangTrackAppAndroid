package se.lu.humlab.langtrackapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Dataset(
    var createdAt: String,
    var updatedAt: String,
    var answers: MutableList<Answer>
) : Parcelable