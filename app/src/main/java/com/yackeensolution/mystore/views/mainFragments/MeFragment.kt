package com.yackeensolution.mystore.views.mainFragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.data.viewModels.UserViewModel
import com.yackeensolution.mystore.models.LogoutRequest
import com.yackeensolution.mystore.utils.LoginDialog
import com.yackeensolution.mystore.utils.SaveSharedPreference
import com.yackeensolution.mystore.views.logActivities.LoginActivity
import com.yackeensolution.mystore.views.logActivities.MainActivity
import com.yackeensolution.mystore.views.meTabActivities.MyFavouritesActivity
import com.yackeensolution.mystore.views.meTabActivities.MyOrdersActivity
import com.yackeensolution.mystore.views.meTabActivities.MyProfileActivity
import com.yackeensolution.mystore.views.meTabActivities.MyReviewsActivity

class MeFragment : Fragment() {
    private var loginButton: Button? = null
    private var myOrdersButton: Button? = null
    private var myFavouritesButton: Button? = null
    private var myReviewsButton: Button? = null
    private var myProfileButton: Button? = null
    private var language: String = ""
    private var arabicRadioButton: RadioButton? = null
    private var englishRadioButton: RadioButton? = null
    private var languageRadioGroup: RadioGroup? = null
    private var rootView: View? = null
    private var userViewModel: UserViewModel? = null

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val nullParent: ViewGroup? = null

        rootView = inflater.inflate(R.layout.fragment_me, nullParent)
        initViews()

        language = context?.let { SaveSharedPreference.getLanguage(it) }!!
        if (language == "ar") {
            arabicRadioButton!!.isChecked = true
        } else {
            englishRadioButton!!.isChecked = true
        }

        languageRadioGroup!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == arabicRadioButton!!.id) {
                context?.let { SaveSharedPreference.setLanguage(it, "ar") }
                language = "ar"
                activity!!.finish()
                startActivity(Intent(group.context, MainActivity::class.java))
            } else {
                context?.let { SaveSharedPreference.setLanguage(it, "en") }
                language = "en"
                activity!!.finish()
                startActivity(Intent(group.context, MainActivity::class.java))
            }
        }

        myFavouritesButton!!.setOnClickListener {
            if (context?.let { SaveSharedPreference.getUserId(it) } != -1) {
                startActivity(Intent(activity, MyFavouritesActivity::class.java))
            } else {
                val dialog = activity?.let { LoginDialog(it) }
                dialog!!.show()
            }
        }

        myProfileButton!!.setOnClickListener {
            if (context?.let { SaveSharedPreference.getUserId(it) } != -1) {
                startActivity(Intent(activity, MyProfileActivity::class.java))
            } else {
                val dialog = activity?.let { LoginDialog(it) }
                dialog!!.show()
            }
        }

        myReviewsButton!!.setOnClickListener {
            if (context?.let { SaveSharedPreference.getUserId(it) } != -1) {
                startActivity(Intent(activity, MyReviewsActivity::class.java))
            } else {
                val dialog = activity?.let { LoginDialog(it) }
                dialog!!.show()
            }
        }

        if (context?.let { SaveSharedPreference.getUserId(it) } != -1) {
            loginButton!!.setText(R.string.logout)
        } else {
            loginButton!!.setText(R.string.login)
        }

        loginButton!!.setOnClickListener {
            if (context?.let { SaveSharedPreference.getUserId(it) } != -1) {
                val dialog = activity?.let { LogoutDialog(it) }
                dialog!!.show()
            } else {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        myOrdersButton!!.setOnClickListener {
            if (context?.let { SaveSharedPreference.getUserId(it) } != -1) {
                startActivity(Intent(activity, MyOrdersActivity::class.java))
            } else {
                val dialog = activity?.let { LoginDialog(it) }
                dialog!!.show()
            }
        }
        return rootView
    }

    private inner class LogoutDialog internal constructor(context: Context) : Dialog(context), View.OnClickListener {

        private var yesButton: Button? = null
        private var cancelButton: Button? = null
        private var mainContainer: View? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_logout)
            val mLanguage = SaveSharedPreference.getLanguage(context)
            mainContainer = findViewById(R.id.logout_dialog_main_container)
            if (mLanguage == "ar") {
                ViewCompat.setLayoutDirection(mainContainer!!, ViewCompat.LAYOUT_DIRECTION_RTL)
            }

            cancelButton = findViewById(R.id.dialog_logout_btn_cancel)
            cancelButton!!.setOnClickListener(this)
            yesButton = findViewById(R.id.dialog_logout_btn_yes)
            yesButton!!.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.dialog_login_btn_cancel -> dismiss()
                R.id.dialog_logout_btn_yes -> logout()
                else -> {
                }
            }
            dismiss()
        }
    }

    private fun logout() {
        context?.let {
            val logoutRequest: LogoutRequest? = LogoutRequest(SaveSharedPreference.getUserId(it), SaveSharedPreference.getLanguage(it))

            userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
            userViewModel!!.userLogout(logoutRequest).observe(this, Observer<String> { state ->
                if (state == "OK") {
                    SaveSharedPreference.clearLanguage(it)
                    SaveSharedPreference.clearUserId(it)
                    loginButton!!.text = resources.getString(R.string.login)
                } else {
                    Toast.makeText(it, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                }
            })
        }
    }


    private fun initViews() {
        myOrdersButton = rootView!!.findViewById(R.id.btn_my_orders)
        myFavouritesButton = rootView!!.findViewById(R.id.btn_my_favourites)
        myReviewsButton = rootView!!.findViewById(R.id.btn_my_reviews)
        myProfileButton = rootView!!.findViewById(R.id.btn_my_profile)
        loginButton = rootView!!.findViewById(R.id.btn_logout)
        languageRadioGroup = rootView!!.findViewById(R.id.rg_language_group)
        arabicRadioButton = rootView!!.findViewById(R.id.rb_arabic)
        englishRadioButton = rootView!!.findViewById(R.id.rb_english)
    }

}