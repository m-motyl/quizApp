package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadFriendModel
import com.example.licencjat_projekt.Projekt.Models.ReadUserModel
import com.example.licencjat_projekt.Projekt.database.Quiz
import com.example.licencjat_projekt.Projekt.database.User
import com.example.licencjat_projekt.Projekt.utils.FriendsList
import com.example.licencjat_projekt.Projekt.utils.UsersList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


class CommunityActivity : AppCompatActivity(), View.OnClickListener {
    private var friendsList = ArrayList<ReadFriendModel>()
    private var usersList = ArrayList<ReadUserModel>()
    private lateinit var userAdapter: UsersList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        setSupportActionBar(community_toolbar)
        community_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        getAllFriends()
        friendsRecyclerView(friendsList)
    }

    private fun getAllFriends() {
        val list = runBlocking {
            return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                //TODO: make return user.friends
                User.all().toList()
            }
        }
        if (list.isNotEmpty())
            exposedToFriendModel(list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.community_toolbar_menu, menu)
//        val searchItem = menu!!.findItem(R.id.community_search_drawer)
//        val searchView: SearchView = searchItem.actionView as SearchView
//
//        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                userAdapter.filter.filter(newText)
//                return false
//            }
//        })
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
//            getAllUsers()
//            usersRecyclerView(usersList)
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
        }
    }

    private fun getAllUsers() {
        val list = runBlocking {
            return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                User.all().toList()
            }
        }
        if (list.isNotEmpty())
            exposedToUserModel(list)
    }

    private fun exposedToFriendModel(list: List<User>) {
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

    private fun exposedToUserModel(list: List<User>) {
        val usersArrayList = ArrayList<ReadUserModel>()
        for (i in list) {
           usersArrayList.add(
                ReadUserModel(
                    i.login,
                )
            )
        }
       usersList = usersArrayList
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

    private fun usersRecyclerView(users: ArrayList<ReadUserModel>) {
        community_rv_users.layoutManager = LinearLayoutManager(this)
        community_rv_users.setHasFixedSize(true)
        val usersList = UsersList(this, users)
        userAdapter = usersList
        community_rv_users.adapter = usersList

        usersList.setOnClickListener(object : UsersList.OnClickListener {

            override fun onClick(position: Int, model: ReadUserModel) {
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