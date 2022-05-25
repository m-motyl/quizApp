package com.example.licencjat_projekt.Projekt.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.Projekt.utils.FriendInviteList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_community_invite_msg.*

class CommunityInviteMsg : AppCompatActivity() {
    private var friendInvitesList = ArrayList<ReadFriendInvitationModel>()
    private lateinit var friendInvitesAdapter: FriendInviteList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_invite_msg)
        community_invite_msg_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        //getAllInvitations()
        friendInvitesRecyclerView(friendInvitesList)
    }

    private fun exposedToInvitationModel(list: List<ReadFriendInvitationModel>) {
        val friendInvitationsArrayList = ArrayList<ReadFriendInvitationModel>()
        for (i in list) {
            friendInvitationsArrayList.add(
                ReadFriendInvitationModel(
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

    private fun friendInvitesRecyclerView(friendInvites: ArrayList<ReadFriendInvitationModel>) {
        community_invite_rv.layoutManager = LinearLayoutManager(this)
        community_invite_rv.setHasFixedSize(true)
        val friendInvitesList = FriendInviteList(this, friendInvites)
        community_rv_friends.adapter = friendInvitesList

        friendInvitesList.setOnClickListener(object : FriendInviteList.OnClickListener {

            override fun onClick(position: Int, model: ReadFriendInvitationModel) {
            }
        })
    }


}
