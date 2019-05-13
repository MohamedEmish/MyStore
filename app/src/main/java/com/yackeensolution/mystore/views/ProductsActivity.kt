package com.yackeensolution.mystore.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.adapters.ProductsAdapter
import com.yackeensolution.mystore.data.viewModels.ProductViewModel
import com.yackeensolution.mystore.models.Product
import java.util.*

class ProductsActivity : AppCompatActivity() {

    private var productRecyclerView: RecyclerView? = null
    private var productsAdapter: ProductsAdapter? = null
    private var productViewModel: ProductViewModel? = null
    private var progressBar: ProgressBar? = null
    private var productList = ArrayList<Product>()
    private var categoryId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utils.setLocale(this)
        setContentView(R.layout.activity_products)

        val mainContainer = findViewById<View>(R.id.toys_main_container)
        Utils.rtlSupport(this, mainContainer)

        val extras = intent.extras
        if (extras != null) {
            categoryId = extras.getInt("categoryId")
            val categoryTitle = extras.getString("categoryTitle")
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar!!.title = categoryTitle
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        progressBar = findViewById(R.id.pb_products_activity)

        productRecyclerView = findViewById(R.id.rv_products)
        productRecyclerView!!.layoutManager = GridLayoutManager(this, 2)
        productRecyclerView!!.setHasFixedSize(true)
        productsAdapter = ProductsAdapter(this)
        productsAdapter!!.submitList(productList)
        productRecyclerView!!.adapter = productsAdapter
        productsAdapter!!.setOnItemClickListener(object : ProductsAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                val oneProductIntent = Intent(this@ProductsActivity, ProductDetailsActivity::class.java)
                oneProductIntent.putExtra("productId", product.id)
                oneProductIntent.putExtra("productTitle", product.title)
                oneProductIntent.putExtra("categoryId", categoryId)
                startActivity(oneProductIntent)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        setUpData()
    }

    private fun setUpData() {
        let {
            productViewModel!!.getCategoryProducts(it, categoryId).observe(this, Observer<List<Product>> { products ->
                if (products.isNotEmpty()) {
                    setData(products)
                }
            })
        }
    }

    private fun setData(products: List<Product>?) {
        if (products != null) {
            if (products.isNotEmpty()) {
                progressBar?.visibility = View.GONE
                productList.clear()
                products.forEach {
                    productList.add(it)
                    productsAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }
}