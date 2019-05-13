package com.yackeensolution.mystore.views

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.SaveSharedPreference
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.data.viewModels.UserViewModel
import com.yackeensolution.mystore.models.UpdateUserRequest
import com.yackeensolution.mystore.models.User
import com.yackeensolution.mystore.models.UserResponse

class MyProfileActivity : AppCompatActivity() {
    private var emailEditText: EditText? = null
    private var fullNameEditText: EditText? = null
    private var usernameEditText: EditText? = null
    private var phone1EditText: EditText? = null
    private var phone2EditText: EditText? = null
    private var address1EditText: EditText? = null
    private var address2EditText: EditText? = null
    private var maleRadioButton: RadioButton? = null
    private var femaleRadioButton: RadioButton? = null
    private var username: String = ""
    private var name: String = ""
    private var email: String = ""
    private var phone1: String = ""
    private var phone2: String = ""
    private var address1: String = ""
    private var address2: String = ""
    private var gender: String = ""
    private var progressBar: ProgressBar? = null
    private var userViewModel: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_my_profile)

        val mainContainer = findViewById<View>(R.id.my_profile_main_container)
        Utils.rtlSupport(this, mainContainer)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        initViews()
        getUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_my_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_save_edit -> {
                if (!fullNameEditText!!.isEnabled) {
                    item.setTitle(R.string.save)
                    fullNameEditText!!.isEnabled = true
                    usernameEditText!!.isEnabled = true
                    address1EditText!!.isEnabled = true
                    address2EditText!!.isEnabled = true
                    emailEditText!!.isEnabled = true
                    phone1EditText!!.isEnabled = true
                    phone2EditText!!.isEnabled = true
                    maleRadioButton!!.isEnabled = true
                    femaleRadioButton!!.isEnabled = true
                } else {
                    progressBar!!.visibility = View.VISIBLE
                    item.setTitle(R.string.edit)
                    name = fullNameEditText!!.text.toString()
                    fullNameEditText!!.isEnabled = false

                    username = usernameEditText!!.text.toString()
                    usernameEditText!!.isEnabled = false

                    address1 = address1EditText!!.text.toString()
                    address1EditText!!.isEnabled = false

                    address2 = address2EditText!!.text.toString()
                    address2EditText!!.isEnabled = false

                    email = emailEditText!!.text.toString()
                    emailEditText!!.isEnabled = false

                    phone1 = phone1EditText!!.text.toString()
                    phone1EditText!!.isEnabled = false

                    phone2 = phone2EditText!!.text.toString()
                    phone2EditText!!.isEnabled = false

                    gender = if (maleRadioButton!!.isChecked) {
                        User.Gender.Male.name
                    } else {
                        User.Gender.Male.name
                    }
                    maleRadioButton!!.isEnabled = false
                    femaleRadioButton!!.isEnabled = false

                    val user: User? = null
                    user?.email = email
                    user?.name = name
                    user?.username = username
                    user?.phone1 = phone1
                    user?.phone2 = phone2
                    user?.address1 = address1
                    user?.address2 = address2
                    user?.id = SaveSharedPreference.getUserId(this)
                    user?.gender = gender

                    val request = UpdateUserRequest(user)

                    updateUser(request)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.my_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        emailEditText = findViewById(R.id.et_sign_up_email)
        maleRadioButton = findViewById(R.id.rb_male)
        phone2EditText = findViewById(R.id.et_mobile_number_2)
        femaleRadioButton = findViewById(R.id.rb_female)
        phone1EditText = findViewById(R.id.et_sign_up_mobile_number)
        address1EditText = findViewById(R.id.et_sign_up_address)
        fullNameEditText = findViewById(R.id.et_sign_up_full_name)
        usernameEditText = findViewById(R.id.et_sign_up_user_name)
        address2EditText = findViewById(R.id.et_address_2)
        progressBar = findViewById(R.id.pb_my_profile)
    }

    private fun getUser() {
        userViewModel!!.getUser(this).observe(this, Observer<UserResponse> {
            progressBar!!.visibility = View.GONE
            val user: User?
            if (it != null) {
                user = it.user
                if (user != null) assignEditTextsData(user)
            }
        })
    }

    private fun assignEditTextsData(user: User) {
        email = user.email
        if (email != "") {
            emailEditText!!.setText(email)
        }

        name = user.name
        if (name != "") {
            fullNameEditText!!.setText(name)
        }

        username = user.username
        if (username != "") {
            usernameEditText!!.setText(username)
        }

        phone1 = user.phone1
        if (phone1 != "") {
            phone1EditText!!.setText(phone1)
        }

        phone2 = user.phone2
        if (phone2 != "") {
            phone2EditText!!.setText(phone2)
        }

        address1 = user.address1
        if (address1 != "") {
            address1EditText!!.setText(address1)
        }

        address2 = user.address2
        if (address2 != "") {
            address2EditText!!.setText(address2)
        }

        gender = user.gender
        if (gender == "1") {
            maleRadioButton!!.isChecked = true
        } else if (gender == "2") {
            femaleRadioButton!!.isChecked = true
        }
    }

    private fun updateUser(request: UpdateUserRequest) {
        userViewModel!!.updateUser(request).observe(this, Observer<User> {
            if (it != null) {
                Toast.makeText(this@MyProfileActivity, R.string.information_updated, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MyProfileActivity, MainActivity::class.java))
            } else {
                Toast.makeText(this@MyProfileActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
