package com.example.licencjat_projekt.Projekt.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profile_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        supportActionBar!!.title = "Profil"
    }

}