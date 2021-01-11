package com.example.tictactoe

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.activity_intro.*


class IntroActivity : AppCompatActivity() {
    private var backPressedTime: Long = -1
    private lateinit var backToast: Toast
    private var info: String = "Press again to exit"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        button_forward.setOnClickListener {
            val usernameEt = username
            val name: String
            usernameEt.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(v)
                }
            }
            name = if(usernameEt.text.toString().isEmpty()) {
                "User1"
            } else {
                usernameEt.text.toString()
            }
            val intent = Intent(this@IntroActivity, GameActivity::class.java)
            intent.putExtra("Name", name)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
            super.onBackPressed()
            finishAffinity();
            finish();
        } else {
            backToast = Toast.makeText(baseContext, HtmlCompat.fromHtml("<font color='#000000'><b>$info</b></font>", HtmlCompat.FROM_HTML_MODE_LEGACY), Toast.LENGTH_SHORT)
            backToast.show()
        }

        backPressedTime = System.currentTimeMillis()
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}