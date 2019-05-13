package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductToOrder(id: Int, amount: Int) {
    @SerializedName("ProductId")
    @Expose
    var productId: Int = id
    @SerializedName("Amount")
    @Expose
    var amount: Int = amount
    @SerializedName("OrderId")
    @Expose
    var orderId: Int = 0
    @SerializedName("Id")
    @Expose
    var id: Int = 0
    @SerializedName("DateTime")
    @Expose
    var dateTime: String? = null
}
