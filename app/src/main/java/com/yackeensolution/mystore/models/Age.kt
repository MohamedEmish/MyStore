package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Age {

    @SerializedName("Id")
    @Expose
    var id: Int = 0

    @SerializedName("Value")
    @Expose
    val value: String = ""

    @SerializedName("DateTime")
    @Expose
    val dateTime: String = ""
}