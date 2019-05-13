package com.yackeensolution.mystore.data

import com.yackeensolution.mystore.models.*

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface StoreApi {


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////
    // Gets :)

    @get:GET("api/app/branches")
    val branches: Observable<List<Branch>>

    @GET("api/category")
    fun getAllCategories(
            @Query("lang") lang: String
    ): Observable<List<Category>>

    @GET("api/category")
    fun getCategories(
            @Query("lang") lang: String?
    ): Call<List<Category>>

    @GET("api/product/all")
    fun getCategoryProducts(
            @Query("lang") lang: String,
            @Query("userId") userId: Int,
            @Query("catgId") categoryId: Int
    ): Observable<List<Product>>

    @GET("api/Products")
    fun getSpecificProduct(
            @Query("lang") lang: String,
            @Query("userId") userId: Int,
            @Query("productId") productId: Int
    ): Single<ProductDetail>

    @GET("api/app/aboutus")
    fun getAboutUs(): Single<AboutUsResponse>

    @GET("api/news")
    fun getNews(
            @Query("lang") language: String
    ): Observable<List<News>>

    @GET("api/offers")
    fun getOffers(
            @Query("lang") language: String
    ): Observable<List<Offer>>

    @GET("api/cart")
    fun getCart(
            @Query("userid") userId: Int,
            @Query("lang") language: String
    ): Observable<List<Product>>


    @GET("api/product/all")
    fun getFilteredProducts(
            @Query("userId") userId: Int,
            @Query("catId") categoryId: Int,
            @Query("age") ageId: Int,
            @Query("intelligenceId") intelligenceId: Int,
            @Query("numberOfPlayerId") numberOfPlayersId: Int,
            @Query("minPrice") minPrice: Int,
            @Query("maxPrice") maxPrice: Int,
            @Query("gender") gender: String,
            @Query("lang") language: String?
    ): Observable<List<Product>>

    @GET("api/review/favourite")
    fun getMyFavourites(
            @Query("userId") userId: Int,
            @Query("lang") language: String
    ): Observable<List<Product>>

    @GET("api/order")
    fun getMyOrders(
            @Query("userId") userId: Int
    ): Observable<List<Order>>

    @GET("api/user/{id}")
    fun getUser(
            @Path("id") userId: Int
    ): Single<UserResponse>

    @GET("api/review/user")
    fun getMyReviews(
            @Query("userId") userId: Int,
            @Query("lang") language: String?
    ): Observable<List<MyReview>>

    @GET("api/order/order-products")
    fun getOrderDetails(
            @Query("orderId") orderId: Int,
            @Query("lang") language: String?
    ): Single<OrderDetailsResponse>

    @GET("api/review/product")
    fun getProductReviews(
            @Query("productid") productId: Int
    ): Observable<List<ProductReview>>

    @GET("api/product/search")
    fun getSearchResults(
            @Query("userId") userId: Int,
            @Query("lang") language: String?,
            @Query("key") key: String?
    ): Observable<List<Product>>

    @get:GET("api/Ages")
    val ages: Call<List<Age>>

    @GET("api/intelligence")
    fun getIntelligence(
            @Query("lang") language: String?
    ): Call<List<Intelligence>>

    @get:GET("api/NumberOfPlayers")
    val numberOfPlayers: Call<List<NumberOfPlayers>>

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////
    // Posts :)

    @POST("api/user/logout")
    fun logout(
            @Body logoutRequest: LogoutRequest?
    ): Call<ResponseBody>

    @POST("api/review")
    fun addReview(
            @Body addReviewRequest: AddReviewRequest?
    ): Call<ResponseBody>

    @POST("api/user/confirm-code")
    fun confirmCode(
            @Body confirmCodeRequest: ConfirmCodeRequest?
    ): Call<UserResponse>

    @POST("api/user/login")
    fun login(
            @Body loginRequest: LoginRequest?
    ): Call<UserResponse>

    @POST("app/contactus")
    fun contactUs(
            @Body contactUsRequest: ContactUsRequest?
    ): Call<ResponseBody>

    @POST("api/user/forget-password")
    fun getPasswordCode(
            @Body forgetPasswordRequest: ForgetPasswordRequest
    ): Call<ForgetPasswordResponse>

    @POST("api/order")
    fun makeAnOrder(
            @Body orderRequest: OrderRequest
    ): Call<Order>

    @POST("api/user/registration")
    fun createAccount(
            @Body registrationRequest: RegistrationRequest
    ): Call<UserResponse>

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////
    // Puts :)

    @PUT("api/cart")
    fun editCart(
            @Body CartRequest: CartRequest
    ): Call<CartResponse>

    @PUT("api/review/favorite")
    fun changeFavorite(
            @Body favoriteRequest: FavoriteRequest
    ): Call<ResponseBody>

    @PUT("api/user/update")
    fun updateUser(
            @Body updateUserRequest: UpdateUserRequest
    ): Call<User>

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////
    // DELETE :)

    @HTTP(method = "DELETE", path = "api/cart", hasBody = true)
    fun deleteCart(
            @Body deleteCartRequest: DeleteCartRequest
    ): Call<DeleteCartResponse>
}