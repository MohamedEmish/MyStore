package com.yackeensolution.mystore.views.homeTabActivities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.adapters.CartAdapter
import com.yackeensolution.mystore.data.viewModels.ProductViewModel
import com.yackeensolution.mystore.models.CartRequest
import com.yackeensolution.mystore.models.DeleteCartRequest
import com.yackeensolution.mystore.models.Product
import com.yackeensolution.mystore.utils.SaveSharedPreference
import com.yackeensolution.mystore.utils.Utils
import com.yackeensolution.mystore.views.logActivities.MainActivity
import java.io.Serializable

class CartActivity : AppCompatActivity() {

    private var mBottomBar: View? = null
    private var progressBar: View? = null
    private var totalPriceTextView: TextView? = null
    private var totalPrice = 0.0
    private var discount = 0.0
    private var startShopping: Button? = null
    private var placeOrder: Button? = null
    private var emptyCart: View? = null
    private var cartAdapter: CartAdapter? = null
    private var mAmount: Int = 0
    private var productId: Int = 0
    private var removedItemPosition: Int = 0
    private var mProducts: MutableList<Product> = ArrayList()
    private var productViewModel: ProductViewModel? = null
    private var cartRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_cart)

        val mainContainer = findViewById<View>(R.id.cart_main_container)
        Utils.rtlSupport(this, mainContainer)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        initViews()
        getMyCart()

        startShopping!!.setOnClickListener { startActivity(Intent(this@CartActivity, MainActivity::class.java)) }
        placeOrder!!.setOnClickListener {
            val intent = Intent(this@CartActivity, PlaceOrderActivity::class.java)
            val extras = Bundle()
            extras.putSerializable("cartList", mProducts as Serializable?)
            extras.putDouble("totalPrice", totalPrice)
            extras.putDouble("discount", discount)
            intent.putExtras(extras)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> {
                if (cartAdapter!!.itemCount != 0) {
                    deleteCart()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.cart)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mBottomBar = findViewById(R.id.cart_bottom_bar)
        progressBar = findViewById(R.id.pb_activity_cart)
        totalPriceTextView = findViewById(R.id.tv_total_price)
        emptyCart = findViewById(R.id.layout_empty_cart)
        startShopping = findViewById(R.id.btn_start_shopping)
        placeOrder = findViewById(R.id.btn_place_order)
    }

    private fun getMyCart() {
        productViewModel!!.getMyCart(this).observe(this, Observer<List<Product>> { products ->
            progressBar?.visibility = View.GONE
            mBottomBar?.visibility = View.VISIBLE
            for (product in products) {
                totalPrice += product.price
                discount += product.discount
                mProducts.add(product)
            }
            totalPriceTextView!!.text = totalPrice.toString()
            buildRecyclerView(mProducts)
        })
    }

    private fun buildRecyclerView(products: MutableList<Product>?) {
        if (products!!.isEmpty()) {
            emptyCart!!.visibility = View.VISIBLE
            mBottomBar!!.visibility = View.GONE
        } else {
            cartRecyclerView = findViewById(R.id.rv_cart)
            cartRecyclerView!!.layoutManager = LinearLayoutManager(this)
            cartRecyclerView!!.setHasFixedSize(true)
            cartAdapter = CartAdapter(this)
            cartRecyclerView!!.adapter = cartAdapter

            cartAdapter!!.submitList(products)

            cartAdapter!!.setOnItemAddClickListener(object : CartAdapter.OnItemAddClickListener {
                override fun onItemClick(product: Product) {
                    mAmount = product.amount
                    mAmount++
                    product.amount = mAmount
                    cartAdapter!!.notifyDataSetChanged()
                    totalPrice += product.price.toDouble()
                    totalPriceTextView!!.text = totalPrice.toString()
                }
            })

            cartAdapter!!.setOnItemMinusClickListener(object : CartAdapter.OnItemMinusClickListener {
                override fun onItemClick(product: Product) {
                    mAmount = product.amount
                    if (mAmount > 1) {
                        mAmount--
                        product.amount = mAmount
                        cartAdapter!!.notifyDataSetChanged()
                        totalPrice -= product.price.toDouble()
                        totalPriceTextView!!.text = totalPrice.toString()
                    }
                }
            })

            cartAdapter!!.setOnItemDeleteClickListener(object : CartAdapter.OnItemDeleteClickListener {
                override fun onItemClick(product: Product) {
                    removedItemPosition = products.indexOf(product)
                    productId = product.id
                    removeFromCart(product)
                }
            })
        }

    }

    private fun deleteCart() {
        val request = DeleteCartRequest(SaveSharedPreference.getUserId(this))
        productViewModel!!.deleteMyCart(this, request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                mProducts.clear()
                cartAdapter!!.notifyItemRangeRemoved(0, mProducts.size)
                mBottomBar!!.visibility = View.GONE
                emptyCart!!.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show()

            }
        })
    }

    private fun removeFromCart(product: Product) {
        val request = CartRequest(SaveSharedPreference.getUserId(this), productId, false)
        productViewModel!!.editMyCart(this, request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                product.isInCart = false
                totalPrice -= product.price * product.amount
                discount -= product.discount * product.amount
                mProducts.removeAt(removedItemPosition)
                cartAdapter!!.notifyItemRemoved(removedItemPosition)
                totalPriceTextView!!.text = totalPrice.toString()
                if (mProducts.isEmpty()) {
                    mBottomBar!!.visibility = View.GONE
                    emptyCart!!.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(this@CartActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }
}