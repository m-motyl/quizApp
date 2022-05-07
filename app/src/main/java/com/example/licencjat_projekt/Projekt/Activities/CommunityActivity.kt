package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.licencjat_projekt.Projekt.Models.GroupModel
import com.example.licencjat_projekt.Projekt.utils.GroupsList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community.*
import java.text.FieldPosition


class CommunityActivity : AppCompatActivity(), View.OnClickListener{
    private var groupsList = ArrayList<GroupModel>()
    private val numberOfColumns = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        community_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        community_add_btn.setOnClickListener(this)
        community_accept_btn.setOnClickListener(this)
        community_decline_btn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.community_add_btn -> {
                community_add_new_group_linear_layout.visibility = View.VISIBLE
            }
            R.id.community_accept_btn -> {
                when {
                    community_new_group_name_text_edit.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj nazwę grupy.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        val groupName: String = community_new_group_name_text_edit.text.toString()
                        val group = GroupModel(
                            groupName,
                            null
                        )
                        community_add_new_group_linear_layout.visibility = View.INVISIBLE
                        community_new_group_name_text_edit.text.clear()
                        groupsList.add(group)
                        groupsRecyclerView(groupsList)

                    }
                }
            }
            R.id.community_decline_btn -> {
                community_new_group_name_text_edit.text.clear()
                community_add_new_group_linear_layout.visibility = View.INVISIBLE
            }
        }
    }

    private fun groupsRecyclerView(groups: ArrayList<GroupModel>){
        community_recycler_view.layoutManager = GridLayoutManager(this, numberOfColumns, GridLayoutManager.VERTICAL, false)
        community_recycler_view.setHasFixedSize(true)

        val groupsList = GroupsList(this, groups)
        community_recycler_view.adapter = groupsList

        groupsList.setOnClickListener(object: GroupsList.OnClickListener{
            override fun onClick(position: Int, model: GroupModel) {
                val intent = Intent(
                    this@CommunityActivity,
                    CommunityDetailActivity::class.java
                )

                intent.putExtra( //passing object to activity
                    CommunityActivity,
                    model
                )
                startActivity(intent)
            }
        })
        groupsList.setOnLongClickListener(object: GroupsList.OnLongClickListener{
            override fun onLongClick(position: Int, model: GroupModel) {
            }
        })
    }

}