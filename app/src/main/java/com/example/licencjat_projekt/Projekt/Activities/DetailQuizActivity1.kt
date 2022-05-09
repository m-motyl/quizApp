package com.example.licencjat_projekt.Projekt.Activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresPermission
import com.example.licencjat_projekt.Projekt.Models.ReadAnswerModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuestionModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.database.Answer
import com.example.licencjat_projekt.Projekt.database.Answers
import com.example.licencjat_projekt.Projekt.database.Answers.question
import com.example.licencjat_projekt.Projekt.database.Question
import com.example.licencjat_projekt.Projekt.database.Questions.quiz
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_detail_quiz.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DetailQuizActivity1 : AppCompatActivity(), View.OnClickListener {
    private var quizDetails: ReadQuizModel? = null
    private var questionsList = arrayListOf<ReadQuestionModel>()
    private var answersList = ArrayList<ReadAnswerModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_quiz)

        if (intent.hasExtra(MainActivity.QUIZ_DETAILS)) {
            quizDetails = intent.getSerializableExtra(MainActivity.QUIZ_DETAILS) as ReadQuizModel
        }
        if (quizDetails != null) {
            setSupportActionBar(detail_quiz_toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            detail_quiz_toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            supportActionBar!!.title = quizDetails!!.title
            detail_quiz_image.setImageBitmap(byteArrayToBitmap(quizDetails!!.image))
            detail_quiz_title.text = quizDetails!!.title
            detail_quiz_description.text = quizDetails!!.description
            detail_quiz_tags.text = quizDetails!!.tags
        }
        question_display_btn_back.setOnClickListener(this)
    }

    //decode image read from db
    private fun byteArrayToBitmap(
        data: ByteArray
    ): Bitmap {
        return BitmapFactory.decodeByteArray(
            data,
            0,
            data.size
        )
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.question_display_btn_back -> {
                readQuestions(quizDetails!!)
            }
        }
    }

    private fun readQuestions(R: ReadQuizModel) = runBlocking {
        Database.connect(
            "jdbc:postgresql://10.0.2.2:5432/db", driver = "org.postgresql.Driver",
            user = "postgres", password = "123"
        )
        var tmp = ArrayList<ReadAnswerModel>()
        val questions = newSuspendedTransaction(Dispatchers.IO) {
            Question.find{quiz eq R.id}.toList()
        }
        for (i in questions){
            var answers = newSuspendedTransaction(Dispatchers.IO) {
                Answer.find{question eq i.id}.toList()
            }
            for (j in answers){
                tmp.add(
                    ReadAnswerModel(
//                        answer_text = j.answer_text,
//                        answer_image = j.answer_image!!.bytes,
                        is_Correct = j.is_correct
                    )
                )
            }
            questionsList.add(
                ReadQuestionModel(
//                    question_text = i.question_text,
//                    question_image = i.question_image!!.bytes,
                    question_pts = i.points,
                    question_answers = tmp,
                    number = i.number,

                )
            )
            tmp.clear()
        }
    }
}