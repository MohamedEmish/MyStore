package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Product {

    @SerializedName("Amount")
    @Expose
    var amount: Int = 1

    @SerializedName("Id")
    @Expose
    var id: Int = 0

    @SerializedName("Title")
    @Expose
    var title: String = ""

    @SerializedName("Rate")
    @Expose
    var rate: Float = 0.0f

    @SerializedName("Price")
    @Expose
    var price: Float = 0.0f

    @SerializedName("Favorite")
    @Expose
    var isFav: Boolean = false

    @SerializedName("InCart")
    @Expose
    var isInCart: Boolean = false

    @SerializedName("InStock")
    @Expose
    var isInStock: Boolean = false

    @SerializedName("ImageUrl")
    @Expose
    var imageUrl: String = ""

    @SerializedName("Discount")
    @Expose
    var discount: Float = 0.0f

    @SerializedName("ReviewsCount")
    @Expose
    var reviewsCount: Int = 0
}