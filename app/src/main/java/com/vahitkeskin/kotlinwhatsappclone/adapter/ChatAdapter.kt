package com.vahitkeskin.kotlinwhatsappclone.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vahitkeskin.kotlinwhatsappclone.R
import com.vahitkeskin.kotlinwhatsappclone.model.MessageModel
import com.vahitkeskin.kotlinwhatsappclone.util.Util.Chats


class ChatAdapter(
    val messageModels: ArrayList<MessageModel>,
    val context: Context,
    val recId: String? = ""
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SENDER_VIEW_TYPE = 1
    val RECEIVER_VIEW_TYPE = 2

    class RecieverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val receiverMsg: TextView = view.findViewById(R.id.recieiverText)
        val receiverTime: TextView = view.findViewById(R.id.recieverTime)
    }

    class SenderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderMsg: TextView = view.findViewById(R.id.senderText)
        val senderTime: TextView = view.findViewById(R.id.senderTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SENDER_VIEW_TYPE) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sample_sender, parent, false)
            return SenderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sample_reciever, parent, false)
            return RecieverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageList = messageModels[position]

        holder.itemView.setOnLongClickListener {
            longClickMsgItem(messageList)
            false
        }

        if (holder.javaClass == SenderViewHolder::class.java) {
            (holder as SenderViewHolder).senderMsg.text = messageList.message
        } else {
            (holder as RecieverViewHolder).receiverMsg.text = messageList.message
        }
    }

    private fun longClickMsgItem(messageModelItem: MessageModel) {
        val alert = AlertDialog.Builder(context)
        alert.setTitle("Are you sure?")
        alert.setMessage("${messageModelItem.message} Delete!")
        alert.setPositiveButton("Yes") { dialog, witch ->
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val senderRoom = FirebaseAuth.getInstance().uid + recId
            database.reference.child(Chats).child(senderRoom)
                .child(messageModelItem.messageId!!)
                .setValue(null)
        }
        alert.setNegativeButton("No") { dialog, witch ->
            dialog.dismiss()
        }
        alert.show()
    }

    override fun getItemCount(): Int {
        return messageModels.size
    }

    override fun getItemViewType(position: Int): Int {
        if (messageModels.get(position).uId.equals(FirebaseAuth.getInstance().uid)) {
            return SENDER_VIEW_TYPE
        } else {
            return RECEIVER_VIEW_TYPE
        }
    }

}