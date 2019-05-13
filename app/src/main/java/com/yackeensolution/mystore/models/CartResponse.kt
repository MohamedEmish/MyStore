package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CartResponse {

    @SerializedName("UserId")
    @Expose
    var userId: Int = 0

    @SerializedName("ProductId")
    @Expose
    var productId: Int = 0

    @SerializedName("UserRate")
    @Expose
    var userRate: Int = 0

    @SerializedName("ReviewTitle")
    @Expose
    var reviewTitle: String? = null

    @SerializedName("ReviewDescription")
    @Expose
    var reviewDescription: String? = null

    @SerializedName("InCart")
    @Expose
    var isInCart: Boolean = false

    @SerializedName("IsFavorite")
    @Expose
    var isFavorite: Boolean = false

    @SerializedName("InStock")
    @Expose
    var isInStock: Boolean = false

    @SerializedName("Id")
    @Expose
    var id: Int = 0

    @SerializedName("DateTime")
    @Expose
    var dateTime: String? = null
}
