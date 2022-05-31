package com.example.licencjat_projekt.Projekt.Activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.database.QuizeResult
import com.example.licencjat_projekt.Projekt.database.QuizeResults
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ProfileActivity : AppCompatActivity(), View.OnClickListener{
    private var passwordChange: Boolean = false
    private var newPassword: String? = null
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
        profile_password_change.setOnClickListener(this)
        profile_password_update.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.profile_password_update -> {
                newPassword = profile_change_pasword.text.toString()

                if(newPassword!!.length < 2 || newPassword!!.length > 20)
                {
                    Toast.makeText(
                        this,
                        "Nowe hasło powinno zawierać od 2 do 20 znaków!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else
                {

                    updatePassword(newPassword!!)

                    tv_profile_password_inv.text = newPassword
                    tv_profile_password_vis.text = newPassword

                    profile_change_pasword.text.clear()

                    profile_password_change_rl.visibility = View.GONE
                    passwordChange = false
                }
            }
            R.id.profile_password_change -> {
                if(passwordChange){
                    passwordChange = false
                    profile_password_change_rl.visibility = View.GONE
                }else{
                    passwordChange = true
                    profile_password_change_rl.visibility = View.VISIBLE
                }
            }
            R.id.profile_show_password -> {
                if (tv_profile_password_inv.visibility == View.VISIBLE) {
                    tv_profile_password_inv.visibility = View.GONE
                    tv_profile_password_vis.visibility = View.VISIBLE
                } else {
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

    private fun userQuizTaken(): String {
        return runBlocking {
            return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                return@newSuspendedTransaction QuizeResult.find { QuizeResults.by eq currentUser!!.id }
                    .count().toString()
            }
        }
    }

    private fun updatePassword(str: String) = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            currentUser!!.password = str
        }
    }
}