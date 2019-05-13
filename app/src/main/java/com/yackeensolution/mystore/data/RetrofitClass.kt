package com.yackeensolution.mystore.data

import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yackeensolution.mystore.models.*
import com.yackeensolution.mystore.utils.SaveSharedPreference
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClass {

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////
    // Gets :)

    val branches: Observable<List<Branch>>
        get() {
            val storeApi = storeApi
            return storeApi.branches
        }

    val aboutUs: Single<AboutUsResponse>
        get() {
            val storeApi = storeApi
            return storeApi.getAboutUs()
        }

    fun getAllCategories(context: Context): Observable<List<Category>> {
        val storeApi = storeApi
        return storeApi.getAllCategories(SaveSharedPreference.getLanguage(context)!!)
    }

    fun getCategoryProduct(context: Context, categoryId: Int): Observable<List<Product>> {
        val storeApi = storeApi
        return storeApi.getCategoryProducts(
                SaveSharedPreference.getLanguage(context)!!,
                SaveSharedPreference.getUserId(context),
                categoryId)
    }

    fun getSpecificProduct(context: Context, productId: Int): Single<ProductDetail> {
        val storeApi = storeApi
        return storeApi.getSpecificProduct(
                SaveSharedPreference.getLanguage(context)!!,
                SaveSharedPreference.getUserId(context),
                productId
        )
    }

    fun getFilteredProducts(context: Context, categoryId: Int,
                            age: Int,
                            intelligenceId: Int,
                            numberOfPlayersId: Int,
                            minPrice: Int,
                            maxPrice: Int,
                            gender: String): Observable<List<Product>> {
        val storeApi = storeApi
        return storeApi.getFilteredProducts(
                SaveSharedPreference.getUserId(context),
                categoryId,
                age,
                intelligenceId,
                numberOfPlayersId,
                minPrice, maxPrice,
                gender,
                SaveSharedPreference.getLanguage(context)
        )
    }

    fun getNews(context: Context): Observable<List<News>> {
        val storeApi = storeApi
        return storeApi.getNews(SaveSharedPreference.getLanguage(context)!!)
    }

    fun getOffers(context: Context): Observable<List<Offer>> {
        val storeApi = storeApi
        return storeApi.getOffers(SaveSharedPreference.getLanguage(context)!!)
    }

    fun getCart(context: Context): Observable<List<Product>> {
        val storeApi = storeApi
        return storeApi.getCart(SaveSharedPreference.getUserId(context), SaveSharedPreference.getLanguage(context)!!)
    }

    fun getMyFavourites(context: Context): Observable<List<Product>> {
        val storeApi = storeApi
        return storeApi.getMyFavourites(SaveSharedPreference.getUserId(context), SaveSharedPreference.getLanguage(context)!!)
    }

    fun getMyOrders(context: Context): Observable<List<Order>> {
        val storeApi = storeApi
        return storeApi.getMyOrders(SaveSharedPreference.getUserId(context))
    }

    fun getUser(context: Context): Single<UserResponse> {
        val storeApi = storeApi
        return storeApi.getUser(SaveSharedPreference.getUserId(context))
    }

    fun getMyReviews(context: Context): Observable<List<MyReview>> {
        val storeApi = storeApi
        return storeApi.getMyReviews(SaveSharedPreference.getUserId(context), SaveSharedPreference.getLanguage(context))
    }

    fun getOrderDetail(context: Context, orderId: Int): Single<OrderDetailsResponse> {
        val storeApi = storeApi
        return storeApi.getOrderDetails(orderId, SaveSharedPreference.getLanguage(context))
    }

    fun getProductReviews(productId: Int): Observable<List<ProductReview>> {
        val storeApi = storeApi
        return storeApi.getProductReviews(productId)
    }

    fun getSearchResults(context: Context, key: String?): Observable<List<Product>> {
        val storeApi = storeApi
        return storeApi.getSearchResults(
                SaveSharedPreference.getUserId(context),
                SaveSharedPreference.getLanguage(context),
                key)
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////
    // Posts :)

    fun userLogout(logoutRequest: LogoutRequest?): Call<ResponseBody> {
        val storeApi = storeApi
        return storeApi.logout(logoutRequest)
    }

    fun addReview(addReviewRequest: AddReviewRequest?): Call<ResponseBody> {
        val storeApi = storeApi
        return storeApi.addReview(addReviewRequest)
    }

    fun confirmCode(confirmCodeRequest: ConfirmCodeRequest?): Call<UserResponse> {
        val storeApi = storeApi
        return storeApi.confirmCode(confirmCodeRequest)
    }

    fun userLogin(loginRequest: LoginRequest?): Call<UserResponse> {
        val storeApi = storeApi
        return storeApi.login(loginRequest)
    }

    fun contactUs(contactUsRequest: ContactUsRequest?): Call<ResponseBody> {
        val storeApi = storeApi
        return storeApi.contactUs(contactUsRequest)
    }

    fun getPasswordCode(forgetPasswordRequest: ForgetPasswordRequest): Call<ForgetPasswordResponse> {
        val storeApi = storeApi
        return storeApi.getPasswordCode(forgetPasswordRequest)
    }

    fun makeOrder(orderRequest: OrderRequest): Call<Order> {
        val storeApi = storeApi
        return storeApi.makeAnOrder(orderRequest)
    }

    fun createAccount(registrationRequest: RegistrationRequest): Call<UserResponse> {
        val storeApi = storeApi
        return storeApi.createAccount(registrationRequest)
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////
    // Puts :)

    fun editCart(cartRequest: CartRequest): Call<CartResponse> {
        val storeApi = storeApi
        return storeApi.editCart(cartRequest)
    }

    fun changeFavorite(favoriteRequest: FavoriteRequest): Call<ResponseBody> {
        val storeApi = storeApi
        return storeApi.changeFavorite(favoriteRequest)
    }

    fun updateUser(updateUserRequest: UpdateUserRequest): Call<User> {
        val storeApi = storeApi
        return storeApi.updateUser(updateUserRequest)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////
    // Deletes :)
    fun deleteCart(deleteCartRequest: DeleteCartRequest): Call<DeleteCartResponse> {
        val storeApi = storeApi
        return storeApi.deleteCart(deleteCartRequest)
    }

    companion object {

        private const val BASE_URL = "http://yakensolution.cloudapp.net/TalentInternShip/"

        private val retrofitInstance: Retrofit
            get() {

                val gSon = GsonBuilder()
                        .setLenient()
                        .serializeNulls()
                        .create()

                val client = OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .readTimeout(1, TimeUnit.MINUTES)
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .writeTimeout(1, TimeUnit.MINUTES)
                        .build()

                return Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gSon))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(client)
                        .build()
            }

        val storeApi: StoreApi
            get() = retrofitInstance.create(StoreApi::class.java)
    }
}