package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FavoriteRequest {
    @SerializedName("UserId")
    @Expose
    var userId: Int = 0
    @SerializedName("ProductId")
    @Expose
    var productId: Int = 0
    @SerializedName("IsFavorite")
    @Expose
    var isFavorite: Boolean = false
}