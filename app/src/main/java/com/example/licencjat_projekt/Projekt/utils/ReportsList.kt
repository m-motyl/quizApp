package com.example.licencjat_projekt.Projekt.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.licencjat_projekt.Projekt.Models.AnswerModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.Models.ReadReportModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.question_item.view.*
import kotlinx.android.synthetic.main.quiz_item.view.*
import kotlinx.android.synthetic.main.quiz_item.view.quizitem_title
import kotlinx.android.synthetic.main.report_item.view.*

open class ReportsList(
    private val context: Context,
    private var listOfQuizes: ArrayList<ReadReportModel>
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var onClickListener: OnClickListener? = null

    private class OwnViewHolder(
        view: View
    ): RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OwnViewHolder(LayoutInflater.from(context).inflate(
            R.layout.report_item, parent, false
        )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ptr = listOfQuizes[position]
        if(holder is OwnViewHolder){
            holder.itemView.reportitem_title.text = ptr.title
            if(ptr.points.isNaN()){
                holder.itemView.reportitem_user_score.text = "0.0"
            }else {
                holder.itemView.reportitem_user_score.text = ptr.points.toString()
            }
            holder.itemView.reportitem_quiz_score.text = "/" + ptr.max_points.toString() + ".0"
            holder.itemView.reportitem_user.text = ptr.author
            holder.itemView.reportitem_image.setImageBitmap(byteArrayToBitmap(ptr.image))
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
        return listOfQuizes.size
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ReadReportModel)
    }
    //decode image read from db
    private fun byteArrayToBitmap(
        data: ByteArray
    ): Bitmap {
        return BitmapFactory.decodeByteArray(
            data,
            0,
            data.size
        )
    }
}