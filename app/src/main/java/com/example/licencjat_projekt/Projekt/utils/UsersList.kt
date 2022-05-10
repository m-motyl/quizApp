package com.example.licencjat_projekt.Projekt.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.licencjat_projekt.Projekt.Models.ReadUserModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.item_contact.view.*
import java.util.*
import kotlin.collections.ArrayList

open class UsersList(
    private val context: Context,
    private var listOfUsers: ArrayList<ReadUserModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable{
    private var onClickListener: OnClickListener? = null
    private val filteredListOfUsers: ArrayList<ReadUserModel> = ArrayList<ReadUserModel>()

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
        val ptr = listOfUsers[position]
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
        return listOfUsers.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ReadUserModel)
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<ReadUserModel> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(listOfUsers)
            } else {
                val filterPattern = constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in listOfUsers) {
                    if (item.login.lowercase(Locale.getDefault()).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            filteredListOfUsers.clear()
            filteredListOfUsers.addAll(results.values as Collection<ReadUserModel>)
            notifyDataSetChanged()
        }
    }
}
