package com.example.licencjat_projekt.Projekt.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.Models.LoadUserModel
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.Projekt.database.*
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.profile_toolbar
import kotlinx.android.synthetic.main.activity_profile_add.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class ProfileActivityAdd : AppCompatActivity(), View.OnClickListener {
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
            visitatedUser =
                intent.getSerializableExtra(CommunityActivity.PROFILE_DETAILS) as LoadUserModel
        }

        profile_add_add_friend.setOnClickListener(this)

        supportActionBar!!.title = "Profil"
    }

    private fun sendFriendInvitationToDatabase() = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            Friend.new {
                status = 0
                from = currentUser!!
                to = User.findById(visitatedUser!!.id)!!
            }
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.profile_add_add_friend -> {
                var debugCurrentUser = currentUser
                sendFriendInvitationToDatabase()
                Toast.makeText(
                    this,
                    "Zaprosiłeś użytkownika do znajomych.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}