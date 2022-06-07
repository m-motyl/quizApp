package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.Models.LoadUserModel
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.Projekt.database.Friend
import com.example.licencjat_projekt.Projekt.database.Friends
import com.example.licencjat_projekt.Projekt.database.QuizeResult
import com.example.licencjat_projekt.Projekt.database.QuizeResults
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.profile_toolbar
import kotlinx.android.synthetic.main.activity_profile_friend.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ProfileFriendActivity : AppCompatActivity(), View.OnClickListener {
    private var user: LoadUserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_friend)
        setSupportActionBar(profile_friend_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profile_friend_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra(CommunityActivity.PROFILE_DETAILS)) {
            user =
                intent.getSerializableExtra(CommunityActivity.PROFILE_DETAILS) as LoadUserModel
        }

        profile_friend_login.text = user!!.login
        profile_friend_image.setImageBitmap(byteArrayToBitmap(user!!.profile_picture))
        profile_friend_quiz_taken.text = userQuizTaken()

        supportActionBar!!.title = ""
        profile_add_delete_friend.setOnClickListener(this)
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    private fun userQuizTaken(): String {
        return runBlocking {
            return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                return@newSuspendedTransaction QuizeResult.find { QuizeResults.by eq user!!.id }
                    .count().toString()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.profile_add_delete_friend -> {
                deleteFriend()
                finish()
                val intent = Intent(this@ProfileFriendActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun deleteFriend() = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            Friends.deleteWhere {
                ((Friends.from eq currentUser!!.id) and (Friends.to eq user!!.id)) or
                        ((Friends.to eq currentUser!!.id) and (Friends.from eq user!!.id))
            }
        }
    }
}