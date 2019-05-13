package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeleteCartResponse {

    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String = ""
    @SerializedName("isSuccess")
    @Expose
    var isSuccess: Boolean = false
    @SerializedName("Response")
    @Expose
    var response: String = ""
}
