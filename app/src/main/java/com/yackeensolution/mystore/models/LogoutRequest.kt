package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LogoutRequest(@SerializedName("UserId")
                    @Expose var userId: Int?, @SerializedName("Lang")
                    @Expose var language: String?) {

    @SerializedName("DeviceToken")
    @Expose
    var deviceToken: String = "string"

}
