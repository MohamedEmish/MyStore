package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequest {

    @SerializedName("Email")
    @Expose
    var email: String = ""
    @SerializedName("Password")
    @Expose
    var password: String = ""

    @SerializedName("DeviceToken")
    @Expose
    var deviceToken: String = "string"
}
