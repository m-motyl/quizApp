package com.example.licencjat_projekt.Projekt.Activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.Models.LoadUserModel
import com.example.licencjat_projekt.Projekt.Models.ReadFriendInvitationModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.profile_toolbar
import kotlinx.android.synthetic.main.activity_profile_friend.*

class ProfileFriendActivity : AppCompatActivity(){
    private var user: LoadUserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_friend)
        setSupportActionBar(profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profile_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra(CommunityActivity.PROFILE_DETAILS)) {
            user =
                intent.getSerializableExtra(CommunityActivity.PROFILE_DETAILS) as LoadUserModel
        }

        profile_friend_login.text = user!!.login
        profile_friend_image.setImageBitmap(byteArrayToBitmap(user!!.profile_picture))

        supportActionBar!!.title = "Profil"
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }
}