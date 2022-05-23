package com.example.licencjat_projekt.Projekt.Activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profile_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        supportActionBar!!.title = "Profil"
        if(currentUser != null){
            profile_main_login.text = currentUser!!.login
            profile_username.text = currentUser!!.login
            profile_email.text = currentUser!!.email
            tv_profile_password_vis.text = currentUser!!.password
            tv_profile_password_inv.text = currentUser!!.password
            profile_image.setImageBitmap(byteArrayToBitmap(currentUser!!.profile_picture!!.bytes))
            profile_quiz_taken.text = userQuizTaken()
        }
        profile_show_password.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.profile_show_password -> {
                if(tv_profile_password_inv.visibility == View.VISIBLE){
                    tv_profile_password_inv.visibility = View.GONE
                    tv_profile_password_vis.visibility = View.VISIBLE
                }
                else{
                    tv_profile_password_inv.visibility = View.VISIBLE
                    tv_profile_password_vis.visibility = View.GONE
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
    private fun userQuizTaken() : String{ //TODO: (WITOLD) liczba rozwiazanych quizów
        return "1"
    }
}