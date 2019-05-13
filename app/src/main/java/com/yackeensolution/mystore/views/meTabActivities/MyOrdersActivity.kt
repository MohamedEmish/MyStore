package com.yackeensolution.mystore.views.meTabActivities

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.adapters.MyOrdersAdapter
import com.yackeensolution.mystore.data.viewModels.OrdersViewModel
import com.yackeensolution.mystore.models.Order
import com.yackeensolution.mystore.utils.Utils
import com.yackeensolution.mystore.views.homeTabActivities.OrderDetailsActivity
import com.yackeensolution.mystore.views.logActivities.MainActivity

class MyOrdersActivity : AppCompatActivity() {
    private var orders: List<Order>? = null
    private var progressBar: ProgressBar? = null
    private var noOrders: View? = null
    private var startShopping: Button? = null
    private var ordersViewModel: OrdersViewModel? = null
    private var ordersRecyclerView: RecyclerView? = null
    private var orderAdapter: MyOrdersAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_my_orders)

        val mainContainer = findViewById<View>(R.id.my_orders_main_container)
        Utils.rtlSupport(this, mainContainer)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.my_orders)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)

        progressBar = findViewById(R.id.pb_activity_my_orders)
        noOrders = findViewById(R.id.layout_no_orders)
        startShopping = findViewById(R.id.btn_start_shopping_my_orders)

        getMyOrders()
    }

    private fun getMyOrders() {
        ordersViewModel!!.getMyOrders(this).observe(this, Observer<List<Order>> {
            if (it.isEmpty()) {
                progressBar!!.visibility = View.GONE
                noOrders!!.visibility = View.VISIBLE
                startShopping!!.setOnClickListener {
                    val intent = Intent(this@MyOrdersActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            } else {
                orders = it
                progressBar!!.visibility = View.GONE
                buildRecyclerView()
            }
        })
    }

    private fun buildRecyclerView() {
        ordersRecyclerView = findViewById(R.id.rv_my_orders)
        ordersRecyclerView!!.layoutManager = LinearLayoutManager(this)
        ordersRecyclerView!!.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ordersRecyclerView!!.addItemDecoration(dividerItemDecoration)
        orderAdapter = MyOrdersAdapter(this)
        ordersRecyclerView!!.adapter = orderAdapter
        orderAdapter!!.submitList(orders)

        orderAdapter!!.setOnItemClickListener(object : MyOrdersAdapter.OnItemClickListener {
            override fun onItemClick(order: Order) {
                val orderId = order.id
                val intent = Intent(this@MyOrdersActivity, OrderDetailsActivity::class.java)
                intent.putExtra("orderId", orderId)
                startActivity(intent)
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
