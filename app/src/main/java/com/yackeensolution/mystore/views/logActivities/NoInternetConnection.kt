package com.yackeensolution.mystore.views.logActivities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yackeensolution.mystore.R

class NoInternetConnection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet_connection)

        val tryAgain: TextView = findViewById(R.id.try_again)
        tryAgain.setOnClickListener {
            val mainIntent = Intent(this@NoInternetConnection, SplashScreen::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}
