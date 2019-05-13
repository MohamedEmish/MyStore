package com.yackeensolution.mystore.data.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yackeensolution.mystore.data.RetrofitClass
import com.yackeensolution.mystore.models.Branch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class BranchesViewModel(application: Application) : AndroidViewModel(application) {

    private val retrofitClass = RetrofitClass()
    private val disposable = CompositeDisposable()

    fun getAllBranches(context: Context): LiveData<List<Branch>> {
        val data = MutableLiveData<List<Branch>>()
        disposable.add(
                retrofitClass.branches
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer<List<Branch>> { branches ->
                            data.setValue(branches)
                        }))
        return data
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}