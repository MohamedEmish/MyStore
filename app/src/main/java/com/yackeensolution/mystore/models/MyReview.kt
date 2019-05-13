package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyReview {

    @SerializedName("ReviewTitle")
    @Expose
    var reviewTitle: String = ""

    @SerializedName("MyReview")
    @Expose
    var review: String = ""

    @SerializedName("ProductTitle")
    @Expose
    var productTitle: String = ""

    @SerializedName("ImageUrl")
    @Expose
    var imageUrl: String = ""

    @SerializedName("ProductId")
    @Expose
    var productId: Int = 0

    @SerializedName("Rate")
    @Expose
    var rate: Float = 0.0f
}
