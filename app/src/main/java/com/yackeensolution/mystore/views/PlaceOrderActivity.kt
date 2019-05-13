package com.yackeensolution.mystore.views

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.SaveSharedPreference
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.adapters.ProductsToOrderAdapter
import com.yackeensolution.mystore.data.viewModels.OrdersViewModel
import com.yackeensolution.mystore.data.viewModels.UserViewModel
import com.yackeensolution.mystore.models.*
import java.util.*

class PlaceOrderActivity : AppCompatActivity() {
    private var totalPriceTextView: TextView? = null
    private var discountTextView: TextView? = null
    private var finalPriceTextView: TextView? = null
    private var deliveryAddressesSpinner: Spinner? = null
    private var contactNumbersSpinner: Spinner? = null
    private var confirmOrder: Button? = null
    private var cancelOrder: Button? = null
    private var addContactNumber: Button? = null
    private var addDeliveryAddress: Button? = null
    private var deliveryNotesEditText: EditText? = null
    private var progressBar: ProgressBar? = null
    private var mainScreen: View? = null
    private var deliveryAddress: String = ""
    private var contactNumber: String = ""
    private var deliveryNotes: String = ""
    private var products: List<Product>? = null
    private val contactNumbers = ArrayList<String>()
    private val deliveryAddresses = ArrayList<String>()
    private var totalPrice: Double = 0.toDouble()
    private var discount: Double = 0.toDouble()
    private var finalPrice: Double = 0.toDouble()
    private var userViewModel: UserViewModel? = null
    private var ordersViewModel: OrdersViewModel? = null
    private var ordersRecyclerView: RecyclerView? = null
    private var orderAdapter: ProductsToOrderAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_place_order)

        val mainContainer = findViewById<View>(R.id.order_details_main_container)
        Utils.rtlSupport(this, mainContainer)

        initViews()

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)

        val extras = intent.extras
        if (extras != null) {
            @Suppress("UNCHECKED_CAST")
            products = extras.getSerializable("cartList") as List<Product>
            totalPrice = extras.getDouble("totalPrice")
            discount = extras.getDouble("discount")
            finalPrice = totalPrice - discount
        }

        totalPriceTextView!!.text = totalPrice.toString()
        discountTextView!!.text = discount.toString()
        finalPriceTextView!!.text = finalPrice.toString()

        buildRecyclerView()
        getUser()

        addContactNumber!!.setOnClickListener { showAddInfoDialog(getString(R.string.add_contact_number), getString(R.string.contact_number)) }

        addDeliveryAddress!!.setOnClickListener { showAddInfoDialog(getString(R.string.add_delivery_address), getString(R.string.delivery_address)) }

        cancelOrder!!.setOnClickListener {
            val intent = Intent(this@PlaceOrderActivity, CartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        confirmOrder!!.setOnClickListener {
            if (deliveryNotesEditText!!.text.toString().isNotEmpty()) {
                deliveryNotes = deliveryNotesEditText!!.text.toString().trim { it <= ' ' }
            }
            val currentTime = Calendar.getInstance().time.toString()
            val order = Order()

            order.userId = SaveSharedPreference.getUserId(this)
            order.totalPrice = totalPrice
            order.totalDiscount = discount
            order.finalPrice = finalPrice
            order.deliveryAddress = deliveryAddress
            order.contactNumber = contactNumber
            order.deliveryNotes = deliveryNotes
            order.dateTime = currentTime

            val productsToOrder = ArrayList<ProductToOrder>()
            for (product in products!!) {
                val productToOrder = ProductToOrder(product.id, product.amount)
                productsToOrder.add(productToOrder)
            }
            mainScreen!!.visibility = View.GONE
            progressBar!!.visibility = View.VISIBLE
            placeAnOrder(order, productsToOrder)
        }
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.place_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        totalPriceTextView = findViewById(R.id.tv_ordered_total_before_discount)
        discountTextView = findViewById(R.id.tv_ordered_discount)
        finalPriceTextView = findViewById(R.id.tv_ordered_final_price)
        deliveryAddressesSpinner = findViewById(R.id.spinner_delivery_address)
        contactNumbersSpinner = findViewById(R.id.spinner_contact_number)
        confirmOrder = findViewById(R.id.btn_confirm_order)
        cancelOrder = findViewById(R.id.btn_cancel_order)
        addContactNumber = findViewById(R.id.btn_add_contact_number)
        addDeliveryAddress = findViewById(R.id.btn_add_delivery_address)
        deliveryNotesEditText = findViewById(R.id.et_delivery_notes)
        progressBar = findViewById(R.id.pb_place_order)
        mainScreen = findViewById(R.id.main_screen_place_order)
    }

    private fun buildRecyclerView() {
        ordersRecyclerView = findViewById(R.id.rv_ordered_products)
        ordersRecyclerView!!.layoutManager = LinearLayoutManager(this)
        ordersRecyclerView!!.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ordersRecyclerView!!.addItemDecoration(dividerItemDecoration)
        orderAdapter = ProductsToOrderAdapter(this)
        ordersRecyclerView!!.adapter = orderAdapter
        orderAdapter!!.submitList(products)
    }

    private fun getUser() {
        userViewModel!!.getUser(this).observe(this, Observer<UserResponse> {
            progressBar!!.visibility = View.GONE
            val user: User?
            if (it != null) {
                user = it.user
                if (user != null) {
                    if (user.phone1 != "") {
                        contactNumbers.add(user.phone1)
                    }
                    if (user.phone2 != "") {
                        contactNumbers.add(user.phone2)
                    }
                    if (user.address1 != "") {
                        deliveryAddresses.add(user.address1)
                    }
                    if (user.address2 != "") {
                        deliveryAddresses.add(user.address2)
                    }
                }
                setupSpinners(contactNumbersSpinner!!, contactNumbers)
                setupSpinners(deliveryAddressesSpinner!!, deliveryAddresses)
            } else {
                Toast.makeText(this@PlaceOrderActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSpinners(spinner: Spinner, strings: ArrayList<String>) {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strings)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        setOnItemClicks()
    }

    private fun setOnItemClicks() {
        contactNumbersSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                contactNumber = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        deliveryAddressesSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                deliveryAddress = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun showAddInfoDialog(dialogTitle: String, hint: String) {
        val dialog = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val nullParent: ViewGroup? = null

        val dialogView = inflater.inflate(R.layout.dialog_add_order_info, nullParent)

        val textView = dialogView.findViewById<TextView>(R.id.tv_add_info)
        val editText = dialogView.findViewById<EditText>(R.id.et_add_info)
        val add = dialogView.findViewById<Button>(R.id.btn_add_info)

        textView.text = dialogTitle
        editText.hint = hint
        if (dialogTitle == getString(R.string.add_contact_number)) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        add.setOnClickListener {
            if (dialogTitle == getString(R.string.add_contact_number)) {
                if (Utils.isValidNumber(editText, resources.getString(R.string.enter_valid_phone_number))) {
                    contactNumbers.add(editText.text.toString().trim { it <= ' ' })
                    dialog.dismiss()
                }
            } else if (dialogTitle == getString(R.string.add_delivery_address)) {
                deliveryAddresses.add(editText.text.toString().trim { it <= ' ' })
                dialog.dismiss()
            }
        }

        dialog.setView(dialogView)
        dialog.show()
    }

    private fun placeAnOrder(order: Order, productsToOrder: List<ProductToOrder>) {
        val orderRequest = OrderRequest()
        orderRequest.order = order
        orderRequest.productsToOrder = productsToOrder

        ordersViewModel!!.makeOrder(orderRequest).observe(this, Observer<String> { state ->
            if (state == "OK") {
                Toast.makeText(this@PlaceOrderActivity, getString(R.string.order_has_been_made), Toast.LENGTH_SHORT).show()
                val intent = Intent(this@PlaceOrderActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                Toast.makeText(this@PlaceOrderActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }

        })
    }
}