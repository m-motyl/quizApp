package com.example.licencjat_projekt.Projekt.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivityAdd : AppCompatActivity(), View.OnClickListener {
    private var invitationModel: ReadFriendInvitationModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_add)
        setSupportActionBar(profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profile_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        supportActionBar!!.title = "Profil"
    }

    //:TODO (WITEK) edytować i dopasować pola w ReadFriendInvitationModel
    private fun exposeToInvitationModel(){
        invitationModel = ReadFriendInvitationModel(
            fromUser = "from",
            toUser = "to",
            isAccepted = false
        )
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