package com.yackeensolution.mystore.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar


import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.adapters.MyReviewsAdapter
import com.yackeensolution.mystore.data.viewModels.ProductViewModel
import com.yackeensolution.mystore.models.MyReview

class MyReviewsActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null
    private var noReviews: View? = null
    private var startShopping: Button? = null
    private var productViewModel: ProductViewModel? = null
    private var reviewsRecyclerView: RecyclerView? = null
    private var reviewsAdapter: MyReviewsAdapter? = null
    private var myReviewList: MutableList<MyReview>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_my_reviews)

        val mainContainer = findViewById<View>(R.id.my_reviews_main_container)
        Utils.rtlSupport(this, mainContainer)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        initViews()
        getMyReviews()
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.my_reviews)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        progressBar = findViewById(R.id.pb_my_reviews)
        noReviews = findViewById(R.id.layout_no_reviews)
        startShopping = findViewById(R.id.btn_reviews_start_shopping)
    }

    private fun getMyReviews() {
        productViewModel!!.getMyReviews(this).observe(this, Observer<List<MyReview>> { reviews ->
            reviews.forEach {
                myReviewList!!.add(it)
            }
            progressBar!!.visibility = View.GONE
            buildRecyclerView(myReviewList)
        })
    }

    private fun buildRecyclerView(myReviews: MutableList<MyReview>?) {
        if (myReviews!!.isEmpty()) {
            noReviews!!.visibility = View.VISIBLE
            startShopping!!.setOnClickListener {
                val intent = Intent(this@MyReviewsActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                this@MyReviewsActivity.startActivity(intent)
            }
        } else {
            reviewsRecyclerView = findViewById(R.id.rv_my_reviews)
            reviewsRecyclerView!!.layoutManager = LinearLayoutManager(this)
            reviewsRecyclerView!!.setHasFixedSize(true)
            reviewsAdapter = MyReviewsAdapter(this)
            reviewsRecyclerView!!.adapter = reviewsAdapter
            reviewsAdapter!!.submitList(myReviews)
        }

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