package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AddReviewRequest {

    @SerializedName("UserId")
    @Expose
    var userId: Int = 0

    @SerializedName("ProductId")
    @Expose
    var productId: Int = 0

    @SerializedName("Review")
    @Expose
    var review: String = ""

    @SerializedName("Title")
    @Expose
    var title: String = ""

    @SerializedName("Rate")
    @Expose
    var rate: Int = 0
}