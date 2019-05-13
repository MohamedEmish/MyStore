package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductDetail {
    @SerializedName("Id")
    @Expose
    var id: Int = 0
    @SerializedName("Title")
    @Expose
    var title: String = ""
    @SerializedName("Description")
    @Expose
    var description: String = ""
    @SerializedName("Rate")
    @Expose
    var rate: Float = 0.0f
    @SerializedName("UserRate")
    @Expose
    var userRate: Float = 0.0f
    @SerializedName("Price")
    @Expose
    var price: Float = 0.0f
    @SerializedName("Favorite")
    @Expose
    var isFav: Boolean = false
    @SerializedName("InCart")
    @Expose
    var isInCart: Boolean = false
    @SerializedName("Discount")
    @Expose
    var discount: Float = 0.0f
    @SerializedName("Gender")
    @Expose
    var gender: String = ""
    @SerializedName("Amount")
    @Expose
    var amount: Int = 0
    @SerializedName("FacebookLink")
    @Expose
    var fbLink: String = ""
    @SerializedName("YoutubeLink")
    @Expose
    var youtubeLink: String = ""
    @SerializedName("FavouriteCount")
    @Expose
    var favCount: Int = 0
    @SerializedName("ReviewsCount")
    @Expose
    var reviewsCount: Int = 0
    @SerializedName("Code")
    @Expose
    var code: String = ""
    @SerializedName("InStock")
    @Expose
    var isInStock: Boolean = false
    @SerializedName("Images")
    @Expose
    var imageUrls: List<String> = ArrayList<String>()
    @SerializedName("Age")
    @Expose
    var age: String = ""
    @SerializedName("NumberOfPlayers")
    @Expose
    var numberOfPlayers: String = ""
    @SerializedName("IntelligenceType")
    @Expose
    var intelligenceType: String = ""
    @SerializedName("Category")
    @Expose
    var category: String = ""
}