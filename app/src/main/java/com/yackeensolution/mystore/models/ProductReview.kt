package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductReview {

    @SerializedName("ReviewTitle")
    @Expose
    var reviewTitle: String = ""
    @SerializedName("Review")
    @Expose
    var review: String = ""
    @SerializedName("Name")
    @Expose
    var name: String = ""
    @SerializedName("Rate")
    @Expose
    var rate: Float = 0.0f
    @SerializedName("Id")
    @Expose
    var id: Int = 0
    @SerializedName("DateTime")
    @Expose
    var dateTime: String = ""
}


