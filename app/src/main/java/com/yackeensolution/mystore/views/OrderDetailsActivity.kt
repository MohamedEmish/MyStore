package com.yackeensolution.mystore.views

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.adapters.ProductsToOrderAdapter
import com.yackeensolution.mystore.data.viewModels.OrdersViewModel
import com.yackeensolution.mystore.models.Order
import com.yackeensolution.mystore.models.OrderDetailsResponse
import com.yackeensolution.mystore.models.Product

class OrderDetailsActivity : AppCompatActivity() {
    private var totalPriceTextView: TextView? = null
    private var discountTextView: TextView? = null
    private var finalPriceTextView: TextView? = null
    private var contactNumber: TextView? = null
    private var deliveryAddress: TextView? = null
    private var deliveryNotes: TextView? = null
    private var deliveryNotesTitle: TextView? = null
    private var orderedProducts: List<Product>? = null
    private var orderId: Int = 0
    private var activityScreen: View? = null
    private var progressBar: ProgressBar? = null
    private var orderViewModel: OrdersViewModel? = null
    private var ordersRecyclerView: RecyclerView? = null
    private var orderAdapter: ProductsToOrderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_order_details)

        val mainContainer = findViewById<View>(R.id.order_details_main_container)
        Utils.rtlSupport(this, mainContainer)

        orderViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        initViews()

        val extras = intent.extras
        if (extras != null) {
            orderId = extras.getInt("orderId")
        }
        getOrderDetails()
    }

    private fun getOrderDetails() {
        orderViewModel!!.getOrderDetail(this, orderId).observe(this, Observer<OrderDetailsResponse> {
            val order: Order?
            if (it != null) {
                progressBar!!.visibility = View.GONE
                activityScreen!!.visibility = View.VISIBLE
                orderedProducts = it.products
                buildRecyclerView()
                order = it.order
                if (order != null) {
                    totalPriceTextView!!.text = order.totalPrice.toString()
                    discountTextView!!.text = order.totalDiscount.toString()
                    finalPriceTextView!!.text = order.finalPrice.toString()
                    contactNumber!!.text = order.contactNumber
                    deliveryAddress!!.text = order.deliveryAddress
                    if (order.deliveryNotes == "") {
                        deliveryNotesTitle!!.visibility = View.GONE
                    } else {
                        deliveryNotes!!.text = order.deliveryNotes
                    }
                }
            }
        })
    }

    private fun buildRecyclerView() {
        ordersRecyclerView = findViewById(R.id.rv_ordered_products)
        ordersRecyclerView!!.layoutManager = LinearLayoutManager(this)
        ordersRecyclerView!!.setHasFixedSize(true)
        ordersRecyclerView!!.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ordersRecyclerView!!.addItemDecoration(dividerItemDecoration)
        orderAdapter = ProductsToOrderAdapter(this)
        ordersRecyclerView!!.adapter = orderAdapter
        orderAdapter!!.submitList(orderedProducts)
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.order_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        totalPriceTextView = findViewById(R.id.tv_ordered_total_before_discount)
        discountTextView = findViewById(R.id.tv_ordered_discount)
        finalPriceTextView = findViewById(R.id.tv_ordered_final_price)
        contactNumber = findViewById(R.id.tv_ordered_contact_number)
        deliveryAddress = findViewById(R.id.tv_ordered_delivery_address)
        deliveryNotes = findViewById(R.id.tv_ordered_delivery_notes)
        deliveryNotesTitle = findViewById(R.id.tv_ordered_delivery_notes_title)
        progressBar = findViewById(R.id.pb_order_details)
        activityScreen = findViewById(R.id.activity_screen_order_details)
    }
}
