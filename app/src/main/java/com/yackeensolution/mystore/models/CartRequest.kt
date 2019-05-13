package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CartRequest(
        @SerializedName("UserId")
        @Expose
        val userId: Int,

        @SerializedName("ProductId")
        @Expose
        val productId: Int,

        @SerializedName("Add")
        @Expose
        val add: Boolean)