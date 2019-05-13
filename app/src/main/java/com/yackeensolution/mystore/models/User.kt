package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {

    @SerializedName("Email")
    @Expose
    var email: String = ""
    @SerializedName("Username")
    @Expose
    var username: String = ""
    @SerializedName("Name")
    @Expose
    var name: String = ""
    @SerializedName("FacebookId")
    @Expose
    var facebookId: String = ""
    @SerializedName("GmailId")
    @Expose
    var gmailId: String = ""
    @SerializedName("Phone1")
    @Expose
    var phone1: String = ""
    @SerializedName("Gender")
    @Expose
    var gender: String = ""

    @SerializedName("Phone2")
    @Expose
    var phone2: String = ""

    @SerializedName("Address1")
    @Expose
    var address1: String = ""

    @SerializedName("Address2")
    @Expose
    var address2: String = ""

    @SerializedName("Code")
    @Expose
    var code: String = ""

    @SerializedName("GoogleAccessToken")
    @Expose
    var googleAccessToken: String = ""

    @SerializedName("FacebookAccessToken")
    @Expose
    var facebookAccessToken: String = ""

    @SerializedName("Id")
    @Expose
    var id: Int = 0

    @SerializedName("DateTime")
    @Expose
    var dateTime: String = ""


    enum class Gender(private val code: Int) {
        both(0), Male(1), Female(2);


        companion object {

            fun fromCode(code: Int): Gender {
                for (gender in values()) {
                    if (gender.code == code) {
                        return gender
                    }
                }
                return both
            }
        }
    }
}
