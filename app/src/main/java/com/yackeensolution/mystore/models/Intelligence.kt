package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Intelligence {

    @SerializedName("Id")
    @Expose
    var id: Int = 0
    @SerializedName("Title")
    @Expose
    var title: String = ""
}
