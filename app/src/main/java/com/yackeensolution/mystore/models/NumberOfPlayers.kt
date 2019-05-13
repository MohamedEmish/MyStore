package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NumberOfPlayers {

    @SerializedName("Value")
    @Expose
    var value: String = ""
    @SerializedName("Id")
    @Expose
    var id: Int = 0
    @SerializedName("DateTime")
    @Expose
    var dateTime: String = ""
}