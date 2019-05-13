package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistrationRequest {

    @SerializedName("Email")
    @Expose
    var email: String = ""
    @SerializedName("Password")
    @Expose
    var password: String = ""
    @SerializedName("Username")
    @Expose
    var username: String = ""
    @SerializedName("Name")
    @Expose
    var name: String = ""
    @SerializedName("Phone")
    @Expose
    var phone: String = ""
    @SerializedName("Address")
    @Expose
    var address: String = ""
    @SerializedName("Gender")
    @Expose
    var gender: String = ""
    @SerializedName("GoogleAccessToken")
    @Expose
    var googleAccessToken: String = "string"
    @SerializedName("FacebookAccessToken")
    @Expose
    var facebookAccessToken: String = "string"
    @SerializedName("DeviceToken")
    @Expose
    var deviceToken: String = "string"

}
