package com.yackeensolution.mystore.models

import com.google.gson.annotations.SerializedName

class OrderDetailsResponse {

    @SerializedName("Order")
    var order: Order? = null
        internal set
    @SerializedName("Products")
    var products: List<Product>? = null
        internal set
}
