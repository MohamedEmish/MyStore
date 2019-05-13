package com.yackeensolution.mystore.views.logActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.data.viewModels.UserViewModel
import com.yackeensolution.mystore.models.RegistrationRequest
import com.yackeensolution.mystore.models.User
import com.yackeensolution.mystore.models.UserResponse
import com.yackeensolution.mystore.utils.SaveSharedPreference
import com.yackeensolution.mystore.utils.Utils

class SignUpActivity : AppCompatActivity() {
    private var passwordEditText: EditText? = null
    private var fullNameEditText: EditText? = null
    private var addressEditText: EditText? = null
    private var userNameEditText: EditText? = null
    private var phoneNumberEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var password: String = ""
    private var fullName: String = ""
    private var address: String = ""
    private var userName: String = ""
    private var phoneNumber: String = ""
    private var email: String = ""
    private var selectedGender: String = ""
    private var maleRadioButton: RadioButton? = null
    private var femaleRadioButton: RadioButton? = null
    private var genderRadioGroup: RadioGroup? = null
    private var registerButton: Button? = null
    private var loadingView: View? = null
    private var userViewModel: UserViewModel? = null

    private val isValidData: Boolean
        get() {
            if (!Utils.isValueSet(fullNameEditText, resources.getString(R.string.enter_your_full_name))) {
                fullNameEditText!!.requestFocus()
                return false
            } else {
                fullName = fullNameEditText!!.text.toString().trim { it <= ' ' }
            }


            if (!Utils.isValueSet(userNameEditText, resources.getString(R.string.enter_your_username))) {
                userNameEditText!!.requestFocus()
                return false
            } else {
                userName = userNameEditText!!.text.toString().trim { it <= ' ' }
            }

            if (!Utils.isValueSet(emailEditText, resources.getString(R.string.enter_your_email))) {
                emailEditText!!.requestFocus()
                return false
            } else if (!Utils.isValidEmail(emailEditText, resources.getString(R.string.enter_valid_email_address))) {
                emailEditText!!.requestFocus()
                return false
            } else {
                email = emailEditText!!.text.toString().trim { it <= ' ' }
            }

            if (!Utils.isValueSet(passwordEditText, resources.getString(R.string.enter_your_password))) {
                passwordEditText!!.requestFocus()
                return false
            } else if (!Utils.isValidPassword(passwordEditText, resources.getString(R.string.password_length_warning))) {
                passwordEditText!!.requestFocus()
                return false
            } else {
                password = passwordEditText!!.text.toString().trim { it <= ' ' }
            }

            if (!Utils.isValueSet(confirmPasswordEditText, resources.getString(R.string.confirm_your_password))) {
                confirmPasswordEditText!!.requestFocus()
                return false
            } else if (confirmPasswordEditText!!.text.toString() != password) {
                confirmPasswordEditText!!.error = getString(R.string.password_does_not_match)
                confirmPasswordEditText!!.requestFocus()
                return false
            }

            if (!Utils.isValueSet(phoneNumberEditText, resources.getString(R.string.enter_your_phone_number))) {
                phoneNumberEditText!!.requestFocus()
                return false
            } else if (!Utils.isValidNumber(phoneNumberEditText!!, resources.getString(R.string.enter_valid_phone_number))) {
                phoneNumberEditText!!.requestFocus()
                return false
            } else {
                phoneNumber = phoneNumberEditText!!.text.toString().trim { it <= ' ' }
            }

            if (!Utils.isValueSet(addressEditText, resources.getString(R.string.enter_your_address))) {
                addressEditText!!.requestFocus()
                return false
            } else {
                address = addressEditText!!.text.toString().trim { it <= ' ' }
            }

            if (genderRadioGroup!!.checkedRadioButtonId == -1) {
                Toast.makeText(this@SignUpActivity, getString(R.string.select_your_gender), Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_sign_up)

        val mainContainer = findViewById<View>(R.id.sign_up_main_container)
        Utils.rtlSupport(this, mainContainer)

        initViews()

        val extras = intent.extras
        if (extras != null) {
            fullNameEditText!!.setText(extras.getString("name"))
            emailEditText!!.setText(extras.getString("email"))
        }

        setGenderListener()

        registerButton!!.setOnClickListener {
            if (isValidData) {
                loadingView!!.visibility = View.VISIBLE
                register()
            }
        }
    }

    private fun initViews() {
        passwordEditText = findViewById(R.id.et_sign_up_password)
        maleRadioButton = findViewById(R.id.rb_sign_up_male)
        fullNameEditText = findViewById(R.id.et_sign_up_full_name)
        addressEditText = findViewById(R.id.et_sign_up_address)
        userNameEditText = findViewById(R.id.et_sign_up_user_name)
        femaleRadioButton = findViewById(R.id.rb_sign_up_female)
        genderRadioGroup = findViewById(R.id.rg_sign_up_gender)
        phoneNumberEditText = findViewById(R.id.et_sign_up_mobile_number)
        emailEditText = findViewById(R.id.et_sign_up_email)
        registerButton = findViewById(R.id.btn_register)
        confirmPasswordEditText = findViewById(R.id.et_sign_up_confirm_password)
        loadingView = findViewById(R.id.loading_view_sign_up)
    }

    private fun register() {
        val request = RegistrationRequest()
        request.email = email
        request.password = password
        request.username = userName
        request.name = fullName
        request.phone = phoneNumber
        request.address = address
        request.gender = selectedGender

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        userViewModel!!.createAccount(request).observe(this, Observer<UserResponse> {
            val user: User?
            if (it != null) {
                user = it.user
                if (user != null) {
                    SaveSharedPreference.setUserId(this, user.id)
                }
                Toast.makeText(this@SignUpActivity, getString(R.string.welcome), Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                Toast.makeText(this@SignUpActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setGenderListener() {
        genderRadioGroup!!.setOnCheckedChangeListener { _, _ ->
            if (maleRadioButton!!.isChecked) {
                selectedGender = User.Gender.Male.name
            } else if (femaleRadioButton!!.isChecked) {
                selectedGender = User.Gender.Female.name
            }
        }
    }
}