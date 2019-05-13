package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class News {

    @SerializedName("Id")
    @Expose
    var id: Int = 0
    @SerializedName("Title")
    @Expose
    var title: String = ""
    @SerializedName("Description")
    @Expose
    var description: String = ""
    @SerializedName("ImageUrl")
    @Expose
    var imageUrl: String = ""
    @SerializedName("DateTime")
    @Expose
    var dateTime: String = ""
}