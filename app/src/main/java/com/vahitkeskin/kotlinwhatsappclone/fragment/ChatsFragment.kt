package com.vahitkeskin.kotlinwhatsappclone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vahitkeskin.kotlinwhatsappclone.adapter.UsersAdapter
import com.vahitkeskin.kotlinwhatsappclone.databinding.FragmentChatsBinding
import com.vahitkeskin.kotlinwhatsappclone.model.Users
import com.vahitkeskin.kotlinwhatsappclone.util.Util

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private val list: ArrayList<Users> = ArrayList()
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance()

        val adapter = UsersAdapter(list, context!!)
        binding.chatRecyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context)
        binding.chatRecyclerView.layoutManager = linearLayoutManager

        database.reference.child(Util.Users).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot in snapshot.children) {
                    val users = dataSnapshot.getValue(Users::class.java)
                    users?.let {
                        users.userId = dataSnapshot.key
                        if (!users.userId.equals(FirebaseAuth.getInstance().uid)) {
                            list.add(users)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return binding.root
    }
}