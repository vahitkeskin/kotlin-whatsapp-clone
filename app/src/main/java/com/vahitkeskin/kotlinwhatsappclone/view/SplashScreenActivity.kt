package com.vahitkeskin.kotlinwhatsappclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.vahitkeskin.kotlinwhatsappclone.databinding.ActivitySplashScreenBinding

@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val TAG = "SplashScreenActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        //2500
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick: ${millisUntilFinished / 1000}")
            }

            override fun onFinish() {
                val intent = Intent(this@SplashScreenActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }

        }.start()
    }
}