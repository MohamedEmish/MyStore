package com.yackeensolution.mystore.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeleteCartRequest(@SerializedName("UserId")
                        @Expose var userId: Int)
