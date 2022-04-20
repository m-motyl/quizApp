package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.Models.LoginModel
import com.example.licencjat_projekt.Projekt.database.Questions
import com.example.licencjat_projekt.Projekt.database.Quizes
import com.example.licencjat_projekt.Projekt.database.User
import com.example.licencjat_projekt.Projekt.database.Users
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var loginModel: LoginModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signin_login.setOnClickListener(this)
        signin_sign_up.setOnClickListener(this)
        connectToDb()
    }

    private fun connectToDb() {
        runBlocking {
            Database.connect(
                "jdbc:postgresql://10.0.2.2:5432/db", driver = "org.postgresql.Driver",
                user = "postgres", password = "123"
            )
            newSuspendedTransaction(Dispatchers.IO) {
                SchemaUtils.create(Questions)
                SchemaUtils.create(Quizes)
                SchemaUtils.create(Users)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.signin_login -> {
                when {
                    signin_username.text.isNullOrEmpty() -> {
                        //xd
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
                    else -> {
                        loginModel = LoginModel(
                            signin_username.text.toString(),
                            signin_password.text.toString()
                        )
                        if (validateUserAndUpdateToken(loginModel)) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }

            }
            R.id.signin_sign_up -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validateUserAndUpdateToken(loginModel: LoginModel): Boolean {
        return runBlocking {
            val result = newSuspendedTransaction(Dispatchers.IO) {
                val resultado =
                    User.find { Users.login eq loginModel.login and (Users.password eq loginModel.password) }
                        .toList()
                if (resultado.isNotEmpty())
                    resultado.elementAt(0).token = UUID.randomUUID()
                return@newSuspendedTransaction resultado

            }
            if (result.isNotEmpty()) {
                currentUser = result.elementAt(0)
            }
            return@runBlocking result.isNotEmpty()
        }
    }
}