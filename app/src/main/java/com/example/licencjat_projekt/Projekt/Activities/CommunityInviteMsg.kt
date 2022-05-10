package com.example.licencjat_projekt.Projekt.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community_invite_msg.*

class CommunityInviteMsg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_invite_msg)
        community_invite_msg_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
