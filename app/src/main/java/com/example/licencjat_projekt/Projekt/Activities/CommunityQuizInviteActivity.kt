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
import com.example.licencjat_projekt.Projekt.Models.*
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


class CommunityQuizInviteActivity : AppCompatActivity() {
    var quizDetails: ReadQuizModel? = null
    var quizInvitation: ReadQuizInvitationModel? = null

    private var friendsList = ArrayList<LoadUserModel>()
    private lateinit var layoutColorInitial: Drawable
    private lateinit var friendsLinearLayout: LinearLayout
    private var toast: Toast = Toast.makeText(
        this,
        "Zaprosiłeś użytkownika do quizu",
        Toast.LENGTH_SHORT
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        setSupportActionBar(community_toolbar)
        community_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra(DetailQuizActivity.QUIZ_DETAILS)) {
            quizDetails = intent.getSerializableExtra(DetailQuizActivity.QUIZ_DETAILS) as ReadQuizModel
        }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.community_toolbar_menu, menu)
        return true
    }

    private fun exposeToQuizInvitationModel(invitatedUser: LoadUserModel) {
        var invitatedUserID = invitatedUser.id
        if (intent.hasExtra(CommunityActivity.PROFILE_DETAILS)) {
            //TODO: (WITEK) get user from id
//            quizInvitation = ReadQuizInvitationModel(
//                status = 0,
//                fromUser = currentUser,
//                toUser = ,
//                quizID = quizDetails.id,
//                quizCode = quizDetails.invitation_code,
//            )
        }
    }
//:TODO(Witek) wysłać zaproszenie do bazy
    private fun sendInvitationtoDataBase(){
        quizInvitation
    }


    private fun friendsRecyclerView(friends: ArrayList<LoadUserModel>) {
        community_rv_friends.layoutManager = LinearLayoutManager(this)
        community_rv_friends.setHasFixedSize(true)
        val friendsList = FriendsList(this, friends)
        community_rv_friends.adapter = friendsList

        friendsList.setOnClickListener(object : FriendsList.OnClickListener {

            override fun onClick(position: Int, model: LoadUserModel) {
                exposeToQuizInvitationModel(model)
                sendInvitationtoDataBase()
                toast.show()
            }
        })
    }

}