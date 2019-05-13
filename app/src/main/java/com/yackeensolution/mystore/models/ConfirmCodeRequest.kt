package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConfirmCodeRequest {

    @SerializedName("Email")
    @Expose
    var email: String = ""

    @SerializedName("Code")
    @Expose
    var code: String = ""

    @SerializedName("NewPassword")
    @Expose
    var newPassword: String = ""
}
