package com.vahitkeskin.kotlinwhatsappclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vahitkeskin.kotlinwhatsappclone.adapter.ChatAdapter
import com.vahitkeskin.kotlinwhatsappclone.databinding.ActivityGroupChatBinding
import com.vahitkeskin.kotlinwhatsappclone.model.MessageModel
import com.vahitkeskin.kotlinwhatsappclone.util.Util.GroupChat
import java.util.*
import kotlin.collections.ArrayList

class GroupChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val messageModels: ArrayList<MessageModel> = ArrayList()
        val senderId = FirebaseAuth.getInstance().uid
        binding.userName.text = "Friends Chat"
        val adapter: ChatAdapter = ChatAdapter(messageModels, this)
        binding.chatRecyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.layoutManager = linearLayoutManager

        database.reference.child(GroupChat).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageModels.clear()
                for (dataSnapshot in snapshot.children) {
                    val model = dataSnapshot.getValue(MessageModel::class.java)
                    model?.let {
                        messageModels.add(model)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.send.setOnClickListener {
            val message: String = binding.etMessage.text.toString()
            val model: MessageModel = MessageModel(senderId,message)
            model.timestamp = Date().time

            binding.etMessage.setText("")

            database.reference.child(GroupChat).push().setValue(model)
                .addOnSuccessListener {void ->

                }
        }

    }
}