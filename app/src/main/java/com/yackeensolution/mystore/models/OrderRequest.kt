package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderRequest {
    @SerializedName("Order")
    @Expose
    var order: Order? = null
    @SerializedName("OrderProducts")
    @Expose
    var productsToOrder: List<ProductToOrder> = ArrayList<ProductToOrder>()
}
