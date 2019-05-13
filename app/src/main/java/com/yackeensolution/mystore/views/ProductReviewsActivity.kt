package com.yackeensolution.mystore.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yackeensolution.mystore.LoginDialog
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.SaveSharedPreference
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.adapters.ProductReviewsAdapter
import com.yackeensolution.mystore.data.viewModels.ProductViewModel
import com.yackeensolution.mystore.models.ProductReview

class ProductReviewsActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null
    private var productId: Int = 0
    private var noReviews: View? = null
    private var addReview: Button? = null
    private var productViewModel: ProductViewModel? = null
    private var productReviewsRecyclerView: RecyclerView? = null
    private var productReviewsAdapter: ProductReviewsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_product_reviews)

        val mainContainer = findViewById<View>(R.id.product_reviews_main_container)
        Utils.rtlSupport(this, mainContainer)
        initViews()

        val extras = intent.extras
        if (extras != null) {
            productId = extras.getInt("productId")
        }
    }

    override fun onResume() {
        super.onResume()
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        getProductReviews()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.pb_product_reviews)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.product_reviews)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val fab = findViewById<FloatingActionButton>(R.id.fab_product_reviews)
        fab.setOnClickListener {
            if (SaveSharedPreference.getUserId(this) == -1) {
                val dialog = LoginDialog(this)
                dialog.show()
            } else {
                val intent = Intent(this@ProductReviewsActivity, AddReviewActivity::class.java)
                intent.putExtra("productId", productId)
                startActivity(intent)
            }
        }

        noReviews = findViewById(R.id.layout_no_reviews)
        addReview = findViewById(R.id.btn_add_review)
    }

    private fun getProductReviews() {
        productViewModel!!.getProductReviews(productId).observe(this, Observer<List<ProductReview>> {
            if (it.isNotEmpty()) {
                progressBar!!.visibility = View.GONE
                buildRecyclerView(it)
            } else {
                Toast.makeText(this@ProductReviewsActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun buildRecyclerView(productReviews: List<ProductReview>) {
        if (productReviews.isEmpty()) {
            noReviews!!.visibility = View.VISIBLE
            addReview!!.setOnClickListener { startActivity(Intent(this@ProductReviewsActivity, AddReviewActivity::class.java)) }
        } else {
            productReviewsRecyclerView = findViewById(R.id.rv_product_reviews)
            productReviewsRecyclerView!!.layoutManager = LinearLayoutManager(this)
            productReviewsRecyclerView!!.setHasFixedSize(true)
            productReviewsAdapter = ProductReviewsAdapter(this)
            productReviewsRecyclerView!!.adapter = productReviewsAdapter
            productReviewsAdapter!!.submitList(productReviews)
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