package com.yackeensolution.mystore.data.viewModels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yackeensolution.mystore.data.RetrofitClass
import com.yackeensolution.mystore.models.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val retrofitClass = RetrofitClass()
    private val disposable = CompositeDisposable()

    fun getUser(context: Context): LiveData<UserResponse> {
        val data = MutableLiveData<UserResponse>()
        disposable.add(
                retrofitClass.getUser(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { userResponse ->
                            data.setValue(userResponse)
                        })
        return data
    }

    fun userLogout(logoutRequest: LogoutRequest?): LiveData<String> {
        val data = MutableLiveData<String>()
        retrofitClass.userLogout(logoutRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(@NonNull call: Call<ResponseBody>, @NonNull response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    data.value = "OK"
                }
            }

            override fun onFailure(@NonNull call: Call<ResponseBody>, @NonNull t: Throwable) {
                data.value = "NOT OK"
            }
        })
        return data
    }

    fun updateUser(context: Context, updateUserRequest: UpdateUserRequest): LiveData<String> {
        val data = MutableLiveData<String>()
        retrofitClass.updateUser(updateUserRequest).enqueue(object : Callback<User> {
            override fun onResponse(@NonNull call: Call<User>, @NonNull response: Response<User>) {
                if (response.isSuccessful) {
                    data.value = "Ok"
                    Toast.makeText(context, "response.isSuccessful", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(@NonNull call: Call<User>, @NonNull t: Throwable) {
                data.value = "NOT OK"
            }
        })
        return data
    }

    fun userLogin(loginRequest: LoginRequest?): LiveData<UserResponse> {
        val data = MutableLiveData<UserResponse>()
        retrofitClass.userLogin(loginRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(@NonNull call: Call<UserResponse>, @NonNull response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                }
            }

            override fun onFailure(@NonNull call: Call<UserResponse>, @NonNull t: Throwable) {
                data.value = null
            }
        })
        return data
    }

    fun getPasswordCode(forgetPasswordRequest: ForgetPasswordRequest): LiveData<ForgetPasswordResponse> {
        val data = MutableLiveData<ForgetPasswordResponse>()
        retrofitClass.getPasswordCode(forgetPasswordRequest).enqueue(object : Callback<ForgetPasswordResponse> {
            override fun onResponse(@NonNull call: Call<ForgetPasswordResponse>, @NonNull response: Response<ForgetPasswordResponse>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                }
            }

            override fun onFailure(@NonNull call: Call<ForgetPasswordResponse>, @NonNull t: Throwable) {
                data.value = null
            }
        })
        return data
    }

    fun createAccount(registrationRequest: RegistrationRequest): LiveData<UserResponse> {
        val data = MutableLiveData<UserResponse>()
        retrofitClass.createAccount(registrationRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(@NonNull call: Call<UserResponse>, @NonNull response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                }
            }

            override fun onFailure(@NonNull call: Call<UserResponse>, @NonNull t: Throwable) {
                data.value = null
            }
        })
        return data
    }

    fun confirmCode(confirmCodeRequest: ConfirmCodeRequest?): LiveData<String> {
        val data = MutableLiveData<String>()
        retrofitClass.confirmCode(confirmCodeRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(@NonNull call: Call<UserResponse>, @NonNull response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    data.value = "OK"
                }
            }

            override fun onFailure(@NonNull call: Call<UserResponse>, @NonNull t: Throwable) {
                data.value = "NOT OK"
            }
        })
        return data
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}