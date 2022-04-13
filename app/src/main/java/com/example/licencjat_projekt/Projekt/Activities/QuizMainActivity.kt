package com.example.licencjat_projekt.Projekt.Activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.licencjat_projekt.Projekt.database.Quiz
import com.example.licencjat_projekt.Projekt.database.User
import com.example.licencjat_projekt.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_quiz_main.*
import org.jetbrains.exposed.sql.Op
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.jar.Manifest

class QuizMainActivity : AppCompatActivity(), View.OnClickListener {
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private lateinit var title: String
    private var time_limit: Int = 0
    private lateinit var description: String
    private lateinit var gz_text: String
    private var isPrivate: Boolean = false
    private lateinit var invitation_code: String
    private var correct_answers: Int = 0
    private var questions: Int = 0
    private var no_tries: Int = 0
    private lateinit var quiz_image: ByteArray
    private var isImage: Boolean = false
    private lateinit var user: User

    companion object {
        internal const val GALLERY_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_main)

        quizmain_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        quizmain_start_creating.setOnClickListener(this)
        quizmain_privacy.setOnClickListener(this)
        quizmain_image.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.quizmain_start_creating ->{
                when {
                    quizmain_title.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj tytuł!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    quizmain_timer.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj długość testu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    quizmain_description.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj opis!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    quizmain_final_comment.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj komentarz końcowy!",
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
                        title = quizmain_title.toString()
                        time_limit = Integer.parseInt(quizmain_timer.text.toString())
                        description = quizmain_description.toString()
                        gz_text = quizmain_final_comment.toString()
                        //private = true/ false set by checkbox
                        invitation_code = generateInvitationCode() //TODO: check if not in DB
                        //correct_answers = 0
                        //questions = 0
                        //no_tries = 0
                        //quiz_image
                        //user = getCurrentUser() //TODO: get current user*/

                        val intent = Intent(this, QuestionsActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            R.id.quizmain_privacy -> {
                isPrivate = quizmain_privacy.isChecked
            }
            R.id.quizmain_image -> {
                chooseImageFromGalery()
            }
        }
    }
    private fun generateInvitationCode(): String{
        var codeLen = 6
        return ((1..codeLen)
            .map{i->kotlin.random.Random.nextInt(0, charPool.size)}
            .map(charPool::get).joinToString(""))
    }

    private fun getCurrentUser(): User{
        return user
    }
    private fun chooseImageFromGalery(){
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
                        null)
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
                        quiz_image = saveImageByteArray(selectedImage)
                        isImage = true
                        quizmain_image.setImageBitmap(selectedImage)
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
    ): ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            stream
        )
        return stream.toByteArray()
    }
}