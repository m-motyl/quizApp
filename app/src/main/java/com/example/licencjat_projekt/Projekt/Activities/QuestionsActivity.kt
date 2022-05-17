package com.example.licencjat_projekt.Projekt.Activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.example.licencjat_projekt.Projekt.database.*
import com.example.licencjat_projekt.Projekt.utils.AnswersList
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.ByteArrayOutputStream
import java.io.IOException

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var answersList = ArrayList<AnswerModel>()
    private val emptyByteArray: ByteArray = ByteArray(1)
    private var quizModel: CreateQuizModel? = null
    private var noQuestions = 0
    private var questionsList = arrayListOf<CreateQuestionModel>()
    private var question_image: ByteArray = ByteArray(1)
    private var selectCorrect: Boolean = false
    private var removeAnswers: Boolean = false
    private var isImage: Boolean = false

    companion object {
        internal const val GALLERY_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        questions_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra(QuizMainActivity.QUIZ_DETAILS)) {
            quizModel =
                intent.getSerializableExtra(QuizMainActivity.QUIZ_DETAILS) as CreateQuizModel
        }

        questions_add_button.setOnClickListener(this)
        questions_delete.setOnClickListener(this)
        questions_prev_question.setOnClickListener(this)
        questions_next_question.setOnClickListener(this)
        questions_image.setOnClickListener(this)
        questions_save_correct_ans.setOnClickListener(this)
        questions_finish_quiz.setOnClickListener(this)
        questions_image_delete.setOnClickListener(this)
        questions_end_del.setOnClickListener(this)
        questions_end_correct_ans.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.questions_end_correct_ans -> {
                selectCorrect = false
                questions_end_correct_ans.visibility = View.GONE
            }
            R.id.questions_end_del -> {
                removeAnswers = false
                questions_end_del.visibility = View.GONE
            }
            R.id.questions_image_delete -> {
                questions_image.setImageResource(R.drawable.add_screen_image_placeholder)
                if (questionsList.getOrNull(noQuestions) != null) {
                    questionsList[noQuestions].question_image = emptyByteArray
                }
                questions_image_delete.visibility = View.GONE
                isImage = false
            }

            R.id.questions_finish_quiz -> {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Czy na pewno chcesz zakończyć tworzenie quizu?")
                val items = arrayOf(
                    "Tak",
                    "Nie"
                )
                alert.setItems(items) { _, n ->
                    when (n) {
                        0 -> saveQuizToDB(quizModel!!, questionsList)
                        1 -> goBack()
                    }
                }
                alert.show()
            }
            R.id.questions_save_correct_ans -> {
                selectCorrect = true
                questions_end_correct_ans.visibility = View.VISIBLE
                /*if (selectCorrect) {
                    questions_save_correct_ans.setImageResource(R.drawable.ic_baseline_star_24)
                    selectCorrect = false
                } else {
                    questions_save_correct_ans.setImageResource(R.drawable.ic_baseline_star_24_black)
                    selectCorrect = true
                }*/
            }
            R.id.questions_image -> {
                chooseImageFromGalery()
            }
            R.id.questions_prev_question -> {
                //if not empty
                if (!questions_question.text.isNullOrEmpty() && !questions_points.text.isNullOrEmpty()) {
                    if (questionsList.getOrNull(noQuestions) == null) { //create new if not exists
                        lateinit var questionModel: CreateQuestionModel
                        if (isImage) {
                            questionModel = CreateQuestionModel(
                                questions_question.text.toString(),
                                question_image,
                                Integer.parseInt(questions_points.text.toString()),
                                ArrayList(answersList)
                            )
                        } else {
                            questionModel = CreateQuestionModel(
                                questions_question.text.toString(),
                                emptyByteArray,
                                Integer.parseInt(questions_points.text.toString()),
                                ArrayList(answersList)
                            )
                        }
                        question_image = emptyByteArray
                        isImage = false
                        answersList.clear()
                        questionsList.add(questionModel)
                    } else { //update if exists
                        questionsList[noQuestions].question_text =
                            questions_question.text.toString()
                        if (isImage) {
                            questionsList[noQuestions].question_image = question_image
                            isImage = false
                        }
                        questionsList[noQuestions].question_pts =
                            Integer.parseInt(questions_points.text.toString())
                        questionsList[noQuestions].question_answers += answersList
                        answersList.clear()
                    }
                } else {
                    answersList.clear()
                }

                if (noQuestions > 0) {
                    noQuestions -= 1
                }

                if (questionsList.getOrNull(noQuestions) != null) { //read element if exists
                    questions_question.setText(questionsList[noQuestions].question_text)
                    if (!questionsList[noQuestions].question_image.contentEquals(emptyByteArray)) {
                        questions_image_delete.visibility = View.VISIBLE
                        questions_image.setImageBitmap(byteArrayToBitmap(questionsList[noQuestions].question_image))
                    } else {
                        questions_image_delete.visibility = View.GONE
                        questions_image.setImageResource(R.drawable.add_screen_image_placeholder)
                    }
                    questions_points.setText(questionsList[noQuestions].question_pts.toString())
                    answersRecyclerView(questionsList[noQuestions].question_answers)
                } else {
                    questions_image_delete.visibility = View.GONE
                    answersRecyclerView(answersList)
                }
            }
            R.id.questions_next_question -> {
                if (!questions_question.text.isNullOrEmpty() && !questions_points.text.isNullOrEmpty()) {
                    if (questionsList.getOrNull(noQuestions) == null) { //create new if not exists
                        lateinit var questionModel: CreateQuestionModel
                        if (isImage) {
                            questionModel = CreateQuestionModel(
                                questions_question.text.toString(),
                                question_image,
                                Integer.parseInt(questions_points.text.toString()),
                                ArrayList(answersList)
                            )
                        } else {
                            questionModel = CreateQuestionModel(
                                questions_question.text.toString(),
                                emptyByteArray,
                                Integer.parseInt(questions_points.text.toString()),
                                ArrayList(answersList)
                            )
                        }
                        question_image = emptyByteArray
                        isImage = false
                        answersList.clear()
                        questionsList.add(questionModel)
                    } else { //update if exists
                        questionsList[noQuestions].question_text =
                            questions_question.text.toString()
                        if (isImage) {
                            questionsList[noQuestions].question_image = question_image
                            isImage = false
                        }
                        questionsList[noQuestions].question_pts =
                            Integer.parseInt(questions_points.text.toString())
                        questionsList[noQuestions].question_answers += answersList
                        answersList.clear()
                    }

                    questions_question.text.clear()
                    questions_points.text.clear()
                    questions_image.setImageResource(R.drawable.add_screen_image_placeholder)

                    noQuestions += 1
                }

                if (questionsList.getOrNull(noQuestions) != null) { //read element if exists

                    questions_question.setText(questionsList[noQuestions].question_text)
                    if (!questionsList[noQuestions].question_image.contentEquals(emptyByteArray)) {
                        questions_image_delete.visibility = View.VISIBLE
                        questions_image.setImageBitmap(byteArrayToBitmap(questionsList[noQuestions].question_image))
                    } else {
                        questions_image_delete.visibility = View.GONE
                        questions_image.setImageResource(R.drawable.add_screen_image_placeholder)
                    }
                    questions_points.setText(questionsList[noQuestions].question_pts.toString())
                    answersRecyclerView(questionsList[noQuestions].question_answers)
                } else {
                    questions_image_delete.visibility = View.GONE
                    answersRecyclerView(answersList)
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
                    else -> {
                        val ans = AnswerModel(
                            questions_add_answer.text.toString(),
                            emptyByteArray,
                            false
                        )
                        questions_add_answer.text.clear()
                        answersList.add(ans)
                        if (questionsList.getOrNull(noQuestions) != null) {
                            answersRecyclerView(
                                (questionsList[noQuestions].question_answers + answersList)
                                        as ArrayList<AnswerModel>
                            )
                        } else {
                            answersRecyclerView(answersList)
                        }
                    }
                }
            }
            R.id.questions_delete -> {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Usuń:")
                val items = arrayOf(
                    "Pytanie",
                    "Odpowiedzi"
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

    private fun removeQuestion() {
        if (questionsList.getOrNull(noQuestions) != null) {
            questionsList.removeAt(noQuestions)

            if (questionsList.getOrNull(0) != null) {
                noQuestions = 0
                questions_question.setText(questionsList[0].question_text)
                questions_points.setText(questionsList[0].question_pts.toString())
                answersRecyclerView(questionsList[0].question_answers)
                if (!questionsList[0].question_image.contentEquals(emptyByteArray)) {
                    questions_image_delete.visibility = View.VISIBLE
                    questions_image.setImageBitmap(byteArrayToBitmap(questionsList[0].question_image))
                } else {
                    questions_image_delete.visibility = View.GONE
                    questions_image.setImageResource(R.drawable.add_screen_image_placeholder)
                }
            } else if (questionsList.getOrNull(0) == null) {
                questions_image.setImageResource(R.drawable.add_screen_image_placeholder)
                questions_image_delete.visibility = View.GONE
                isImage = false
                questions_question.text.clear()
                questions_points.text.clear()
                answersList.clear()
                answersRecyclerView(answersList)
            }
        } else if (questionsList.getOrNull(noQuestions) == null) {
            questions_image.setImageResource(R.drawable.add_screen_image_placeholder)
            questions_image_delete.visibility = View.GONE
            isImage = false
            questions_question.text.clear()
            questions_points.text.clear()
            answersList.clear()
            answersRecyclerView(answersList)
        }
    }

    private fun removeMarkedAnswers() {
        removeAnswers = true
        questions_end_del.visibility = View.VISIBLE
        if (questionsList.getOrNull(noQuestions) != null) {
            questionsList[noQuestions].question_answers += answersList
            answersList.clear()
        }
    }

    private fun answersRecyclerView(answers: ArrayList<AnswerModel>) {
        questions_recycler_view.layoutManager = LinearLayoutManager(this)
        questions_recycler_view.setHasFixedSize(true)

        val ansList = AnswersList(this, answers)
        questions_recycler_view.adapter = ansList

        ansList.setOnClickListener(object : AnswersList.OnClickListener {
            override fun onClick(position: Int, model: AnswerModel) {
                if (selectCorrect) {
                    if (model.is_Correct) { //temporary
                        model.answer_text = model.answer_text.dropLast(9)
                        model.is_Correct = false
                    } else {
                        model.answer_text += "(correct)"
                        model.is_Correct = true
                    }
                    questions_recycler_view.adapter = ansList
                }
                if (removeAnswers) {
                    if (questionsList.getOrNull(noQuestions) != null) {
                        if (answersList.size == 0) {
                            answersList = ArrayList(questionsList[noQuestions].question_answers)
                        }
                        answersList.removeAt(position)
                        questionsList[noQuestions].question_answers.clear()
                        answersRecyclerView(answersList)
                    } else if (questionsList.getOrNull(noQuestions) == null) {
                        answersList.removeAt(position)
                        answersRecyclerView(answersList)
                    }
                }
            }
        })
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
                        question_image = saveImageByteArray(selectedImage)
                        questions_image_delete.visibility = View.VISIBLE
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
    ): ByteArray {
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
    ): Bitmap {
        return BitmapFactory.decodeByteArray(
            data,
            0,
            data.size
        )
    }

    private fun saveQuizToDB(q: CreateQuizModel, ql: ArrayList<CreateQuestionModel>) = runBlocking {
        val t = q.tags.split(" ").toTypedArray()
        Database.connect(
            "jdbc:postgresql://10.0.2.2:5432/db", driver = "org.postgresql.Driver",
            user = "postgres", password = "123"
        )
        if (!questions_question.text.isNullOrEmpty() && !questions_points.text.isNullOrEmpty()
            && questionsList.getOrNull(noQuestions) == null
        ) {
            val questionModel = CreateQuestionModel(
                questions_question.text.toString(),
                question_image,
                Integer.parseInt(questions_points.text.toString()),
                ArrayList(answersList)
            )
            ql.add(questionModel)
        } else if (!questions_question.text.isNullOrEmpty() && !questions_points.text.isNullOrEmpty()
            && questionsList.getOrNull(noQuestions) != null
        ) {

            if (isImage) {
                questionsList[noQuestions].question_image = question_image
                isImage = false
            }
            questionsList[noQuestions].question_text =
                questions_question.text.toString()
            questionsList[noQuestions].question_pts =
                Integer.parseInt(questions_points.text.toString())
            questionsList[noQuestions].question_answers += answersList
        }
        var xd = 0
        for (i in ql){
            xd += i.question_pts
        }
        newSuspendedTransaction(Dispatchers.IO) {
            val newQuiz = Quiz.new {
                title = q.title
                time_limit = q.time_limit
                description = q.description
                gz_text = q.gz_text
                private = q.private
                invitation_code = q.invitation_code
                questions = ql.size
                no_tries = 0
                image = ExposedBlob(q.image)
                user = currentUser!!
                max_points = xd
            }
            for (i in t) {
                val findTag = Tag.find { Tags.name eq i }.toList()
                if (findTag.isEmpty()) {
                    var newTag = Tag.new {
                        name = i
                    }
                    QuizTag.new {
                        tag = newTag
                        quiz = newQuiz
                    }
                } else {
                    QuizTag.new {
                        tag = findTag[0]
                        quiz = newQuiz
                    }
                }
            }
            var n = -1
            for (i in ql) {
                n++
                val newQuestion = Question.new {
                    number = n
                    question_text = i.question_text
                    question_image = ExposedBlob(i.question_image)
                    points = i.question_pts
                    quiz = newQuiz
                }
                for (j in i.question_answers) {
                    Answer.new {
                        answer_text = j.answer_text
                        answer_image = ExposedBlob(j.answer_image)
                        is_correct = j.is_Correct
                        question = newQuestion
                    }
                }
            }
        }
        addTagsDB(q.tags.split(" ").toTypedArray())
        finish()
    }

    private fun addTagsDB(t: Array<String>) = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {

        }
    }

    private fun goBack() {}
}