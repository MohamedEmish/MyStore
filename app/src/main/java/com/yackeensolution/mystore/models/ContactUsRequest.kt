package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ContactUsRequest {

    @SerializedName("UserId")
    @Expose
    var userId: Int = 0
    @SerializedName("Name")
    @Expose
    var name: String = ""
    @SerializedName("Email")
    @Expose
    var email: String = ""
    @SerializedName("Body")
    @Expose
    var body: String = ""
}
