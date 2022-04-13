package com.example.licencjat_projekt.Projekt.Activities

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.licencjat_projekt.Projekt.Models.GroupModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community.*



class CommunityActivity : AppCompatActivity(), View.OnClickListener{
    private var groupList = ArrayList<GroupModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        community_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        community_add_btn.setOnClickListener(this)
        community_accept_btn.setOnClickListener(this)
        community_decline_btn.setOnClickListener(this)
        community_group_btn.setOnClickListener(this)

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
                            groupName
                        )
                        community_add_new_group_linear_layout.visibility = View.INVISIBLE
                        community_new_group_name_text_edit.text.clear()
                        groupList.add(group)
                        addGroup(groupName)
                    }
                }
            }
            R.id.community_decline_btn -> {
                community_new_group_name_text_edit.text.clear()
                community_add_new_group_linear_layout.visibility = View.INVISIBLE
            }
            R.id.community_group_btn -> {
                val intent = Intent(
                    this,
                    CommunityDetailActivity::class.java
                )
                startActivity(intent)
            }
        }
    }

    fun addGroup(groupName: String) {
        val gridLayout: GridLayout = findViewById(R.id.community_grid_layout)

        val layoutParams: GridLayout.LayoutParams = GridLayout.LayoutParams()

        lateinit var newBtn: Button
        newBtn.text = groupName

        gridLayout.addView(newBtn, layoutParams)
    }
}