package com.yackeensolution.mystore.data.viewModels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yackeensolution.mystore.data.RetrofitClass
import com.yackeensolution.mystore.models.AboutUsResponse
import com.yackeensolution.mystore.models.ContactUsRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionViewModel(application: Application) : AndroidViewModel(application) {

    private val retrofitClass = RetrofitClass()
    private val disposable = CompositeDisposable()

    val aboutUs: LiveData<AboutUsResponse>
        get() {
            val data = MutableLiveData<AboutUsResponse>()
            disposable.add(
                    retrofitClass.aboutUs
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(Consumer<AboutUsResponse> { aboutUs ->
                                data.setValue(aboutUs)
                            }))
            return data
        }

    fun contactUs(contactUsRequest: ContactUsRequest?): LiveData<String> {
        val data = MutableLiveData<String>()
        retrofitClass.contactUs(contactUsRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(@NonNull call: Call<ResponseBody>, @NonNull response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    data.value = "OK"
                }
            }

            override fun onFailure(@NonNull call: Call<ResponseBody>, @NonNull t: Throwable) {
                data.value = "NOTOK"
            }
        })
        return data
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}