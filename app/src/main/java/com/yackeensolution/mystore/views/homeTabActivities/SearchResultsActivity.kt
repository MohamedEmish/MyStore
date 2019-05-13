package com.yackeensolution.mystore.views.homeTabActivities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.adapters.ProductsAdapter
import com.yackeensolution.mystore.data.viewModels.ProductViewModel
import com.yackeensolution.mystore.models.CartRequest
import com.yackeensolution.mystore.models.FavoriteRequest
import com.yackeensolution.mystore.models.Product
import com.yackeensolution.mystore.utils.LoginDialog
import com.yackeensolution.mystore.utils.SaveSharedPreference
import com.yackeensolution.mystore.utils.Utils


class SearchResultsActivity : AppCompatActivity() {
    private var productId: Int = 0
    private var key: String? = null
    private var productsAdapter: ProductsAdapter? = null
    private var products: List<Product>? = null
    private var progressBar: ProgressBar? = null
    private val product: Product? = null
    private var productViewModel: ProductViewModel? = null
    private var productsRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_search_results)

        val mainContainer = findViewById<View>(R.id.search_results_main_container)
        Utils.rtlSupport(this, mainContainer)

        initViews()

        val extras = intent.extras
        if (extras != null) {
            key = extras.getString("key")
        }
    }

    private fun getSearchResults() {
        productViewModel!!.getSearchResults(this, key).observe(this, Observer<List<Product>> {
            if (it.isEmpty()) {
                Toast.makeText(this@SearchResultsActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            } else {
                progressBar!!.visibility = View.GONE
                products = it
                buildRecyclerView()
            }
        })
    }

    private fun buildRecyclerView() {
        productsRecyclerView = findViewById(R.id.rv_search_results)
        productsRecyclerView!!.setHasFixedSize(true)
        productsRecyclerView!!.layoutManager = GridLayoutManager(this, 2)
        productsAdapter = ProductsAdapter(this)
        productsRecyclerView!!.adapter = productsAdapter
        productsAdapter!!.submitList(products)

        productsAdapter!!.setOnItemClickListener(object : ProductsAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                productId = product.id
                val productTitle = product.title
                val oneProductIntent = Intent(this@SearchResultsActivity, ProductDetailsActivity::class.java)
                oneProductIntent.putExtra("productId", productId)
                oneProductIntent.putExtra("productTitle", productTitle)
                startActivity(oneProductIntent)
            }
        })
        productsAdapter!!.setOnItemCartClickListener(object : ProductsAdapter.OnItemCartClickListener {
            override fun onItemClick(product: Product) {
                if (SaveSharedPreference.getUserId(this@SearchResultsActivity) != -1) {
                    productId = product.id
                    if (product.isInCart) {
                        editCart(false)
                    } else {
                        editCart(true)
                    }
                } else {
                    val dialog = LoginDialog(this@SearchResultsActivity)
                    dialog.show()
                }
            }
        })
        productsAdapter!!.setOnItemFavClickListener(object : ProductsAdapter.OnItemFavClickListener {
            override fun onItemClick(product: Product) {
                if (SaveSharedPreference.getUserId(this@SearchResultsActivity) != -1) {
                    productId = product.id
                    if (product.isFav) {
                        changeFavorite(false)
                    } else {
                        changeFavorite(true)
                    }
                } else {
                    val dialog = LoginDialog(this@SearchResultsActivity)
                    dialog.show()
                }
            }
        })

    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(R.string.results)
        progressBar = findViewById(R.id.pb_search_results_activity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun changeFavorite(setFav: Boolean) {
        val request = FavoriteRequest()
        request.userId = SaveSharedPreference.getUserId(this)
        request.productId = productId
        request.isFavorite = setFav

        productViewModel!!.changeFavorite(this, request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                product!!.isFav = setFav
                if (setFav) {
                    Toast.makeText(this, R.string.added_to_favourites, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.removed_from_favourites, Toast.LENGTH_SHORT).show()
                }
                productsAdapter!!.notifyDataSetChanged()

            } else {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editCart(setInCart: Boolean) {
        val request = CartRequest(SaveSharedPreference.getUserId(this), productId, setInCart)
        productViewModel!!.editMyCart(this, request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                product!!.isInCart = setInCart
                productsAdapter!!.notifyDataSetChanged()
                if (!setInCart) {
                    Toast.makeText(this, R.string.removed_from_cart, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.added_to_cart, Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onResume() {
        super.onResume()
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        getSearchResults()
    }
}