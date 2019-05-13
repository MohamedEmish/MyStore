package com.yackeensolution.mystore.data.viewModels

import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yackeensolution.mystore.data.RetrofitClass
import com.yackeensolution.mystore.models.Order
import com.yackeensolution.mystore.models.OrderDetailsResponse
import com.yackeensolution.mystore.models.OrderRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel(application: Application) : AndroidViewModel(application) {

    private val retrofitClass = RetrofitClass()
    private val disposable = CompositeDisposable()

    fun getMyOrders(context: Context): LiveData<List<Order>> {
        val data = MutableLiveData<List<Order>>()
        disposable.add(
                retrofitClass.getMyOrders(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer<List<Order>> { orders ->
                            data.setValue(orders)
                        }))
        return data
    }

    fun makeOrder(orderRequest: OrderRequest): LiveData<String> {
        val data = MutableLiveData<String>()
        retrofitClass.makeOrder(orderRequest).enqueue(object : Callback<Order> {
            override fun onResponse(@NonNull call: Call<Order>, @NonNull response: Response<Order>) {
                if (response.isSuccessful) {
                    data.value = "OK"
                } else {
                    data.value = "NOTOK"
                }
            }

            override fun onFailure(@NonNull call: Call<Order>, @NonNull t: Throwable) {
                data.value = "NOTOK"
            }
        })
        return data
    }

    fun getOrderDetail(context: Context, orderId: Int): LiveData<OrderDetailsResponse> {
        val data = MutableLiveData<OrderDetailsResponse>()
        disposable.add(
                retrofitClass.getOrderDetail(context, orderId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer<OrderDetailsResponse> { details ->
                            data.setValue(details)
                        }))
        return data
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}