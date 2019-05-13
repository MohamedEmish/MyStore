package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse {
    @SerializedName("User")
    @Expose
    var user: User? = null
    @SerializedName("IsExist")
    @Expose
    var isExist: Boolean = false
}
