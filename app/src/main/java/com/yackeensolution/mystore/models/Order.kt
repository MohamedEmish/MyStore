package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Order {

    @SerializedName("UserId")
    @Expose
    var userId: Int = 0
    @SerializedName("Status")
    @Expose
    var statusValue: Int = 0
    @SerializedName("Total")
    @Expose
    var totalPrice: Double = 0.toDouble()
    @SerializedName("TotalDiscount")
    @Expose
    var totalDiscount: Double = 0.toDouble()
    @SerializedName("TotalAfterDiscount")
    @Expose
    var finalPrice: Double = 0.toDouble()
    @SerializedName("DeliveryAddress")
    @Expose
    var deliveryAddress: String = ""
    @SerializedName("Contact")
    @Expose
    var contactNumber: String = ""
    @SerializedName("Notes")
    @Expose
    var deliveryNotes: String = ""
    @SerializedName("Id")
    @Expose
    var id: Int = 0
    @SerializedName("DateTime")
    @Expose
    var dateTime: String = ""

    enum class Status(val code: Int) {
        Ordered(0), InReview(1), Confirmed(2), InWay(3), Undefined(4);


        companion object {

            fun fromCode(code: Int): Status {
                for (status in values()) {
                    if (status.code == code) {
                        return status
                    }
                }
                return Undefined
            }
        }
    }
}

