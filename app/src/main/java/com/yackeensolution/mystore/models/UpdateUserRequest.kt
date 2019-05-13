package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateUserRequest(mUser: User?) {

    @SerializedName("User")
    @Expose
    var user: User? = mUser
}
