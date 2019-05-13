package com.yackeensolution.mystore.views.homeTabActivities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spanned
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.adapters.ImageSliderAdapter
import com.yackeensolution.mystore.data.viewModels.ProductViewModel
import com.yackeensolution.mystore.models.CartRequest
import com.yackeensolution.mystore.models.FavoriteRequest
import com.yackeensolution.mystore.models.ProductDetail
import com.yackeensolution.mystore.utils.LoginDialog
import com.yackeensolution.mystore.utils.SaveSharedPreference
import com.yackeensolution.mystore.utils.Utils
import java.util.*


class ProductDetailsActivity : AppCompatActivity() {

    private var categoryId: Int = -1
    private var productId: Int = -1
    private var productViewModel: ProductViewModel? = null


    private var favouriteImageView: ImageView? = null
    private var youtubeImageView: ImageView? = null
    private var facebookImageView: ImageView? = null
    private var mImages: List<String>? = null
    private var productDetails: ProductDetail? = null
    private var viewPager: ViewPager? = null
    private var addToCart: Button? = null
    private var productRatingBar: RatingBar? = null
    private var productPrice: TextView? = null
    private var productCode: TextView? = null
    private var productDescription: TextView? = null
    private var productReviewsCount: TextView? = null
    private var productFavouritesCount: TextView? = null
    private var outOfStock: TextView? = null
    private var progressBar: ProgressBar? = null
    private var mainScreen: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_product_details)

        val mainContainer = findViewById<View>(R.id.one_toy_main_container)
        Utils.rtlSupport(this, mainContainer)

        val extras = intent.extras
        if (extras != null) {
            categoryId = extras.getInt("categoryId")
            productId = extras.getInt("productId")
            val categoryTitle = extras.getString("productTitle")
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar!!.title = categoryTitle
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        initViews()

        val reviewsLayout = findViewById<LinearLayout>(R.id.linear_layout_reviews)
        reviewsLayout.setOnClickListener {
            val reviewsIntent = Intent(this@ProductDetailsActivity, ProductReviewsActivity::class.java)
            reviewsIntent.putExtra("productId", productId)
            startActivity(reviewsIntent)
        }


        favouriteImageView!!.setOnClickListener {
            if (SaveSharedPreference.getUserId(this) != -1) {
                if (productDetails!!.isFav) {
                    changeFavorite(false)
                } else {
                    changeFavorite(true)
                }
            } else {
                val dialog = LoginDialog(this@ProductDetailsActivity)
                dialog.show()
                Toast.makeText(this@ProductDetailsActivity, "plz login", Toast.LENGTH_SHORT).show()

            }
        }


        val mAddToCartButton = findViewById<View>(R.id.btn_add_to_cart)
        mAddToCartButton.setOnClickListener {
            if (SaveSharedPreference.getUserId(this) != -1) {
                if (productDetails!!.isInCart) {
                    editCart(false)
                } else {
                    editCart(true)
                }
            } else {
                val dialog = LoginDialog(this@ProductDetailsActivity)
                dialog.show()
                Toast.makeText(this@ProductDetailsActivity, "plz login", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun changeFavorite(setFav: Boolean) {
        val request = FavoriteRequest()
        request.userId = SaveSharedPreference.getUserId(this)
        request.productId = productId
        request.isFavorite = setFav

        productViewModel!!.changeFavorite(this, request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                Toast.makeText(this, R.string.added_to_favourites, Toast.LENGTH_SHORT).show()
                productDetails!!.isFav = setFav
                if (!setFav) {
                    favouriteImageView!!.setImageResource(R.drawable.ic_like)
                    Toast.makeText(this@ProductDetailsActivity, R.string.removed_from_favourites, Toast.LENGTH_SHORT).show()
                } else {
                    favouriteImageView!!.setImageResource(R.drawable.ic_liked)
                    Toast.makeText(this@ProductDetailsActivity, R.string.added_to_favourites, Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editCart(setInCart: Boolean) {
        val request = CartRequest(SaveSharedPreference.getUserId(this), productId, setInCart)
        productViewModel!!.editMyCart(this, request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                productDetails!!.isInCart = setInCart
                if (!setInCart) {
                    addToCart!!.text = getString(R.string.add_to_cart)
                    Toast.makeText(this@ProductDetailsActivity, R.string.removed_from_cart, Toast.LENGTH_SHORT).show()
                } else {
                    addToCart!!.text = getString(R.string.remove_from_cart)
                    Toast.makeText(this@ProductDetailsActivity, R.string.added_to_cart, Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initViews() {
        productRatingBar = findViewById(R.id.one_product_rating_bar)
        productCode = findViewById(R.id.tv_product_code)
        productDescription = findViewById(R.id.tv_product_description)
        productPrice = findViewById(R.id.tv_product_price)
        productReviewsCount = findViewById(R.id.tv_product_reviews_count)
        productFavouritesCount = findViewById(R.id.tv_added_to_favourites_count)
        youtubeImageView = findViewById(R.id.iv_product_youtube_link)
        facebookImageView = findViewById(R.id.iv_product_facebook_link)
        progressBar = findViewById(R.id.pb_one_product)
        mainScreen = findViewById(R.id.main_screen_product_details)
        viewPager = findViewById(R.id.viewpager)
        favouriteImageView = findViewById(R.id.iv_favourite)
        addToCart = findViewById(R.id.btn_add_to_cart)
        outOfStock = findViewById(R.id.tv_out_of_stock)
    }

    override fun onResume() {
        super.onResume()
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        setUpData()
    }

    private fun setUpData() {
        let {
            productViewModel!!.getSpecificProductDetail(it, productId).observe(this, Observer<ProductDetail> { details ->
                if (details != null) {
                    productDetails = details
                    setData(details)
                }
            })
        }
    }

    private fun String.toSpanned(): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(this)
        }
    }

    private fun setData(detail: ProductDetail?) {
        if (detail != null) {
            progressBar?.visibility = View.GONE
            mainScreen?.visibility = View.VISIBLE
            addToCart?.visibility = View.VISIBLE
            if (!productDetails!!.isInStock) {
                outOfStock!!.visibility = View.VISIBLE
            }
            productRatingBar!!.rating = detail.rate
            productCode!!.text = detail.code
            productDescription!!.text = detail.description.toSpanned()
            productPrice!!.text = detail.price.toString()
            productReviewsCount!!.text = detail.reviewsCount.toString()
            productFavouritesCount!!.text = detail.favCount.toString()

            mImages = detail.imageUrls
            setupViewPager(detail.imageUrls)


            if (detail.isFav) {
                favouriteImageView!!.setImageResource(R.drawable.ic_liked)
            } else {
                favouriteImageView!!.setImageResource(R.drawable.ic_like)
            }

            if (detail.isInCart) {
                addToCart!!.text = getString(R.string.remove_from_cart)
            } else {
                addToCart!!.text = getString(R.string.add_to_cart)
            }


            youtubeImageView!!.setOnClickListener {
                if (detail.youtubeLink != null && detail.youtubeLink.isNotEmpty()) {
                    val youtubeIntent = Intent(Intent.ACTION_VIEW)
                    youtubeIntent.data = Uri.parse(detail.youtubeLink)
                    startActivity(youtubeIntent)
                } else {
                    Toast.makeText(this@ProductDetailsActivity, getString(R.string.no_youtube_link), Toast.LENGTH_SHORT).show()
                }
            }

            facebookImageView!!.setOnClickListener {
                if (detail.fbLink != null && detail.fbLink.isNotEmpty()) {
                    val facebookIntent = Intent(Intent.ACTION_VIEW)
                    facebookIntent.data = Uri.parse(detail.fbLink)
                    startActivity(facebookIntent)
                } else {
                    Toast.makeText(this@ProductDetailsActivity, getString(R.string.no_facebook_link), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupViewPager(images: List<String>) {

        val adapter = ImageSliderAdapter(this, images)
        viewPager!!.adapter = adapter
        val indicator = findViewById<TabLayout>(R.id.indicator)
        indicator.setupWithViewPager(viewPager, true)

        val handler = Handler()
        val update = Runnable {
            if (viewPager!!.currentItem < mImages!!.size - 1) {
                viewPager!!.currentItem = viewPager!!.currentItem + 1
            } else {
                viewPager!!.currentItem = 0
            }
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 3000, 3000)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}