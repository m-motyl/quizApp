package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuizInvitationModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.database.*
import com.example.licencjat_projekt.Projekt.utils.FriendInviteList
import com.example.licencjat_projekt.Projekt.utils.QuizInviteList
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community_invite_msg.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
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

        getAllFriendInvitations()
        friendInvitesRecyclerView(friendInvitesList)
        quizInvitesRecyclerView(quizInvitesList)
    }

        //:TODO (WITEK) zaprszenie dostaje osoba która wysłała a nie która powinna otrzymać
    private fun getAllFriendInvitations() = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            val list = Friend.find { (Friends.from eq currentUser!!.id) and (Friends.status eq 0) }
                .with(Friend::to).toList()
            if (list.isNotEmpty())
                exposedToFriendInvitationModel(list)
        }
    }

    //:TODO(Witek)

//    private fun getAllQuizInvitations()= runBlocking {
//        newSuspendedTransaction(Dispatchers.IO) {
//            val list = QuizInvitation.all.get()
//            if (list.isNotEmpty())
//                exposedToQuizInvitationModel(list)
//        }
//    }

    private fun exposedToFriendInvitationModel(list: List<Friend>) {
        for (i in list) {
            friendInvitesList.add(
                ReadFriendInvitationModel(
                    fromUser = i.from.id,
                    toUser = i.to.id,
                    status = i.status
                )
            )
        }
    }

    private fun exposedToQuizInvitationModel(list: List<QuizInvitation>) {
        for (i in list) {
            quizInvitesList.add(
                ReadQuizInvitationModel(
                    i.from.id,
                    i.to.id,
                    i.status,
                    i.quiz.id,
                )
            )
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.community_invite_msg_friends_btn -> {
                community_invite_msg_friends_scroll.visibility = View.VISIBLE
                community_invite_msg_quizes_scroll.visibility = View.GONE
                community_invite_msg_friends_btn.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_05
                    )
                )
                community_invite_msg_quizes_btn.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.gray
                    )
                )

            }
            R.id.community_invite_msg_quizes_btn -> {
                community_invite_msg_friends_scroll.visibility = View.GONE
                community_invite_msg_quizes_scroll.visibility = View.VISIBLE
                community_invite_msg_friends_btn.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.gray
                    )
                )
                community_invite_msg_quizes_btn.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_05
                    )
                )
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
        },

            object : FriendInviteList.OnAcceptClickListener {
                override fun onClick(position: Int, model: ReadFriendInvitationModel) {
                    Log.e("button ACCEPT dziala", "tak")
                    //:TODO(Witek) zmienić status zaproszenia na 1 (model to model zaproszenia)
                    getAllFriendInvitations()
                    friendInvitesRecyclerView(friendInvites)
                }
            },

            object : FriendInviteList.OnDeclineClickListener {
                override fun onClick(position: Int, model: ReadFriendInvitationModel) {
                    Log.e("button DECLINE dziala", "tak")
                    //:TODO(Witek) zmienić status zaproszenia na -1 (model to model zaproszenia)
                    getAllFriendInvitations()
                    friendInvitesRecyclerView(friendInvites)
                }
            },
        )
    }

    private fun getQuizTags(q: Quiz): String {
        val tmp = runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                val query = Tags.innerJoin(QuizTags).slice(Tags.columns).select {
                    QuizTags.quiz eq q.id
                }.withDistinct()
                return@newSuspendedTransaction Tag.wrapRows(query).toList()
            }
        }
        var tags = ""
        for (i in tmp) {
            tags += i.name + " "
        }
        return tags
    }

    private fun exposedToQuizModel(quiz: Quiz): ReadQuizModel {

            getQuizTags(quiz)
            var quizModel =
                ReadQuizModel(
                    quiz.id.value,
                    quiz.title,
                    quiz.time_limit,
                    quiz.description,
                    getQuizTags(quiz),
                    quiz.gz_text,
                    quiz.private,
                    quiz.invitation_code,
                    quiz.image.bytes,
                    quiz.user.login,
                    quiz.questions,
                )

        return quizModel
    }

    private fun quizInvitesRecyclerView(quizInvites: ArrayList<ReadQuizInvitationModel>) {
        community_invite_rv_quizes.layoutManager = LinearLayoutManager(this)
        community_invite_rv_quizes.setHasFixedSize(true)
        val quizInvitesList = QuizInviteList(this, quizInvites)
        community_invite_rv_quizes.adapter = quizInvitesList

        quizInvitesList.setOnClickListener(object : QuizInviteList.OnClickListener {

            override fun onClick(position: Int, model: ReadQuizInvitationModel) {
            }
        },

            object : QuizInviteList.OnAcceptClickListener {
                override fun onClick(position: Int, model: ReadQuizInvitationModel) {
                    Log.e("button ACCEPT dziala", "tak")
                    //:TODO(Witek) zmienić status zaproszenia na 1 (model to model zaproszenia)
                    val intent = Intent(
                        this@CommunityInviteMsg,
                        DetailQuizActivity::class.java
                    )
                    var quiz = Quiz.findById(model.quizID)
                    var quizModel = exposedToQuizModel(quiz!!)
                    intent.putExtra(
                        MainActivity.QUIZ_DETAILS,
                        quizModel
                    )
                    startActivity(intent)
                }
            },

            object : QuizInviteList.OnDeclineClickListener {
                override fun onClick(position: Int, model: ReadQuizInvitationModel) {
                    Log.e("button DECLINE dziala", "tak")
                    //:TODO(Witek) zmienić status zaproszenia na -1 (model to model zaproszenia)
                    getAllFriendInvitations()
                    quizInvitesRecyclerView(quizInvites)
                }
            },
        )
    }


}
