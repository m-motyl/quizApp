package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.FriendInvitationModel
import com.example.licencjat_projekt.Projekt.Models.ReadFriendModel
import com.example.licencjat_projekt.Projekt.database.User
import com.example.licencjat_projekt.Projekt.utils.FriendInvite
import com.example.licencjat_projekt.Projekt.utils.FriendsList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_community_invite_msg.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class CommunityInviteMsg : AppCompatActivity() {
    private var friendInvitesList = ArrayList<FriendInvitationModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_invite_msg)
        community_invite_msg_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        //getAllInvitations()
        friendInvitesRecyclerView(friendInvitesList)
    }

    private fun exposedToInvitationModel(list: List<FriendInvitationModel>) {
        val friendInvitationsArrayList = ArrayList<FriendInvitationModel>()
        for (i in list) {
            friendInvitationsArrayList.add(
                FriendInvitationModel(
                    i.fromUser,
                    i.toUser,
                    i.isAccepted
                )
            )
        }
        friendInvitesList = friendInvitationsArrayList
    }

//    private fun getAllInvitations() {
//        val list = runBlocking {
//            return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
//                //TODO: make FriendInvitation
//                //FriendInvitation.all().toList()
//            }
//        }
//        if (list.isNotEmpty())
//            exposedToInvitationModel(list)
//    }

    private fun friendInvitesRecyclerView(friendInvites: ArrayList<FriendInvitationModel>) {
        community_invite_rv.layoutManager = LinearLayoutManager(this)
        community_invite_rv.setHasFixedSize(true)
        val friendInvitesList = FriendInvite(this, friendInvites)
        community_rv_friends.adapter = friendInvitesList

        friendInvitesList.setOnClickListener(object : FriendInvite.OnClickListener {

            override fun onClick(position: Int, model: FriendInvitationModel) {
            }
        })
    }


}
