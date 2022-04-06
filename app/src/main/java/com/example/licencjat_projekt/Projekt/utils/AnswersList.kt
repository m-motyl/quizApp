package com.example.licencjat_projekt.Projekt.utils

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.licencjat_projekt.Projekt.Models.AnswerModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.question_item.view.*

open class AnswersList(
    private val context: Context,
    private var listOfAnswers: ArrayList<AnswerModel>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        private var onClickListener: OnClickListener? = null

    private class OwnViewHolder(
        view: View
    ): RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OwnViewHolder(LayoutInflater.from(context).inflate(
                R.layout.question_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ptr = listOfAnswers[position]
        if(holder is OwnViewHolder){
            holder.itemView.item_answer.text = ptr.answer
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
        return listOfAnswers.size
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: AnswerModel)
    }
}