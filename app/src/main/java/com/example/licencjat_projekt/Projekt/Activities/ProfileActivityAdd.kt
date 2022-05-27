package com.example.licencjat_projekt.Projekt.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.Models.LoadUserModel
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivityAdd : AppCompatActivity(), View.OnClickListener {
    private var invitationModel: ReadFriendInvitationModel? = null
    private var visitatedUser: LoadUserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_add)
        setSupportActionBar(profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profile_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra(CommunityActivity.PROFILE_DETAILS)) {
            visitatedUser = intent.getSerializableExtra(CommunityActivity.PROFILE_DETAILS) as LoadUserModel
        }

        supportActionBar!!.title = "Profil"
    }

    private fun exposeToInvitationModel() {
        if (intent.hasExtra(CommunityActivity.PROFILE_DETAILS)) {
            var visitatedUserID = visitatedUser?.id
            //TODO: (WITEK) get user from id -- ??? exposeToInvitationModel
//            invitationModel = ReadFriendInvitationModel(
//                status = 0,
//                fromUser = currentUser,
//                toUser = ,
//            )
        }
    }

    //:TODO (WITEK) wysłać invitationModel do bazy
//    private fun sendInvitationToDB(){
//        invitationModel
//    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.profile_add_add_friend -> {
                exposeToInvitationModel()
                //sendInvitationToDB()
                Toast.makeText(
                    this,
                    "Zaprosiłeś użytkownika do znajomych.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}