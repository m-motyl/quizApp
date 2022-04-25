package com.example.licencjat_projekt.Projekt.Activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.AnswerModel
import com.example.licencjat_projekt.Projekt.Models.CreateQuestionModel
import com.example.licencjat_projekt.Projekt.Models.CreateQuizModel
import com.example.licencjat_projekt.Projekt.Models.QuizModel
import com.example.licencjat_projekt.Projekt.utils.AnswersList
import com.example.licencjat_projekt.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.android.synthetic.main.activity_quiz_main.*
import kotlinx.android.synthetic.main.question_item.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class QuestionsActivity : AppCompatActivity(), View.OnClickListener{

    private var answersList = ArrayList<AnswerModel>()
    private val emptyByteArray: ByteArray = ByteArray(1)
    private var quizModel: CreateQuizModel? = null
    private var noQuestions = 0
    private var questionsList = arrayListOf<CreateQuestionModel>()
    private lateinit var question_image: ByteArray
    private var isImage: Boolean = false

    companion object{
        internal const val GALLERY_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        questions_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }

        if(intent.hasExtra(QuizMainActivity.QUIZ_DETAILS)){
            quizModel = intent.getSerializableExtra(QuizMainActivity.QUIZ_DETAILS) as CreateQuizModel
        }

        questions_add_button.setOnClickListener(this)
        questions_delete.setOnClickListener(this)
        questions_prev_question.setOnClickListener(this)
        questions_next_question.setOnClickListener(this)
        questions_image.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.questions_image -> {
                chooseImageFromGalery()
            }
            R.id.questions_prev_question -> {
                //if not empty
                if(!questions_question.text.isNullOrEmpty() && isImage && !questions_points.text.isNullOrEmpty()){
                    if(questionsList.getOrNull(noQuestions) == null) { //create new if not exists
                        val questionModel = CreateQuestionModel(
                            questions_question.text.toString(),
                            question_image,
                            Integer.parseInt(questions_points.text.toString())
                        )
                        questionsList.add(questionModel)
                    }else{ //update if exists
                        questionsList[noQuestions].question_text = questions_question.text.toString()
                        questionsList[noQuestions].question_image = question_image
                        questionsList[noQuestions].question_pts = Integer.parseInt(questions_points.text.toString())
                    }
                }

                if(noQuestions > 0){
                    noQuestions -= 1
                }

                if(questionsList.getOrNull(noQuestions) != null){ //read element if exists
                    isImage = true
                    questions_question.setText(questionsList[noQuestions].question_text)
                    questions_image.setImageBitmap(byteArrayToBitmap(questionsList[noQuestions].question_image))
                    questions_points.setText(questionsList[noQuestions].question_pts.toString())
                }
            }
            R.id.questions_next_question -> {
                if(!questions_question.text.isNullOrEmpty() && isImage && !questions_points.text.isNullOrEmpty()){
                    if(questionsList.getOrNull(noQuestions) == null) { //create new if not exists
                        val questionModel = CreateQuestionModel(
                            questions_question.text.toString(),
                            question_image,
                            Integer.parseInt(questions_points.text.toString())


                        )
                        questionsList.add(questionModel)
                    }else{ //update if exists
                        questionsList[noQuestions].question_text = questions_question.text.toString()
                        questionsList[noQuestions].question_image = question_image
                        questionsList[noQuestions].question_pts = Integer.parseInt(questions_points.text.toString())
                    }

                    questions_question.text.clear()
                    questions_points.text.clear()
                    questions_image.setImageResource(R.drawable.add_screen_image_placeholder)

                    noQuestions += 1
                }

                if(questionsList.getOrNull(noQuestions) != null){ //read element if exists
                    isImage = true
                    questions_question.setText(questionsList[noQuestions].question_text)
                    questions_image.setImageBitmap(byteArrayToBitmap(questionsList[noQuestions].question_image))
                    questions_points.setText(questionsList[noQuestions].question_pts.toString())
                }
            }
            R.id.questions_add_button -> {
                when {
                    questions_add_answer.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj odpowiedź!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> { //TODO: repair adding models, just testing
                        val ans = AnswerModel(
                            questions_add_answer.text.toString(),
                            emptyByteArray,
                            false
                        )
                        questions_add_answer.text.clear()
                        answersList.add(ans)
                        answersRecyclerView(answersList)
                    }
                }
            }
            R.id.questions_delete -> {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Usuń:")
                val items = arrayOf(
                    "Pytanie",
                    "Zaznaczone odpowiedzi"
                )
                alert.setItems(items) { _, n ->
                    when (n) {
                        0 -> removeQuestion()
                        1 -> removeMarkedAnswers()
                    }
                }
                alert.show()
            }
        }
    }
    private fun removeQuestion(){
        //TODO: remove question repair
        questions_question.text.clear()
        questions_add_answer.text.clear()
        if(answersList.size > 0) {
            answersList.clear()
            answersRecyclerView(answersList)
        }
    }
    private fun removeMarkedAnswers(){
        //TODO: remove answers repair
    }

    private fun answersRecyclerView(answers: ArrayList<AnswerModel>){
        questions_recycler_view.layoutManager = LinearLayoutManager(this)
        questions_recycler_view.setHasFixedSize(true)

        val answersList = AnswersList(this, answers)
        questions_recycler_view.adapter = answersList

        answersList.setOnClickListener(object: AnswersList.OnClickListener{
            override fun onClick(position:Int, model: AnswerModel) {
            }
        })
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
                        question_image = saveImageByteArray(selectedImage)
                        isImage = true
                        questions_image.setImageBitmap(selectedImage)
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

    private fun byteArrayToBitmap(
        data: ByteArray
    ): Bitmap{
        return BitmapFactory.decodeByteArray(
            data,
            0,
            data.size
        )
    }

}