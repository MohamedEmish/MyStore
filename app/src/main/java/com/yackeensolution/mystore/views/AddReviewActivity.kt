package com.yackeensolution.mystore.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.SaveSharedPreference
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.data.viewModels.ProductViewModel
import com.yackeensolution.mystore.models.AddReviewRequest

class AddReviewActivity : AppCompatActivity() {
    private var reviewTitleEditText: EditText? = null
    private var reviewBodyEditText: EditText? = null
    private var addReview: Button? = null
    private var ratingBar: RatingBar? = null
    private var productId: Int = 0
    private var productViewModel: ProductViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_add_review)

        val mainContainer = findViewById<View>(R.id.add_review_main_container)
        Utils.rtlSupport(this, mainContainer)

        initViews()
        val extras = intent.extras
        if (extras != null) {
            productId = extras.getInt("productId")
        }
        addReview!!.setOnClickListener {

            checkInputs()
        }
    }

    private fun checkInputs() {
        if (!Utils.isValueSet(reviewTitleEditText, resources.getString(R.string.edit_text_error))) {
            reviewTitleEditText!!.requestFocus()
            return
        }
        if (!Utils.isValueSet(reviewBodyEditText, resources.getString(R.string.edit_text_error))) {
            reviewTitleEditText!!.requestFocus()
            return
        }

        val request: AddReviewRequest? = null
        val reviewTitle = reviewTitleEditText!!.text.toString().trim { it <= ' ' }
        val reviewBody = reviewBodyEditText!!.text.toString().trim { it <= ' ' }
        val rate = ratingBar!!.numStars

        request?.productId = productId
        request?.rate = rate
        request?.title = reviewTitle
        request?.userId = SaveSharedPreference.getUserId(this)
        request?.review = reviewBody

        addReview(request)
    }

    private fun addReview(request: AddReviewRequest?) {
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        productViewModel!!.addReview(this, request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                Toast.makeText(this@AddReviewActivity, R.string.thank_you, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AddReviewActivity, MainActivity::class.java))
            } else {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.add_review)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        addReview = findViewById(R.id.btn_add_review)
        reviewBodyEditText = findViewById(R.id.et_add_review_body)
        reviewTitleEditText = findViewById(R.id.et_add_review_title)
        ratingBar = findViewById(R.id.rb_add_review)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
