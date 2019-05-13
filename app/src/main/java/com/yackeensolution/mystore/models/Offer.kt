package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Offer {
    @SerializedName("Id")
    @Expose
    var id: Int = 0

    @SerializedName("CategoryId")
    @Expose
    var categoryId: Int = 0

    @SerializedName("ProductId")
    @Expose
    var productId: Int = 0

    @SerializedName("Discount")
    @Expose
    var discount: Double = 0.toDouble()

    @SerializedName("Title")
    @Expose
    var title: String = ""

    @SerializedName("Description")
    @Expose
    var description: String = ""

    @SerializedName("ImageUrl")
    @Expose
    var imageUrl: String = ""

    @SerializedName("ExpirationDateTime")
    @Expose
    var expirationDateTime: String = ""

    @SerializedName("OfferType")
    @Expose
    var offerType: String = ""

}
