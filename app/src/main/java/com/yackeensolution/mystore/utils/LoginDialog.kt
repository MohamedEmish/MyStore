package com.yackeensolution.mystore.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import com.yackeensolution.mystore.R.id
import com.yackeensolution.mystore.R.layout
import com.yackeensolution.mystore.views.logActivities.LoginActivity

class LoginDialog(context: Context) : Dialog(context), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        Utils.setLocale(context)
        setContentView(layout.dialog_login)
        val mainContainer = findViewById<View>(id.login_dialog_main_container)
        Utils.rtlSupport(context, mainContainer)

        val cancelButton = findViewById<View>(id.dialog_login_btn_cancel)
        cancelButton.setOnClickListener(this)

        val okButton = findViewById<View>(id.dialog_login_btn_ok)
        okButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            id.dialog_login_btn_cancel -> dismiss()
            id.dialog_login_btn_ok -> {
                val loginIntent = Intent(context, LoginActivity::class.java)
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(loginIntent)
            }
            else -> {
            }
        }
        dismiss()
    }
}