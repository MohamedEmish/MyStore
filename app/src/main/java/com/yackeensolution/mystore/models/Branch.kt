package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Branch {

    @SerializedName("Name")
    @Expose
    var name: String = ""

    @SerializedName("Address")
    @Expose
    var address: String = ""

    @SerializedName("Contact")
    @Expose
    var phoneNumber: String = ""

    @SerializedName("Lang")
    @Expose
    var lang: Float = 0.0f

    @SerializedName("Lat")
    @Expose
    var lat: Float = 0.0f

    @SerializedName("Id")
    @Expose
    var id: Int = 0

    @SerializedName("StartAt")
    @Expose
    var startAt: String = ""

    @SerializedName("EndAt")
    @Expose
    var endAt: String = ""

    @SerializedName("DateTime")
    @Expose
    var date: String = ""
}
