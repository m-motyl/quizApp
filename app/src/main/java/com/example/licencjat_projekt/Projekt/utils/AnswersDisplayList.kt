package com.example.licencjat_projekt.Projekt.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.licencjat_projekt.Projekt.Models.AnswerModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.answer_item.view.*

open class AnswersDisplayList(
    private val context: Context,
    private var listOfAnswers: ArrayList<AnswerModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    private class OwnViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OwnViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.answer_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ptr = listOfAnswers[position]
        if (holder is OwnViewHolder) {
            if (!holder.itemView.answer_item_text_btn.text.isNullOrEmpty()) {
                holder.itemView.answer_item_text_btn.text = ptr.answer_text
                holder.itemView.answer_item_text_btn.visibility = View.VISIBLE
            } else if (null != holder.itemView.answer_item_image_btn.drawable) {
                holder.itemView.answer_item_image_btn.visibility = View.VISIBLE
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
        return listOfAnswers.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: AnswerModel)
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }
}