package com.yackeensolution.mystore.views.aboutUsTabActivities

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.utils.Utils

class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_about_us)

        val mainContainer = findViewById<View>(R.id.about_us_main_container)
        Utils.rtlSupport(this, mainContainer)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.about_us)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val descriptionTextView = findViewById<TextView>(R.id.tv_about_us)

        val extras = intent.extras
        var description: String? = null
        if (extras != null) {
            description = extras.getString("description")
        }
        descriptionTextView.text = description!!.toSpanned()
    }

    private fun String.toSpanned(): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}