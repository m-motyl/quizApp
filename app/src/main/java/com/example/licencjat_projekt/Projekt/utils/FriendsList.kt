package com.example.licencjat_projekt.Projekt.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.licencjat_projekt.Projekt.Models.ReadFriendModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.answer_item.view.*
import kotlinx.android.synthetic.main.item_contact.view.*

open class FriendsList(
    private val context: Context,
    private var listOfFriends: ArrayList<ReadFriendModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    private class OwnViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OwnViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_contact, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ptr = listOfFriends[position]
        if (holder is OwnViewHolder) {
            holder.itemView.item_contact_name.text = ptr.login
            //TODO: avatar
            //passing which position was clicked on rv
            //passing ptr
            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, ptr)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfFriends.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ReadFriendModel)
    }
}
