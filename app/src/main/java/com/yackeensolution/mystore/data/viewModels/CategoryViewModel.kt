package com.yackeensolution.mystore.data.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.yackeensolution.mystore.data.RetrofitClass
import com.yackeensolution.mystore.models.Category
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val retrofitClass = RetrofitClass()
    private val disposable = CompositeDisposable()

    fun getAllCategories(context: Context): LiveData<List<Category>> {
        val data = MutableLiveData<List<Category>>()
        disposable.add(
                retrofitClass.getAllCategories(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer<List<Category>> { categories ->
                            data.setValue(categories)
                        }))
        return data
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}

