package com.yackeensolution.mystore.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.data.viewModels.UserViewModel
import com.yackeensolution.mystore.models.ForgetPasswordRequest
import com.yackeensolution.mystore.models.ForgetPasswordResponse

class ForgetPasswordActivity : AppCompatActivity() {
    private var emailEditText: EditText? = null
    private var resetPasswordButton: Button? = null
    private var email: String = ""
    private var userViewModel: UserViewModel? = null

    private val isValidData: Boolean
        get() {
            if (!Utils.isValueSet(emailEditText, resources.getString(R.string.enter_your_email))) {
                emailEditText!!.requestFocus()
                return false
            } else if (!Utils.isValidEmail(emailEditText, resources.getString(R.string.enter_valid_email_address))) {
                emailEditText!!.requestFocus()
                return false
            } else {
                email = emailEditText!!.text.toString().trim { it <= ' ' }
            }
            return true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_forget_password)

        val mainContainer = findViewById<View>(R.id.forget_password_main_container)
        Utils.rtlSupport(this, mainContainer)

        initViews()
        resetPasswordButton!!.setOnClickListener {
            if (isValidData) {
                resetPassword()
            }
        }
    }

    private fun initViews() {
        emailEditText = findViewById(R.id.et_forget_password_email)
        resetPasswordButton = findViewById(R.id.btn_reset_password)
    }

    private fun resetPassword() {
        val forgetPasswordRequest = ForgetPasswordRequest()
        forgetPasswordRequest.email = email
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel!!.getPasswordCode(forgetPasswordRequest).observe(this, Observer<ForgetPasswordResponse> { response ->
            if (response != null) {
                email = response.email
                val code = response.code
                val intent = Intent(this@ForgetPasswordActivity, ConfirmCodeActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("code", code)
                startActivity(intent)
            } else {
                Toast.makeText(this@ForgetPasswordActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }
}