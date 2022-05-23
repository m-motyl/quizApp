package com.example.licencjat_projekt.Projekt.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.Models.FriendInvitationModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivityAdd : AppCompatActivity(), View.OnClickListener {
    private var invitationModel: FriendInvitationModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profile_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        supportActionBar!!.title = "Profil"
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.profile_add_add_friend -> {
                invitationModel = FriendInvitationModel(
                    fromUser = "from",
                    toUser = "to",
                    isAccepted = false
                )
                Toast.makeText(
                    this,
                    "Zaprosiłeś użytkownika do znajomych.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}