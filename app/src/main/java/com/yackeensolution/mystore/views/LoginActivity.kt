package com.yackeensolution.mystore.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.SaveSharedPreference
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.data.viewModels.UserViewModel
import com.yackeensolution.mystore.models.LoginRequest
import com.yackeensolution.mystore.models.User
import com.yackeensolution.mystore.models.UserResponse
import org.json.JSONException


class LoginActivity : AppCompatActivity() {

    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var createAccount: TextView? = null
    private var forgetPassword: TextView? = null
    private var skip: TextView? = null
    private var email: String = ""
    private var password: String = ""
    private var loginButton: Button? = null
    private var fb: Button? = null
    private var googleButton: Button? = null
    private var facebookLoginButton: LoginButton? = null
    private var callbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var loadingView: View? = null
    private var userViewModel: UserViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_login)

        val mainContainer = findViewById<View>(R.id.login_main_container)
        Utils.rtlSupport(this, mainContainer)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        initViews()

        handleFacebookLogin()
        handleGoogleLogin()

        createAccount!!.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
        forgetPassword!!.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java))
        }

        skip!!.setOnClickListener { startActivity(Intent(this@LoginActivity, MainActivity::class.java)) }

        loginButton!!.setOnClickListener {
            if (isValidData) {
                loadingView!!.visibility = View.VISIBLE
                login()
            }
        }
    }

    private fun login() {
        val loginRequest = LoginRequest()
        loginRequest.email = email
        loginRequest.password = password
        userViewModel!!.userLogin(loginRequest).observe(this, Observer<UserResponse> {
            val user: User?
            if (it != null) {
                user = it.user
                if (user != null) {
                    SaveSharedPreference.setUserId(this, user.id)
                }
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                Toast.makeText(this@LoginActivity, R.string.welcome, Toast.LENGTH_SHORT).show()
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                loadingView!!.visibility = View.GONE
                Toast.makeText(this@LoginActivity, R.string.check_your_credentials, Toast.LENGTH_SHORT).show()
            }

        })

    }

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

            if (!Utils.isValueSet(passwordEditText, resources.getString(R.string.enter_your_password))) {
                passwordEditText!!.requestFocus()
                return false
            } else if (Utils.isValidPassword(passwordEditText, resources.getString(R.string.password_length_warning))) {
                passwordEditText!!.requestFocus()
                return false
            } else {
                password = passwordEditText!!.text.toString().trim { it <= ' ' }
            }

            return true
        }


    private fun initViews() {
        createAccount = findViewById(R.id.tv_create_account)
        emailEditText = findViewById(R.id.et_sign_up_email)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.btn_login)
        forgetPassword = findViewById(R.id.tv_cart_title)
        fb = findViewById(R.id.fb)
        googleButton = findViewById(R.id.google)
        skip = findViewById(R.id.tv_skip)
        facebookLoginButton = findViewById(R.id.login_button)
        loadingView = findViewById(R.id.loading_view_login)
    }

    private fun handleFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        facebookLoginButton!!.registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                            try {
                                Log.d(TAG, "Response: $response")
                                val name = `object`.getString("name")
                                val email = `object`.getString("email")
                                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                                intent.putExtra("name", name)
                                intent.putExtra("email", email)
                                startActivity(intent)
                                val isLoggedIn = loginResult.accessToken != null && !loginResult.accessToken.isExpired

                                if (isLoggedIn) {
                                    LoginManager.getInstance().logOut()
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Toast.makeText(this@LoginActivity, getString(R.string.facebook_login_canceled), Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(exception: FacebookException) {
                        Toast.makeText(this@LoginActivity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onError: $exception")
                    }
                })

    }

    private fun handleGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        googleButton!!.setOnClickListener {
            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            var name: String? = null
            if (account != null) {
                name = account.displayName
            }
            var email: String? = null
            if (account != null) {
                email = account.email
            }
            Log.d(TAG, "handleSignInResult: $name$email")
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("email", email)
            startActivity(intent)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }

    }

    fun onClick(v: View) {
        if (v === fb) {
            facebookLoginButton!!.performClick()
        }
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
        private const val RC_SIGN_IN = 125
    }
}
