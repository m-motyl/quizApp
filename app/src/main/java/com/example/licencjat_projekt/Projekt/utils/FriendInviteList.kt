package com.example.licencjat_projekt.Projekt.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.Projekt.database.Quiz
import com.example.licencjat_projekt.Projekt.database.User
import com.example.licencjat_projekt.Projekt.database.Users
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


open class FriendInviteList(
    private val context: Context,
    private var listOfFriendInvitations: ArrayList<ReadFriendInvitationModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var onAcceptClickListener: OnAcceptClickListener? = null
    private var onDeclineClickListener: OnDeclineClickListener? = null

    private class OwnViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OwnViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_message, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ptr = listOfFriendInvitations[position]
        if (holder is OwnViewHolder) {
            val user = getUser(ptr.fromUser)
            holder.itemView.item_invite_name.text = user!!.login
            holder.itemView.item_accept_btn.setOnClickListener {
                if (onAcceptClickListener != null){
                    onAcceptClickListener!!.onClick(position, ptr)
                }
            }
            holder.itemView.item_decline_btn.setOnClickListener {
                if (onDeclineClickListener != null){
                    onDeclineClickListener!!.onClick(position, ptr)
                }
            }
        }

        //passing which position was clicked on rv
        //passing ptr
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, ptr)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfFriendInvitations.size
    }

    private fun getUser(userID: Int)= runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            User.findById(userID)
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener, onAcceptClickListener: OnAcceptClickListener, onDeclineClickListener: OnDeclineClickListener) {
        this.onClickListener = onClickListener
        this.onAcceptClickListener = onAcceptClickListener
        this.onDeclineClickListener = onDeclineClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ReadFriendInvitationModel)
    }

    interface OnAcceptClickListener {
        fun onClick(position: Int, model: ReadFriendInvitationModel)
    }

    interface OnDeclineClickListener {
        fun onClick(position: Int, model: ReadFriendInvitationModel)
    }
}