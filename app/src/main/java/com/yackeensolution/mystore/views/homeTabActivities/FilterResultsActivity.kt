package com.yackeensolution.mystore.views.homeTabActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.adapters.FiltersAdapter
import com.yackeensolution.mystore.adapters.ProductsAdapter
import com.yackeensolution.mystore.data.viewModels.ProductViewModel
import com.yackeensolution.mystore.models.CartRequest
import com.yackeensolution.mystore.models.FavoriteRequest
import com.yackeensolution.mystore.models.Product
import com.yackeensolution.mystore.models.User
import com.yackeensolution.mystore.utils.LoginDialog
import com.yackeensolution.mystore.utils.SaveSharedPreference
import com.yackeensolution.mystore.utils.Utils
import com.yackeensolution.mystore.views.logActivities.MainActivity
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FilterResultsActivity : AppCompatActivity() {
    private var filtersAdapter: FiltersAdapter? = null
    private var selectedFiltersTitles: ArrayList<String>? = ArrayList()
    private var selectedFilters: MutableList<Int>? = ArrayList()
    private var categoryId: Int = 0
    private var ageId: Int = 0
    private var intelligenceId: Int = 0
    private var numberOfPlayersId: Int = 0
    private var mProductId: Int = 0
    private var minPrice: Int = 0
    private var maxPrice: Int = 0
    private var gender: String = ""
    private var noProducts: TextView? = null
    private var productsAdapter: ProductsAdapter? = null
    private var mProducts: List<Product>? = null
    private val mProduct: Product? = null
    private var loadingView: View? = null
    private var filtersRecyclerView: RecyclerView? = null
    private var productViewModel: ProductViewModel? = null
    private var productRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_filter_results)

        val mainContainer = findViewById<View>(R.id.results_main_container)
        Utils.rtlSupport(this, mainContainer)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.results)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        noProducts = findViewById(R.id.no_filtered_products)
        loadingView = findViewById(R.id.loading_view_filter_results)
        filtersRecyclerView = findViewById(R.id.rv_selected_filters)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        getExtras()
        buildSelectedFiltersRecyclerView()
        getFilteredProducts()
    }

    private fun getFilteredProducts() {
        productViewModel!!.getFilteredProducts(this,
                categoryId,
                ageId, intelligenceId, numberOfPlayersId, minPrice, maxPrice,
                gender).observe(this, Observer<List<Product>> { products ->
            if (products.isNotEmpty()) {
                loadingView!!.visibility = View.GONE
                mProducts = products
                buildRecyclerView()
            } else {
                Toast.makeText(this@FilterResultsActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getExtras() {
        val extras = intent.extras
        if (extras != null) {
            selectedFiltersTitles = extras.getStringArrayList("selectedFiltersTitles")
            selectedFilters = extras.getIntegerArrayList("selectedFilters")

            categoryId = extras.getInt("categoryId")
            if (categoryId == 0) {
                categoryId = -1
            }
            ageId = extras.getInt("ageId")
            if (ageId == 0) {
                ageId = -1
            }

            intelligenceId = extras.getInt("intelligenceId")
            if (intelligenceId == 0) {
                intelligenceId = -1
            }
            numberOfPlayersId = extras.getInt("numberOfPlayersId")
            if (numberOfPlayersId == 0) {
                numberOfPlayersId = -1
            }

            gender = extras.getString("gender")
            if (gender == "") {
                gender = User.Gender.both.name
            }

            minPrice = extras.getInt("minPrice")
            maxPrice = extras.getInt("maxPrice")
        }
    }

    private fun buildSelectedFiltersRecyclerView() {
        filtersAdapter = FiltersAdapter(this)
        filtersAdapter!!.setOnDeleteClickListener(object : FiltersAdapter.OnDeleteClickListener {
            override fun onItemClick(position: Int) {
                val removedItem = selectedFilters!![position]
                onRemoveCheck(removedItem)
                getFilteredProducts()
                loadingView!!.visibility = View.VISIBLE
                selectedFiltersTitles!!.removeAt(position)
                selectedFilters!!.remove(position)
                filtersAdapter!!.notifyItemRemoved(position)
                if (selectedFiltersTitles!!.isEmpty()) {
                    filtersRecyclerView!!.visibility = View.GONE
                }
            }
        })
    }

    private fun onRemoveCheck(removedItem: Int) {
        when {
            categoryId == removedItem -> categoryId = -1
            ageId == removedItem -> ageId = -1
            removedItem == 3 -> gender = User.Gender.both.name
            intelligenceId == removedItem -> intelligenceId = -1
            numberOfPlayersId == removedItem -> numberOfPlayersId = -1
            minPrice == removedItem -> minPrice = -1
            maxPrice == removedItem -> maxPrice = -1
        }
    }

    private fun buildRecyclerView() {
        if (mProducts!!.isEmpty()) {
            noProducts!!.visibility = View.VISIBLE
        } else {
            productRecyclerView = findViewById(R.id.rv_filtered_products)
            productRecyclerView!!.layoutManager = GridLayoutManager(this@FilterResultsActivity, 2)
            productRecyclerView!!.setHasFixedSize(true)
            productsAdapter = ProductsAdapter(this)
            productRecyclerView!!.adapter = productsAdapter
            productsAdapter!!.submitList(mProducts)

            productsAdapter!!.setOnItemClickListener(object : ProductsAdapter.OnItemClickListener {
                override fun onItemClick(product: Product) {
                    val productTitle = product.title
                    val oneProductIntent = Intent(this@FilterResultsActivity, ProductDetailsActivity::class.java)
                    oneProductIntent.putExtra("productId", product.id)
                    oneProductIntent.putExtra("productTitle", productTitle)
                    startActivity(oneProductIntent)
                }
            })

            productsAdapter!!.setOnItemCartClickListener(object : ProductsAdapter.OnItemCartClickListener {
                override fun onItemClick(product: Product) {
                    if (SaveSharedPreference.getUserId(this@FilterResultsActivity) != -1) {
                        mProductId = product.id
                        if (product.isInCart) {
                            editCart(false)
                        } else {
                            editCart(true)
                        }
                    } else {
                        val dialog = LoginDialog(this@FilterResultsActivity)
                        dialog.show()
                    }
                }
            })

            productsAdapter!!.setOnItemFavClickListener(object : ProductsAdapter.OnItemFavClickListener {
                override fun onItemClick(product: Product) {
                    if (SaveSharedPreference.getUserId(this@FilterResultsActivity) != -1) {
                        mProductId = product.id
                        if (product.isFav) {
                            changeFavorite(false)
                        } else {
                            changeFavorite(true)
                        }
                    } else {
                        val dialog = LoginDialog(this@FilterResultsActivity)
                        dialog.show()
                    }
                }
            })
        }
    }

    private fun changeFavorite(isFav: Boolean) {
        val request = FavoriteRequest()
        request.userId = SaveSharedPreference.getUserId(this)
        request.productId = mProductId
        request.isFavorite = isFav

        productViewModel!!.changeFavorite(this, request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                mProduct!!.isFav = isFav
                if (isFav) {
                    Toast.makeText(this, R.string.added_to_favourites, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.removed_from_favourites, Toast.LENGTH_SHORT).show()
                }
                productsAdapter!!.notifyDataSetChanged()

            } else {
                Toast.makeText(this@FilterResultsActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editCart(isInCart: Boolean) {
        val request = CartRequest(SaveSharedPreference.getUserId(this), mProductId, isInCart)
        productViewModel!!.editMyCart(this, request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                mProduct!!.isInCart = isInCart
                productsAdapter!!.notifyDataSetChanged()
                if (!isInCart) {
                    Toast.makeText(this, R.string.removed_from_cart, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.added_to_cart, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@FilterResultsActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onResume() {
        super.onResume()
        getFilteredProducts()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@FilterResultsActivity, MainActivity::class.java))
        super.onBackPressed()
    }
}