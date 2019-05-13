package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AboutUsResponse {

    @SerializedName("Id")
    @Expose
    var id: Int = 0

    @SerializedName("Contact")
    @Expose
    val contact: String = ""

    @SerializedName("FacebookLink")
    @Expose
    val facebookLink: String = ""

    @SerializedName("InstagramLink")
    @Expose
    val instagramLink: String = ""

    @SerializedName("PintrestLink")
    @Expose
    val pintrestLink: String = ""

    @SerializedName("TwitterLink")
    @Expose
    val twitterLink: String = ""

    @SerializedName("YoutubeLink")
    @Expose
    val youtubeLink: String = ""

    @SerializedName("Description")
    @Expose
    val description: String = ""
}