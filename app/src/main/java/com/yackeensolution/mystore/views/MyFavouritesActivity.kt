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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.SaveSharedPreference
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.adapters.ProductsAdapter
import com.yackeensolution.mystore.data.viewModels.ProductViewModel
import com.yackeensolution.mystore.models.CartRequest
import com.yackeensolution.mystore.models.FavoriteRequest
import com.yackeensolution.mystore.models.Product

class MyFavouritesActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null
    private var noFavourites: View? = null
    private var products: List<Product>? = null
    private val product: Product? = null
    private var productId: Int = 0
    private var removedItemPosition: Int = 0
    private var productsAdapter: ProductsAdapter? = null
    private var productViewModel: ProductViewModel? = null
    private var productRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_my_favourites)

        val mainContainer = findViewById<View>(R.id.my_favourites_main_container)
        Utils.rtlSupport(this, mainContainer)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        progressBar = findViewById(R.id.pb_my_favourites)
        noFavourites = findViewById(R.id.layout_no_favourites)
        val startShopping = findViewById<Button>(R.id.btn_favourites_start_shopping)
        startShopping.setOnClickListener { startActivity(Intent(this@MyFavouritesActivity, MainActivity::class.java)) }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(R.string.my_favourites)

        getMyFavorites()
    }

    private fun getMyFavorites() {
        productViewModel!!.getMyFavorites(this).observe(this, Observer<List<Product>> {
            progressBar!!.visibility = View.GONE
            if (it.isNotEmpty()) {
                products = it
                buildRecyclerView()
            } else {
                noFavourites!!.visibility = View.VISIBLE
            }
        })
    }

    private fun buildRecyclerView() {
        if (products!!.isEmpty()) {
            noFavourites!!.visibility = View.VISIBLE
        } else {
            productRecyclerView = findViewById(R.id.rv_my_favourites)
            productRecyclerView!!.layoutManager = GridLayoutManager(this, 2)
            productRecyclerView!!.setHasFixedSize(true)
            productsAdapter = ProductsAdapter(this)
            productRecyclerView!!.adapter = productsAdapter
            productsAdapter!!.submitList(products)

            productsAdapter!!.setOnItemClickListener(object : ProductsAdapter.OnItemClickListener {
                override fun onItemClick(product: Product) {
                    productId = product.id
                    val productTitle = product.title
                    val oneProductIntent = Intent(this@MyFavouritesActivity, ProductDetailsActivity::class.java)
                    oneProductIntent.putExtra("productId", productId)
                    oneProductIntent.putExtra("productTitle", productTitle)
                    startActivity(oneProductIntent)
                }
            })
            productsAdapter!!.setOnItemFavClickListener(object : ProductsAdapter.OnItemFavClickListener {
                override fun onItemClick(product: Product) {
                    productId = product.id
                    removedItemPosition = products!!.indexOf(product)
                    changeFavorite(false)
                }
            })
            productsAdapter!!.setOnItemCartClickListener(object : ProductsAdapter.OnItemCartClickListener {
                override fun onItemClick(product: Product) {
                    productId = product.id
                    if (product.isInCart) {
                        editCart(false)
                    } else {
                        editCart(true)
                    }
                }
            })
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
