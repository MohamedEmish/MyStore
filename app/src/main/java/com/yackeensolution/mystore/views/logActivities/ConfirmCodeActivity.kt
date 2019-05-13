package com.yackeensolution.mystore.views.logActivities

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
import com.yackeensolution.mystore.data.viewModels.UserViewModel
import com.yackeensolution.mystore.models.ConfirmCodeRequest
import com.yackeensolution.mystore.models.LoginRequest
import com.yackeensolution.mystore.models.User
import com.yackeensolution.mystore.models.UserResponse
import com.yackeensolution.mystore.utils.SaveSharedPreference
import com.yackeensolution.mystore.utils.Utils

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ConfirmCodeActivity : AppCompatActivity() {
    private var confirmationCodeEditText: EditText? = null
    private var newPasswordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var confirm: Button? = null
    private var newPassword: String = ""
    private var confirmationCode: String = ""
    private var email: String = ""
    private var codeFromApi: String = ""
    private var userViewModel: UserViewModel? = null

    private val isValidDate: Boolean
        get() {

            if (!Utils.isValueSet(confirmationCodeEditText, resources.getString(R.string.enter_confirmation_code))) {
                confirmationCodeEditText!!.requestFocus()
                return false
            } else if (confirmationCodeEditText!!.text.toString() != codeFromApi) {
                confirmationCodeEditText!!.error = getString(R.string.enter_the_right_code)
                confirmationCodeEditText!!.requestFocus()
                return false
            } else {
                confirmationCode = confirmationCodeEditText!!.text.toString().trim { it <= ' ' }
            }

            if (!Utils.isValueSet(newPasswordEditText, resources.getString(R.string.enter_your_new_password))) {
                newPasswordEditText!!.requestFocus()
                return false
            } else if (!Utils.isValidPassword(newPasswordEditText, resources.getString(R.string.password_length_warning))) {
                newPasswordEditText!!.requestFocus()
                return false
            } else {
                newPassword = newPasswordEditText!!.text.toString().trim { it <= ' ' }
            }

            if (!Utils.isValueSet(confirmPasswordEditText, resources.getString(R.string.confirm_your_password))) {
                confirmPasswordEditText!!.requestFocus()
                return false
            } else if (confirmPasswordEditText!!.text.toString() != newPassword) {
                confirmPasswordEditText!!.error = getString(R.string.password_does_not_match)
                confirmPasswordEditText!!.requestFocus()
                return false
            }
            return true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_confirm_code)

        val mainContainer = findViewById<View>(R.id.confirm_code_main_container)
        Utils.rtlSupport(this, mainContainer)

        initViews()

        val extras = intent.extras
        if (extras != null) {
            email = extras.getString("email")
        }
        if (extras != null) {
            codeFromApi = extras.getString("code")
        }

        confirm!!.setOnClickListener {
            if (isValidDate) {
                confirmCode()
            }
        }
    }

    private fun initViews() {
        confirmationCodeEditText = findViewById(R.id.et_confirmation_code)
        newPasswordEditText = findViewById(R.id.et_confirm_code_new_password)
        confirmPasswordEditText = findViewById(R.id.et_confirm_code_confirm_password)
        confirm = findViewById(R.id.btn_confirm)
    }

    private fun confirmCode() {
        val request = ConfirmCodeRequest()
        request.email = email
        request.code = confirmationCode
        request.newPassword = newPassword

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel!!.confirmCode(request).observe(this, Observer<String> { state ->
            if (state == "OK") {
                Toast.makeText(this@ConfirmCodeActivity, R.string.password_changed_successfully, Toast.LENGTH_SHORT).show()
                login()
            } else {
                Toast.makeText(this@ConfirmCodeActivity, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun login() {

        val request = LoginRequest()
        request.email = email
        request.password = newPassword

        var user: User?

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel!!.userLogin(request).observe(this, Observer<UserResponse> { userResponse ->
            if (userResponse != null) {
                user = userResponse.user
                if (user != null) {
                    SaveSharedPreference.setUserId(this, user!!.id)
                }
                val intent = Intent(this@ConfirmCodeActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

            } else {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }
}