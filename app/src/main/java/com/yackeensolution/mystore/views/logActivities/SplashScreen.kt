package com.yackeensolution.mystore.views.logActivities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler

import androidx.appcompat.app.AppCompatActivity
import com.yackeensolution.mystore.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach_screen)
        Handler().postDelayed({
            if (isNetworkAvailable()) {
                val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            } else {
                val mainIntent = Intent(this@SplashScreen, NoInternetConnection::class.java)
                startActivity(mainIntent)
                finish()
            }

        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    companion object {

        private const val SPLASH_DISPLAY_LENGTH = 5000
    }
}
