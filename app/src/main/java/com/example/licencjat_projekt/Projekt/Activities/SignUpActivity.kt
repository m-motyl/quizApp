package com.example.licencjat_projekt.Projekt.Activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.Models.SignUpModel
import com.example.licencjat_projekt.Projekt.database.User
import com.example.licencjat_projekt.Projekt.database.Users
import com.example.licencjat_projekt.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_quiz_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    private var isImage: Boolean = false
    private lateinit var user_image: ByteArray
    private lateinit var signUpModel: SignUpModel

    companion object {
        internal const val GALLERY_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup_sign_up.setOnClickListener(this)
        signup_sign_in.setOnClickListener(this)
        signup_image.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.signup_sign_up -> {
                when {
                    signup_username.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj login!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    signup_email.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj e-mail!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    !isEmailValid(signup_email.text.toString()) -> {
                        Toast.makeText(
                            this,
                            "Niepoprawny mail!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    signup_password.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj hasło!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    !isImage -> {
                        Toast.makeText(
                            this,
                            "Dodaj zdjęcie!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        signUpModel = SignUpModel(
                            signup_username.text.toString(),
                            signup_email.text.toString(),
                            signup_password.text.toString(),
                            user_image
                        )
                        if (checkIfUserExists(signUpModel)) {
                            registerUser(signUpModel)
                            Toast.makeText(
                                this,
                                "Konto utworzone pomyślnie!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else
                            Toast.makeText(
                                this,
                                "Użytkownik o podanym loginie lub emailu już istnieje!",
                                Toast.LENGTH_SHORT
                            ).show()
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            R.id.signup_sign_in -> {
                finish()
            }
            R.id.signup_image -> {
                chooseImageFromGalery()
            }
        }
    }

    private fun checkIfUserExists(m: SignUpModel): Boolean {
        return !runBlocking {
            val result = newSuspendedTransaction(Dispatchers.IO) {
                User.find { Users.login eq m.login or (Users.email eq m.e_mail) }.toList()
            }
            return@runBlocking result.isNotEmpty()
        }
    }

    private fun registerUser(m: SignUpModel) = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            User.new {
                login = m.login
                password = m.password
                email = m.e_mail
                profile_picture = ExposedBlob(m.image)
                creation_time = LocalDateTime.now()
                token = UUID.randomUUID()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun chooseImageFromGalery() {
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(
                    report: MultiplePermissionsReport?
                ) {
                    if (report!!.areAllPermissionsGranted()) {
                        val galleryIntent =
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        startActivityForResult(
                            galleryIntent,
                            GALLERY_CODE
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    permissionDeniedDialog()
                }
            }).onSameThread().check()
    }

    private fun permissionDeniedDialog() {
        AlertDialog.Builder(this).setMessage("Brak uprawnień")
            .setPositiveButton("Przejdz do USTAWIEŃ")
            { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        "package",
                        packageName,
                        null
                    )
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("ANULUJ")
            { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_CODE) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val selectedImage =
                            MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                contentURI
                            )
                        user_image = saveImageByteArray(selectedImage)
                        isImage = true
                        signup_image.setImageBitmap(selectedImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            "Błąd!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun saveImageByteArray(
        bitmap: Bitmap
    ): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            stream
        )
        return stream.toByteArray()
    }
}