package com.example.licencjat_projekt.Projekt.Activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community.*


class CommunityActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        setSupportActionBar(community_toolbar)
        community_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.community_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_community_toolbar_add) {
            community_search.visibility = if (community_search.visibility == View.VISIBLE){
                View.GONE
            } else{
                View.VISIBLE
            }
            return true
        }
        return if (id == R.id.action_community_toolbar_invitation) {

            // Do something
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
        }
    }


}