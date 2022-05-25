package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.LoadUserModel
import com.example.licencjat_projekt.Projekt.Models.ReadFriendModel
import com.example.licencjat_projekt.Projekt.Models.ReadUserModel
import com.example.licencjat_projekt.Projekt.database.*
import com.example.licencjat_projekt.Projekt.utils.FriendsList
import com.example.licencjat_projekt.Projekt.utils.UsersList
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


class CommunityActivity : AppCompatActivity(), View.OnClickListener {
    var searchUserString: String? = null

    private var friendsList = ArrayList<LoadUserModel>()
    private var usersList = ArrayList<LoadUserModel>()
    private lateinit var layoutColorInitial: Drawable
    private lateinit var friendsLinearLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        friendsLinearLayout = findViewById(R.id.community_own_friends_linear)
        layoutColorInitial = friendsLinearLayout.background

        setSupportActionBar(community_toolbar)
        community_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        community_own_friends_linear.setOnClickListener(this)
        community_search_btn.setOnClickListener(this)

        getAllFriends()
        friendsRecyclerView(friendsList)
    }

    private fun getAllFriends() = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            val list = Friend.find { (Friends.from eq currentUser!!.id) and (Friends.status eq 1) }
                .with(Friend::to).toList()
            if (list.isNotEmpty())
                exposedToFriendModel(list)
        }
    }
    //:TODO (WITOLD) pobierać z bazy userów o podobnym lub pasującym loginie/nicku
    private fun getLikeUsers(){
        usersList.clear()
        searchUserString = searchUserString!!.lowercase()
        //exposedToUserModel(list)
        return
    }

    private fun exposedToFriendModel(l: List<Friend>) {
        for (i in l) {
            friendsList.add(
                LoadUserModel(
                    id = i.to.id.value,
                    login = i.to.login,
                    profile_picture = i.to.profile_picture!!.bytes,
                    creation_time = i.to.creation_time.toString()
                )
            )
        }
        for (i in friendsList) {
            Log.e("", i.login)
            Log.e("", i.creation_time)
        }
    }
//:TODO (WITEK) odkomentuj i zobaczysz o co chodzi

//    private fun exposedToUserModel(l: List<User>){
//        for (i in l) {
//            usersList.add(
//                LoadUserModel(
//                    id = i.to.id.value,
//                    login = i.to.login,
//                    profile_picture = i.to.profile_picture!!.bytes,
//                    creation_time = i.to.creation_time.toString()
//                )
//            )
//        }
//        for (i in usersList) {
//            Log.e("", i.login)
//            Log.e("", i.creation_time)
//        }
//    }

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
            community_own_friends_linear.background =
                if(community_own_friends_linear.background != layoutColorInitial){
                    layoutColorInitial
                }else{
                    ColorDrawable(ContextCompat.getColor(this, R.color.gray_tint))
                }
            return true
        }
        return if (id == R.id.action_community_toolbar_invitation) {

            val intent = Intent(
                this,
                CommunityInviteMsg::class.java
            )
            startActivity(intent)
            return true
        } else super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.community_own_friends_linear -> {
                //TODO: click on layout and make other GONE
                community_search_linear.visibility = View.GONE
            }
            R.id.community_search_btn -> {
                searchUserString = search_et.text.toString()
                if (searchUserString != null){
                    getLikeUsers()
                    usersRecyclerView(usersList)
                }
            }
        }
    }

    private fun friendsRecyclerView(friends: ArrayList<LoadUserModel>) {
        community_rv_friends.layoutManager = LinearLayoutManager(this)
        community_rv_friends.setHasFixedSize(true)
        val friendsList = FriendsList(this, friends)
        community_rv_friends.adapter = friendsList

        friendsList.setOnClickListener(object : FriendsList.OnClickListener {

            override fun onClick(position: Int, model: LoadUserModel) {
                val intent = Intent(
                    this@CommunityActivity,
                    ProfileFriendActivity::class.java
                )

                intent.putExtra("profile", model)
                startActivity(intent)
            }
        })
    }

    private fun usersRecyclerView(users: ArrayList<LoadUserModel>) {
        community_rv_users.layoutManager = LinearLayoutManager(this)
        community_rv_users.setHasFixedSize(true)
        val usersList = UsersList(this, users)
        community_rv_users.adapter = usersList

        usersList.setOnClickListener(object : UsersList.OnClickListener {

            override fun onClick(position: Int, model: LoadUserModel) {
                val intent = Intent(
                    this@CommunityActivity,
                    ProfileActivityAdd::class.java
                )

                intent.putExtra("profile", model)
                startActivity(intent)
            }
        })
    }

}