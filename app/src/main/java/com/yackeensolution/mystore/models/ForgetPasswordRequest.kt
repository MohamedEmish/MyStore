package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgetPasswordRequest {
    @SerializedName("Email")
    @Expose
    var email: String = ""
}
