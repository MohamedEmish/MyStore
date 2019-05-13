package com.yackeensolution.mystore.data.viewModels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.data.RetrofitClass
import com.yackeensolution.mystore.models.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofitClass = RetrofitClass()
    private val disposable = CompositeDisposable()

    fun getCategoryProducts(context: Context, categoryId: Int): LiveData<List<Product>> {
        val data = MutableLiveData<List<Product>>()
        disposable.add(
                retrofitClass.getCategoryProduct(context, categoryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { products ->
                            data.setValue(products)
                        }
        )
        return data
    }

    fun getSpecificProductDetail(context: Context, productId: Int): LiveData<ProductDetail> {
        val data = MutableLiveData<ProductDetail>()
        disposable.add(
                retrofitClass.getSpecificProduct(context, productId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { productDetail ->
                            data.setValue(productDetail)
                        }
        )
        return data
    }

    fun getFilteredProducts(context: Context,
                            categoryId: Int,
                            age: Int,
                            intelligenceId: Int,
                            numberOfPlayersId: Int,
                            minPrice: Int,
                            maxPrice: Int,
                            gender: String): LiveData<List<Product>> {
        val data = MutableLiveData<List<Product>>()
        disposable.add(
                retrofitClass.getFilteredProducts(
                        context,
                        categoryId,
                        age,
                        intelligenceId,
                        numberOfPlayersId,
                        minPrice,
                        maxPrice, gender)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { products ->
                            data.setValue(products)
                        }
        )
        return data
    }


    fun addReview(context: Context, addReviewRequest: AddReviewRequest?): LiveData<String> {
        val data = MutableLiveData<String>()
        retrofitClass.addReview(addReviewRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(@NonNull call: Call<ResponseBody>, @NonNull response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    data.value = "OK"
                }
            }

            override fun onFailure(@NonNull call: Call<ResponseBody>, @NonNull t: Throwable) {
                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                data.value = "NOT OK"
            }
        })
        return data
    }

    fun getMyReviews(context: Context): LiveData<List<MyReview>> {
        val data = MutableLiveData<List<MyReview>>()
        disposable.add(
                retrofitClass.getMyReviews(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { reviews ->
                            data.setValue(reviews)
                        })
        return data
    }

    fun getMyCart(context: Context): LiveData<List<Product>> {
        val data = MutableLiveData<List<Product>>()
        disposable.add(
                retrofitClass.getCart(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { products ->
                            data.setValue(products)
                        })
        return data
    }

    fun getMyFavorites(context: Context): LiveData<List<Product>> {
        val data = MutableLiveData<List<Product>>()
        disposable.add(
                retrofitClass.getMyFavourites(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { products ->
                            data.setValue(products)
                        })
        return data
    }

    fun deleteMyCart(context: Context, deleteCartRequest: DeleteCartRequest): LiveData<String> {
        val data = MutableLiveData<String>()
        retrofitClass.deleteCart(deleteCartRequest).enqueue(object : Callback<DeleteCartResponse> {
            override fun onResponse(@NonNull call: Call<DeleteCartResponse>, @NonNull response: Response<DeleteCartResponse>) {
                if (response.isSuccessful) {
                    data.value = "OK"
                }
            }

            override fun onFailure(@NonNull call: Call<DeleteCartResponse>, @NonNull t: Throwable) {
                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                data.value = "NOT OK"
            }
        })
        return data
    }

    fun editMyCart(context: Context, cartRequest: CartRequest): LiveData<String> {
        val data = MutableLiveData<String>()
        retrofitClass.editCart(cartRequest).enqueue(object : Callback<CartResponse> {
            override fun onResponse(@NonNull call: Call<CartResponse>, @NonNull response: Response<CartResponse>) {
                if (response.isSuccessful) {
                    data.value = "OK"
                }
            }

            override fun onFailure(@NonNull call: Call<CartResponse>, @NonNull t: Throwable) {
                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                data.value = "NOT OK"
            }
        })
        return data
    }

    fun changeFavorite(context: Context, favoriteRequest: FavoriteRequest): LiveData<String> {
        val data = MutableLiveData<String>()
        retrofitClass.changeFavorite(favoriteRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(@NonNull call: Call<ResponseBody>, @NonNull response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    data.value = "OK"
                }
            }

            override fun onFailure(@NonNull call: Call<ResponseBody>, @NonNull t: Throwable) {
                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                data.value = "NOT OK"
            }
        })
        return data
    }

    fun getProductReviews(productId: Int): LiveData<List<ProductReview>> {
        val data = MutableLiveData<List<ProductReview>>()
        disposable.add(
                retrofitClass.getProductReviews(productId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { reviews ->
                            data.setValue(reviews)
                        })
        return data
    }

    fun getSearchResults(context: Context, key: String?): LiveData<List<Product>> {
        val data = MutableLiveData<List<Product>>()
        disposable.add(
                retrofitClass.getSearchResults(context, key)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { products ->
                            data.setValue(products)
                        }
        )
        return data
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
