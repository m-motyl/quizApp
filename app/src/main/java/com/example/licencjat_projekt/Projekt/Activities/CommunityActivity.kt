package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadFriendModel
import com.example.licencjat_projekt.Projekt.database.User
import com.example.licencjat_projekt.Projekt.utils.FriendsList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community.*


class CommunityActivity : AppCompatActivity(), View.OnClickListener {
    private var friendsList = ArrayList<ReadFriendModel>()
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
            community_search_linear.visibility =
                if (community_search_linear.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            community_search.visibility =
                if (community_search_linear.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            //TODO: fix this
//            community_own_friends_linear.background =
//                if(community_own_friends_linear.background == ColorDrawable(resources.getColor(R.color.translucent))){
//                    ColorDrawable(resources.getColor(R.color.gray_tint))
//                }else{
//                    ColorDrawable(resources.getColor(R.color.translucent))
//                }
            return true
        }
        return if (id == R.id.action_community_toolbar_invitation) {

            // Do something
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.community_own_friends_linear -> {
                //TODO: click on layout and make other GONE
                community_search_linear.visibility = View.GONE
            }
        }
    }

    private fun exposedToModel(list: List<User>) {
        val friendsArrayList = ArrayList<ReadFriendModel>()
        for (i in list) {
            friendsArrayList.add(
                ReadFriendModel(
                    i.login,
                )
            )
        }
        friendsList = friendsArrayList
    }


    private fun friendsRecyclerView(friends: ArrayList<ReadFriendModel>) {
        community_rv_friends.layoutManager = LinearLayoutManager(this)
        community_rv_friends.setHasFixedSize(true)
        val friendsList = FriendsList(this, friends)
        community_rv_friends.adapter = friendsList

        friendsList.setOnClickListener(object : FriendsList.OnClickListener {

            override fun onClick(position: Int, model: ReadFriendModel) {
                val intent = Intent(
                    this@CommunityActivity,
                    ProfileActivity::class.java
                )

                intent.putExtra("profile", model)
                startActivity(intent)
            }
        })
    }

}