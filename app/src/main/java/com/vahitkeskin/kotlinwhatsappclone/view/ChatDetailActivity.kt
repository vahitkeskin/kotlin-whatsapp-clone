package com.vahitkeskin.kotlinwhatsappclone.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.vahitkeskin.kotlinwhatsappclone.R
import com.vahitkeskin.kotlinwhatsappclone.adapter.ChatAdapter
import com.vahitkeskin.kotlinwhatsappclone.databinding.ActivityChatDetailBinding
import com.vahitkeskin.kotlinwhatsappclone.model.MessageModel
import com.vahitkeskin.kotlinwhatsappclone.util.Util.Chats
import com.vahitkeskin.kotlinwhatsappclone.util.Util.ProfilePic
import com.vahitkeskin.kotlinwhatsappclone.util.Util.UserId
import com.vahitkeskin.kotlinwhatsappclone.util.Util.UserName
import java.util.*
import kotlin.collections.ArrayList


class ChatDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatDetailBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        val senderId = auth.uid.toString()
        val recieveId = intent.getStringExtra(UserId)
        val userName = intent.getStringExtra(UserName)
        val profilePic = intent.getStringExtra(ProfilePic)

        binding.userName.text = userName
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage)

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val messageModels: ArrayList<MessageModel> = ArrayList()
        val chatAdapter = ChatAdapter(messageModels, this, recieveId)
        binding.chatRecyclerView.adapter = chatAdapter

        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.layoutManager = linearLayoutManager

        val senderRoom: String = senderId + recieveId
        val receiverRoom: String = recieveId + senderId


        database.reference.child(Chats).child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageModels.clear()
                    for (dataSnapshot in snapshot.children) {
                        val model = dataSnapshot.getValue(MessageModel::class.java)
                        model?.let {
                            model.messageId = snapshot.key
                            messageModels.add(model)
                        }
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@ChatDetailActivity,
                        "Error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })


        binding.send.setOnClickListener {
            val message = binding.etMessage.text.toString()
            val model: MessageModel = MessageModel(senderId, message)
            model.timestamp = Date().time
            binding.etMessage.setText("")

            database.reference.child(Chats).child(senderRoom).push().setValue(model)
                .addOnSuccessListener { void ->
                    database.reference.child(Chats).child(receiverRoom).push().setValue(model)
                        .addOnSuccessListener { aVoid ->

                        }
                }
        }


    }
}