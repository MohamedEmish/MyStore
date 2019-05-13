package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgetPasswordResponse {
    @SerializedName("Email")
    @Expose
    var email: String = ""
    @SerializedName("Code")
    @Expose
    var code: String = ""
    @SerializedName("UserId")
    @Expose
    var userId: Int = 0
}

