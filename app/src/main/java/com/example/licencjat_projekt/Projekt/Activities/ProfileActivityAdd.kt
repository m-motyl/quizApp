package com.example.licencjat_projekt.Projekt.Activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
    private var invitationSend: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_add)
        setSupportActionBar(profileadd_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profileadd_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra(CommunityActivity.PROFILE_DETAILS)) {
            visitatedUser =
                intent.getSerializableExtra(CommunityActivity.PROFILE_DETAILS) as LoadUserModel
        }

        profile_add_add_friend.setOnClickListener(this)

        supportActionBar!!.title = ""
        if(visitatedUser != null){
            profileadd_main_login.text = visitatedUser!!.login
            profileadd_image.setImageBitmap(byteArrayToBitmap(visitatedUser!!.profile_picture))
            profileadd_quiz_taken.text = userQuizTaken()
        }
        checkIfInvitationSend()
        if(invitationSend){
            profile_add_add_friend.text = "Zaproszenie zostało wysłane..."
        }
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
                if(!invitationSend) {
                    sendFriendInvitationToDatabase()
                    Toast.makeText(
                        this,
                        "Zaprosiłeś użytkownika do znajomych.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        }
    }
    private fun byteArrayToBitmap(
        data: ByteArray
    ): Bitmap {
        return BitmapFactory.decodeByteArray(
            data,
            0,
            data.size
        )
    }

    private fun userQuizTaken(): String {
        return runBlocking {
            return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                return@newSuspendedTransaction QuizeResult.find { QuizeResults.by eq visitatedUser!!.id }
                    .count().toString()
            }
        }
    }
    private fun checkIfInvitationSend(){ //TODO (WITOLD) sprawdzic czy zaproszenie do znaj. już wysłane
        invitationSend = false
    }
}