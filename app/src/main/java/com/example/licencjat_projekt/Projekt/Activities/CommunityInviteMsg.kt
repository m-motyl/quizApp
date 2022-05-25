package com.example.licencjat_projekt.Projekt.Activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuizInvitationModel
import com.example.licencjat_projekt.Projekt.database.Friend
import com.example.licencjat_projekt.Projekt.database.Friends
import com.example.licencjat_projekt.Projekt.utils.FriendInviteList
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_community_invite_msg.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class CommunityInviteMsg : AppCompatActivity(), View.OnClickListener {
    private var friendInvitesList = ArrayList<ReadFriendInvitationModel>()
    private var quizInvitesList = ArrayList<ReadQuizInvitationModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_invite_msg)
        community_invite_msg_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        community_invite_msg_friends_btn.setOnClickListener(this)
        community_invite_msg_quizes_btn.setOnClickListener(this)

        //getAllFriendInvitations()
        friendInvitesList.add(ReadFriendInvitationModel("fromUser", "toUser", false))
        friendInvitesRecyclerView(friendInvitesList)
        //quizInvitesRecyclerView(quizInvitesList)
    }

//    private fun getAllFriendInvitations()= runBlocking {
//        newSuspendedTransaction(Dispatchers.IO) {
//            val list = FriendInvitation.all.get()
//            if (list.isNotEmpty())
//                exposedToFriendInvitationModel(list)
//        }
//    }

//    private fun getAllQuizInvitations()= runBlocking {
//        newSuspendedTransaction(Dispatchers.IO) {
//            val list = QuizInvitation.all.get()
//            if (list.isNotEmpty())
//                exposedToFriendInvitationModel(list)
//        }
//    }

    //:TODO(Witek) zrobić FriendInvitation tabele, ReadFriendInvitationModel jest tylko trzeba go edytować
//    private fun exposedToFriendInvitationModel(list: List<FriendInvitation>) {
//        val friendInvitationsArrayList = ArrayList<FriendInvitation>()
//        for (i in list) {
//            friendInvitesList.add(
//                ReadFriendInvitationModel(
//                    i.fromUser,
//                    i.toUser,
//                    i.isAccepted
//                )
//            )
//        }
//    }

    //:TODO(Witek) zrobić FriendInvitation tabele, ReadQuizInvitationModel jest tylko trzeba go edytować
//    private fun exposedToQuizInvitationModel(list: List<QuizInvitation>) {
//        val quizInvitationsArrayList = ArrayList<QuizInvitation>()
//        for (i in list) {
//            quizInvitesList.add(
//                ReadQuizInvitationModel(
//                    i.fromUser,
//                    i.toUser,
//                    i.isAccepted
//                )
//            )
//        }
//    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.community_invite_msg_friends_btn -> {
                community_invite_msg_friends_scroll.visibility = View.VISIBLE
                community_invite_msg_quizes_scroll.visibility = View.GONE
                community_invite_msg_friends_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_05))
                community_invite_msg_quizes_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))

            }
            R.id.community_invite_msg_quizes_btn -> {
                community_invite_msg_friends_scroll.visibility = View.GONE
                community_invite_msg_quizes_scroll.visibility = View.VISIBLE
                community_invite_msg_friends_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
                community_invite_msg_quizes_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_05))
            }
        }
    }

    private fun friendInvitesRecyclerView(friendInvites: ArrayList<ReadFriendInvitationModel>) {
        community_invite_rv.layoutManager = LinearLayoutManager(this)
        community_invite_rv.setHasFixedSize(true)
        val friendInvitesList = FriendInviteList(this, friendInvites)
        community_invite_rv.adapter = friendInvitesList

        friendInvitesList.setOnClickListener(object : FriendInviteList.OnClickListener {

            override fun onClick(position: Int, model: ReadFriendInvitationModel) {
            }
        })
    }

//    private fun quizesInvitesRecyclerView(friendInvites: ArrayList<ReadQuizInvitationModel>) {
//        community_invite_rv_quizes.layoutManager = LinearLayoutManager(this)
//        community_invite_rv_quizes.setHasFixedSize(true)
//        val quizInvitesList = QuizInviteList(this, friendInvites)
//        community_invite_rv_quizes.adapter = quizInvitesList
//
//        friendInvitesList.setOnClickListener(object : QuizInviteList.OnClickListener {
//
//            override fun onClick(position: Int, model: ReadQuizInvitationModel) {
//            }
//        })
//    }


}
