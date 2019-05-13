package com.yackeensolution.mystore.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.google.android.material.textfield.TextInputEditText
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.SaveSharedPreference
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.data.viewModels.CollectionViewModel
import com.yackeensolution.mystore.models.ContactUsRequest

class ContactUsActivity : AppCompatActivity() {
    private var nameEditText: TextInputEditText? = null
    private var messageEditText: TextInputEditText? = null
    private var emailEditText: TextInputEditText? = null
    private var sendButton: Button? = null
    private var name: String = ""
    private var email: String = ""
    private var message: String = ""
    private var collectionViewModel: CollectionViewModel? = null

    private val isValidData: Boolean
        get() {
            if (messageEditText!!.text!!.toString().isEmpty()) {
                messageEditText!!.error = getString(R.string.enter_your_message)
                messageEditText!!.requestFocus()
                return false
            } else {
                message = messageEditText!!.text!!.toString().trim { it <= ' ' }
            }

            return true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_contact_us)

        val mainContainer = findViewById<View>(R.id.contact_us_main_container)
        Utils.rtlSupport(this, mainContainer)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.contact_us)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initViews()

        sendButton!!.setOnClickListener {
            email = emailEditText!!.text!!.toString().trim { it <= ' ' }
            name = nameEditText!!.text!!.toString().trim { it <= ' ' }
            if (isValidData) {
                val contactUsRequest = ContactUsRequest()
                contactUsRequest.userId = SaveSharedPreference.getUserId(this)
                contactUsRequest.name = name
                contactUsRequest.email = email
                contactUsRequest.body = message
                contactUs(contactUsRequest)
            }
        }
    }

    private fun initViews() {
        nameEditText = findViewById(R.id.et_contact_us_name)
        sendButton = findViewById(R.id.btn_contact_us_send)
        messageEditText = findViewById(R.id.et_contact_us_message)
        emailEditText = findViewById(R.id.et_contact_us_email)
    }

    private fun contactUs(request: ContactUsRequest) {

        collectionViewModel = ViewModelProviders.of(this).get(CollectionViewModel::class.java)
        collectionViewModel!!.contactUs(request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                Toast.makeText(this@ContactUsActivity, getString(R.string.message_sent_successfully), Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ContactUsActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            } else {
                Toast.makeText(this@ContactUsActivity, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
