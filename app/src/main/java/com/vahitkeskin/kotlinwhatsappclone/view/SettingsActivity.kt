package com.vahitkeskin.kotlinwhatsappclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.vahitkeskin.kotlinwhatsappclone.R
import com.vahitkeskin.kotlinwhatsappclone.databinding.ActivitySettingsBinding
import com.vahitkeskin.kotlinwhatsappclone.util.Util.UserName
import com.vahitkeskin.kotlinwhatsappclone.model.Users
import com.vahitkeskin.kotlinwhatsappclone.util.Util
import kotlin.collections.HashMap

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var store: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        store = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.saveButton.setOnClickListener {
            val status = binding.etStatus.text.toString()
            val userName = binding.etUserName.text.toString()

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap[UserName] = userName
            hashMap["status"] = status

            database.reference.child(Util.Users).child(FirebaseAuth.getInstance().uid!!)
                .updateChildren(hashMap)

            Toast.makeText(this,"Profile Updated",Toast.LENGTH_LONG).show()


        }

        database.reference.child(Util.Users).child(FirebaseAuth.getInstance().uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.getValue(Users::class.java)
                    Picasso.get()
                        .load(users?.profilepic)
                        .placeholder(R.drawable.avatar)
                        .into(binding.profileImage)

                    binding.etStatus.setText(users?.status)
                    binding.etUserName.setText((users?.userName))
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        binding.plus.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (data != null) {
                val sFile = data.data
                sFile?.let {
                    binding.profileImage.setImageURI(sFile)

                    val reference: StorageReference = store.reference.child("profile_pictures")
                        .child(FirebaseAuth.getInstance().uid!!)

                    reference.putFile(sFile).addOnSuccessListener {
                        Toast.makeText(this, "Uplaod", Toast.LENGTH_LONG).show()
                        reference.downloadUrl.addOnSuccessListener { uri ->
                            database.reference.child(Util.Users)
                                .child(FirebaseAuth.getInstance().uid!!)
                                .child("profilepic").setValue(uri.toString())
                        }
                    }
                }

            }
        }
    }
}