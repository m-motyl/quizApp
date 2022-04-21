package com.example.licencjat_projekt.Projekt.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.licencjat_projekt.Projekt.Models.AnswerModel
import com.example.licencjat_projekt.Projekt.Models.GroupModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.group_item.view.*
import kotlinx.android.synthetic.main.question_item.view.*

open class GroupsList(
    private val context: Context,
    private var listOfGroups: ArrayList<GroupModel>
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var onClickListener: OnClickListener? = null

    private class OwnViewHolder(
        view: View
    ): RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OwnViewHolder(LayoutInflater.from(context).inflate(
            R.layout.group_item, parent, false
        )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ptr = listOfGroups[position]
        if(holder is OwnViewHolder){
            holder.itemView.item_group_btn.text = ptr.groupName
        }
        //passing which position was clicked on rv
        //passing ptr
        holder.itemView.setOnClickListener{
            if(onClickListener!= null){
                onClickListener!!.onClick(position, ptr)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfGroups.size
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: GroupModel)
    }
}