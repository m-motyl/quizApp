package com.example.licencjat_projekt.Projekt.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuizInvitationModel
import com.example.licencjat_projekt.Projekt.database.Quiz
import com.example.licencjat_projekt.Projekt.database.Quizes
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.item_quiz_message.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


open class QuizInviteList(
    private val context: Context,
    private var listOfFriendInvitations: ArrayList<ReadQuizInvitationModel>
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
            holder.itemView.item_quiz_invite_quiz_name.text = Quiz.findById(ptr.quizID)!!.title
            holder.itemView.item_quiz_invite_name.text = ptr.fromUser.login

            holder.itemView.item_quiz_accept_btn.setOnClickListener {
                if (onAcceptClickListener != null){
                    onAcceptClickListener!!.onClick(position, ptr)
                }
            }
            holder.itemView.item_quiz_decline_btn.setOnClickListener {
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
    private fun getQuizNamexd(quizID:Int)= runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            Quiz.findById(quizID)
        }
    }
    override fun getItemCount(): Int {
        return listOfFriendInvitations.size
    }

    fun setOnClickListener(onClickListener: OnClickListener, onAcceptClickListener: OnAcceptClickListener, onDeclineClickListener: OnDeclineClickListener) {
        this.onClickListener = onClickListener
        this.onAcceptClickListener = onAcceptClickListener
        this.onDeclineClickListener = onDeclineClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ReadQuizInvitationModel)
    }

    interface OnAcceptClickListener {
        fun onClick(position: Int, model: ReadQuizInvitationModel)
    }

    interface OnDeclineClickListener {
        fun onClick(position: Int, model: ReadQuizInvitationModel)
    }
}