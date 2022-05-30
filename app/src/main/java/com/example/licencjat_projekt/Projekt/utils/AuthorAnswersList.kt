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
import com.example.licencjat_projekt.Projekt.Models.ReadAnswerModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.question_item.view.*

open class AuthorAnswersList(
    private val context: Context,
    private var listOfAnswers: ArrayList<ReadAnswerModel>
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
            holder.itemView.item_answer.text = ptr.answer_text
        }

        if(ptr.is_Correct) {
            holder.itemView.item_answer_ll.setBackgroundColor(Color.GRAY)
        }else{
            holder.itemView.item_answer_ll.setBackgroundColor(Color.WHITE)
        }

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
        fun onClick(position: Int, model: ReadAnswerModel)
    }
}