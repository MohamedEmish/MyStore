package com.yackeensolution.mystore.data.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yackeensolution.mystore.data.RetrofitClass
import com.yackeensolution.mystore.models.Offer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class OffersViewModel(application: Application) : AndroidViewModel(application) {

    private val retrofitClass = RetrofitClass()
    private val disposable = CompositeDisposable()

    fun getNews(context: Context): LiveData<List<Offer>> {
        val data = MutableLiveData<List<Offer>>()
        disposable.add(
                retrofitClass.getOffers(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer<List<Offer>> { offers ->
                            data.setValue(offers)
                        }))
        return data
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}