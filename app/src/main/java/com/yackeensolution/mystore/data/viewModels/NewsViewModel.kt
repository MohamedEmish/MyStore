package com.yackeensolution.mystore.data.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yackeensolution.mystore.data.RetrofitClass
import com.yackeensolution.mystore.models.News
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val retrofitClass = RetrofitClass()
    private val disposable = CompositeDisposable()

    fun getNews(context: Context): LiveData<List<News>> {
        val data = MutableLiveData<List<News>>()
        disposable.add(
                retrofitClass.getNews(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { news ->
                            data.setValue(news)
                        })
        return data
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}