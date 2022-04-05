package com.example.licencjat_projekt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signin_login.setOnClickListener(this)
        signin_sign_up.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.signin_login -> {
                val intent = Intent(this, MainActivity::class.java)
                //val intent = Intent(this, MainActivity::class.java) #to start main activity
                startActivity(intent)
            }
            R.id.signin_sign_up -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}