package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.LoadUserModel
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
    private var friendsPage: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_invite_msg)
        community_invite_msg_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        community_invite_msg_friends_btn.setOnClickListener(this)
        community_invite_msg_quizes_btn.setOnClickListener(this)
        community_invite_msg_firstPage.setOnClickListener(this)
        community_invite_msg_backPage.setOnClickListener(this)
        community_invite_msg_nextPage.setOnClickListener(this)
        community_invite_msg_lastPage.setOnClickListener(this)

        if (intent.hasExtra(IS_QUIZ_BTN)) {
            setQuizesBtn()
        }

        //getAllFriendInvitations()
        //getAllQuizInvitations()
        firstFive()
        friendInvitesRecyclerView(friendInvitesList)
        quizInvitesRecyclerView(quizInvitesList)
    }

    private fun getAllFriendInvitations() = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            val list = Friend.find { (Friends.to eq currentUser!!.id) and (Friends.status eq 0) }
                .with(Friend::to).toList()
            if (list.isNotEmpty())
                exposedToFriendInvitationModel(list)
        }
    }


    private fun getAllQuizInvitations() = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            val list =
                QuizInvitation.find { (QuizInvitations.to eq currentUser!!.id) and (QuizInvitations.status eq 0) }
                    .toList()
            if (list.isNotEmpty())
                exposedToQuizInvitationModel(list)
        }
    }

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
                    i.from.id.value,
                    i.to.id.value,
                    i.status,
                    i.quiz.id.value,
                )
            )
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.community_invite_msg_friends_btn -> {
                if(!friendsPage) {
                    setFriendBtn()
                    firstFive()
                }
            }
            R.id.community_invite_msg_quizes_btn -> {
                if(friendsPage) {
                    setQuizesBtn()
                    firstFive()
                }
            }
            R.id.community_invite_msg_firstPage -> {
                firstFive()
            }
            R.id.community_invite_msg_backPage -> {
                prevFive()
            }
            R.id.community_invite_msg_nextPage -> {
                nextFive()
            }
            R.id.community_invite_msg_lastPage -> {
                lastFive()
            }
        }
    }

    private fun setFriendBtn(){
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
        friendsPage = true
    }

    private fun setQuizesBtn(){
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
        friendsPage = false
    }

    private fun changeFriendInvitestatus(poz: Int, s: Int) = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            val x =
                Friend.find { (Friends.from eq friendInvitesList[poz].fromUser) and (Friends.to eq currentUser!!.id) }
                    .toList()
            x[0].status = s
        }
    }

    private fun changeQuizInvStatus(poz: Int, s: Int) = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            val x =
                QuizInvitation.find { (QuizInvitations.from eq quizInvitesList[poz].fromUser) and (QuizInvitations.to eq currentUser!!.id) }
                    .toList()
            x[0].status = s
        }
    }

    private fun restartActivity(){
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun restartQuizesActivity(){
        val intent = intent
        finish()
        intent.putExtra(IS_QUIZ_BTN, true)
        startActivity(intent)
    }

    private fun friendInvitesRecyclerView(friendInvites: ArrayList<ReadFriendInvitationModel>) {
        community_invite_rv.layoutManager = LinearLayoutManager(this)
        community_invite_rv.setHasFixedSize(true)
        val friendInvitesList = FriendInviteList(this, friendInvites)
        community_invite_rv.adapter = friendInvitesList

        friendInvitesList.setOnClickListener(
            object : FriendInviteList.OnClickListener {

                override fun onClick(position: Int, model: ReadFriendInvitationModel) {
                }
            },

            object : FriendInviteList.OnAcceptClickListener {
                override fun onClick(position: Int, model: ReadFriendInvitationModel) {
                    changeFriendInvitestatus(position, 1)
                    finish()
                    val intent = Intent(this@CommunityInviteMsg, CommunityActivity::class.java)
                    startActivity(intent)
                }
            },

            object : FriendInviteList.OnDeclineClickListener {
                override fun onClick(position: Int, model: ReadFriendInvitationModel) {
                    changeFriendInvitestatus(position, -1)
                    restartActivity()
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

        quizInvitesList.setOnClickListener(
            object : QuizInviteList.OnClickListener {

                override fun onClick(position: Int, model: ReadQuizInvitationModel) {
                }
            },

            object : QuizInviteList.OnAcceptClickListener {
                override fun onClick(position: Int, model: ReadQuizInvitationModel) {
                    changeQuizInvStatus(position, 1)
                    val intent = Intent(
                        this@CommunityInviteMsg,
                        DetailQuizActivity::class.java
                    )
                    val quizModel = runBlocking {
                        return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                            val quiz = Quiz.findById(model.quizID)
                            exposedToQuizModel(quiz!!)
                            //TODO(WITOLD) usunąć zaproszenie z bazy po wyszukaniu
                        }
                    }
                    intent.putExtra(
                        MainActivity.QUIZ_DETAILS,
                        quizModel
                    )
                    restartQuizesActivity()
                    startActivity(intent)
                }
            },

            object : QuizInviteList.OnDeclineClickListener {
                override fun onClick(position: Int, model: ReadQuizInvitationModel) {

                    changeQuizInvStatus(position, -1) //TODO(WITOLD usunąć zaproszenie do quizu
                    restartQuizesActivity()
                }
            },
        )
    }
    companion object {
        var IS_QUIZ_BTN = "isQuizBtn"
    }
    //TODO (WITOLD) paginacja zaproszeń do quizów i znaj
    private fun firstFive(){
        if(friendsPage){
            //5 zaproszeń do znaj
        }else{
            //5 zaproszeń do quizu
        }
    }
    private fun prevFive(){}
    private fun nextFive(){}
    private fun lastFive(){}

}
