package com.vahitkeskin.kotlinwhatsappclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.vahitkeskin.kotlinwhatsappclone.R
import com.vahitkeskin.kotlinwhatsappclone.adapter.FragmentsAdapter
import com.vahitkeskin.kotlinwhatsappclone.databinding.ActivityMainBinding
import com.vahitkeskin.kotlinwhatsappclone.util.Util.Message

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference(Message)
        myRef.setValue("Hello, World")

        auth = FirebaseAuth.getInstance()

        binding.viewPager.adapter = FragmentsAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.groupChatFab.setOnClickListener {
            val intent = Intent(this, GroupChatActivity::class.java)
            startActivity(intent)
        }

        binding.settingsFab.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.logOutFab.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}