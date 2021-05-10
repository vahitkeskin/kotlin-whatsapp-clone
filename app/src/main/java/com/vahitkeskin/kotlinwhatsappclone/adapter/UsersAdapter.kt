package com.vahitkeskin.kotlinwhatsappclone.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.vahitkeskin.kotlinwhatsappclone.view.ChatDetailActivity
import com.vahitkeskin.kotlinwhatsappclone.R
import com.vahitkeskin.kotlinwhatsappclone.model.Users
import com.vahitkeskin.kotlinwhatsappclone.util.Util.Chats
import com.vahitkeskin.kotlinwhatsappclone.util.Util.Message
import com.vahitkeskin.kotlinwhatsappclone.util.Util.ProfilePic
import com.vahitkeskin.kotlinwhatsappclone.util.Util.TimesTamp
import com.vahitkeskin.kotlinwhatsappclone.util.Util.UserId
import com.vahitkeskin.kotlinwhatsappclone.util.Util.UserName
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(
    private val usersList: ArrayList<Users>,
    private val context: Context
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: CircleImageView = view.findViewById(R.id.profileImage)
        val userName: TextView = view.findViewById(R.id.userNameLast)
        val lastMessage: TextView = view.findViewById(R.id.lastMessage)
        val cardViewID: CardView = view.findViewById(R.id.cardViewID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sample_show_user, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val users: Users = usersList[position]
        Picasso.get().load(users.profilepic).placeholder(R.drawable.avatar).into(holder.image)
        holder.userName.text = users.userName

        FirebaseDatabase.getInstance().reference.child(Chats)
            .child(FirebaseAuth.getInstance().uid + users.userId)
            .orderByChild(TimesTamp)
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        for (dataSnapshot in snapshot.children) {
                            holder.lastMessage.text = dataSnapshot.child(Message).value.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatDetailActivity::class.java)
            intent.putExtra(UserId, users.userId)
            intent.putExtra(ProfilePic, users.profilepic)
            intent.putExtra(UserName, users.userName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

}