package com.vahitkeskin.kotlinwhatsappclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vahitkeskin.kotlinwhatsappclone.databinding.ActivitySignUpBinding
import com.vahitkeskin.kotlinwhatsappclone.model.Users
import com.vahitkeskin.kotlinwhatsappclone.util.Util
import com.vahitkeskin.kotlinwhatsappclone.util.Util.Users

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        binding.tvAlreadyAccount.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp() {
        val userName = binding.etUserName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val users = Users(userName, email, password)
                        val id = task.result?.user?.uid as String
                        database.reference.child(Util.Users).child(id).setValue(users)

                        Toast.makeText(this, "User Created Successfully", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this, "Is not null!", Toast.LENGTH_LONG).show()
        }

    }
}