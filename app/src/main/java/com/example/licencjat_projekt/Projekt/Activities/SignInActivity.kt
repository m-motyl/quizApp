package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.R
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
                when{
                    signin_login.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj login!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    signin_password.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj hasło!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else ->{
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

            }
            R.id.signin_sign_up -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}