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
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime


class CommunityQuizInviteActivity : AppCompatActivity() {
    var quizDetails: ReadQuizModel? = null

    private var friendsList = ArrayList<LoadUserModel>()

    private lateinit var toastCorrect: Toast
    private lateinit var toastIncorrect: Toast
    private lateinit var toastAuthor: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        setSupportActionBar(community_toolbar)
        community_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra(DetailQuizActivity.QUIZ_DETAILS)) {
            quizDetails =
                intent.getSerializableExtra(DetailQuizActivity.QUIZ_DETAILS) as ReadQuizModel
        }

        toastCorrect = Toast.makeText(
            this,
            "Zaprosiłeś użytkownika do quizu",
            Toast.LENGTH_SHORT
        )

        toastIncorrect = Toast.makeText(
            this,
            "Użytkownik już otrzymał zaproszenie",
            Toast.LENGTH_SHORT
        )

        toastAuthor = Toast.makeText(
            this,
            "Użytkownik jest autorem quizu",
            Toast.LENGTH_SHORT
        )

        getAllFriends()
        friendsRecyclerView(friendsList)
    }

    private fun getAllFriends() = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            val list =
                Friend.find { ((Friends.from eq currentUser!!.id) or (Friends.to eq currentUser!!.id)) and (Friends.status eq 1) }
                    .with(Friend::to, Friend::from).toList()
            if (list.isNotEmpty())
                exposedToFriendModel(list)
        }
    }


    private fun exposedToFriendModel(l: List<Friend>) {
        for (i in l) {
            if (currentUser!!.id == i.from.id)
                friendsList.add(
                    LoadUserModel(
                        id = i.to.id.value,
                        login = i.to.login,
                        profile_picture = i.to.profile_picture!!.bytes,
                        creation_time = i.to.creation_time.toString()
                    )
                )
            else
                friendsList.add(
                    LoadUserModel(
                        id = i.from.id.value,
                        login = i.from.login,
                        profile_picture = i.from.profile_picture!!.bytes,
                        creation_time = i.from.creation_time.toString()
                    )
                )
        }
        for (i in friendsList) {
            Log.e("", i.login)
            Log.e("", i.creation_time)
        }
    }

    private fun sendInvitationToDataBase(userId: Int, quizId: Int): Boolean {
        return runBlocking {
            return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                val q = Quiz.findById(quizId)!!
                val t = User.findById(userId)!!
                if (q.user != t) {
                    QuizInvitation.new {
                        status = 0
                        from = currentUser!!
                        to = t
                        quiz = q
                        time_sent = LocalDateTime.now()
                    }
                    return@newSuspendedTransaction true
                } else
                    return@newSuspendedTransaction false
            }
        }
    }

    private fun isUserInvited(userId: Int, quizId: Int): Boolean {
        return runBlocking {
            val x = newSuspendedTransaction(Dispatchers.IO) {
                QuizInvitation.find { (QuizInvitations.to eq userId) and (QuizInvitations.quiz eq quizId) }
                    .toList()
            }
            return@runBlocking !x.isEmpty()
        }
    }


    private fun friendsRecyclerView(friends: ArrayList<LoadUserModel>) {
        community_rv_friends.layoutManager = LinearLayoutManager(this)
        community_rv_friends.setHasFixedSize(true)
        val friendsList = FriendsList(this, friends)
        community_rv_friends.adapter = friendsList

        friendsList.setOnClickListener(object : FriendsList.OnClickListener {

            override fun onClick(position: Int, model: LoadUserModel) {
                val userID = model.id
                val quizID = quizDetails!!.id
                if (isUserInvited(userID, quizID)) {
                    toastIncorrect.show()
                } else {
                    if(sendInvitationToDataBase(userID, quizID)) {
                        toastCorrect.show()
                    }
                    else {
                        toastAuthor.show()
                    }
                }
                finish()
            }
        })
    }

}