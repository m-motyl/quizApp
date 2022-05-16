package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.AnswerModel
import com.example.licencjat_projekt.Projekt.Models.ReadAnswerModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuestionModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.database.Answer
import com.example.licencjat_projekt.Projekt.database.Answers
import com.example.licencjat_projekt.Projekt.database.Question
import com.example.licencjat_projekt.Projekt.database.Questions
import com.example.licencjat_projekt.Projekt.utils.AnswersList
import com.example.licencjat_projekt.Projekt.utils.DisplayQuestionsAnswers
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_question_display.*
import kotlinx.android.synthetic.main.activity_question_display.question_display_image
import kotlinx.android.synthetic.main.activity_question_display.question_display_title
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.android.synthetic.main.activity_questions.questions_recycler_view
import kotlinx.android.synthetic.main.activity_questions_show.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class QuestionsShowActivity : AppCompatActivity(), View.OnClickListener {
    private var quizDetails: ReadQuizModel? = null
    private var questionsList = arrayListOf<ReadQuestionModel>()
    private val emptyByteArray: ByteArray = ByteArray(1)
    private var noQuestions = 0
    private var quizScore = 0
    private var userScore = 0.0

    companion object {
        var QUIZ_DETAILS = "quiz_details"
        var QUIZ_SCORE = "quiz_score"
        var USER_SCORE = "user_score"
        var FINAL_MESSAGE = "final_message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_show)


        if (intent.hasExtra(DetailQuizActivity1.QUESTION_DETAILS)) {
            quizDetails = intent.getSerializableExtra(DetailQuizActivity1.QUESTION_DETAILS)
                    as ReadQuizModel
        }
        readQuestions(quizDetails!!)

        if(questionsList.size > 0){
            question_display_title.text = questionsList[0].question_text
            if(!questionsList[0].question_image.contentEquals(emptyByteArray)) {
                question_display_image.visibility = View.VISIBLE
                question_display_image.setImageBitmap(byteArrayToBitmap(questionsList[0].question_image!!))
            }else{
                question_display_image.visibility = View.GONE
            }
            answersRecyclerView(questionsList[0].question_answers)
            questionsshow_points.text = questionsList[0].question_pts.toString()
            questionsshow_current_question.text = 1.toString()
            question_display_btn_back.visibility = View.GONE
            questionsshow_no_questions.text = questionsList.size.toString()
        }
        getQuizScore()
        question_display_btn_next.setOnClickListener(this)
        question_display_btn_back.setOnClickListener(this)
    }
    private fun readQuestions(R: ReadQuizModel) = runBlocking {
        Database.connect(
            "jdbc:postgresql://10.0.2.2:5432/db", driver = "org.postgresql.Driver",
            user = "postgres", password = "123"
        )
        var tmp = ArrayList<ReadAnswerModel>()
        val questions = newSuspendedTransaction(Dispatchers.IO) {
            Question.find{ Questions.quiz eq R.id}.toList()
        }
        for (i in questions){
            var answers = newSuspendedTransaction(Dispatchers.IO) {
                Answer.find{ Answers.question eq i.id}.toList()
            }
            for (j in answers){
                tmp.add(
                    ReadAnswerModel(
                        answer_text = j.answer_text,
                        answer_image = j.answer_image!!.bytes,
                        is_Correct = j.is_correct
                    )
                )
            }
            Log.e("rozmiar z funkcji: " , tmp.size.toString())
            questionsList.add(
                ReadQuestionModel(
                    question_text = i.question_text,
                    question_image = i.question_image!!.bytes,
                    question_pts = i.points,
                    question_answers = ArrayList(tmp),
                    number = i.number,
                    )
            )
            questionsList.sortBy{it.number}
            tmp.clear()
        }
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

    private fun answersRecyclerView(answers: ArrayList<ReadAnswerModel>) {
        questions_show_recycler_view.layoutManager = LinearLayoutManager(this)
        questions_show_recycler_view.setHasFixedSize(true)

        val ansList = DisplayQuestionsAnswers(this, answers)
        questions_show_recycler_view.adapter = ansList

        ansList.setOnClickListener(object : DisplayQuestionsAnswers.OnClickListener {
            override fun onClick(position: Int, model: ReadAnswerModel) {
                if(model.is_Selected){
                    model.answer_text = model.answer_text!!.dropLast(3)
                    model.is_Selected = false
                }else{
                    model.answer_text += "(*)"
                    model.is_Selected = true
                }
                questions_show_recycler_view.adapter = ansList
            }
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.question_display_btn_next -> {
                if(questionsList.size - 1 > noQuestions){
                    noQuestions++
                    questionsshow_current_question.text = (noQuestions + 1).toString()
                    if(noQuestions == questionsList.size - 1){
                        question_display_btn_next.text = "ZAKONCZ"
                        question_display_btn_back.visibility = View.VISIBLE
                    }else{
                        question_display_btn_back.visibility = View.VISIBLE
                    }


                    question_display_title.text = questionsList[noQuestions].question_text
                    questionsshow_points.text = questionsList[noQuestions].question_pts.toString()
                    if(!questionsList[noQuestions].question_image.contentEquals(emptyByteArray)) {
                        question_display_image.visibility = View.VISIBLE
                        question_display_image.setImageBitmap(byteArrayToBitmap(questionsList[noQuestions].question_image!!))
                    }else{
                        question_display_image.visibility = View.GONE
                    }
                    answersRecyclerView(questionsList[noQuestions].question_answers)

                }
                else if(questionsList.size - 1 == noQuestions){
                    val intent = Intent(
                        this,
                        ReportActivity::class.java
                    )
                    getUserScore()
                    intent.putExtra(QUIZ_SCORE, quizScore)
                    intent.putExtra(USER_SCORE, userScore)
                    intent.putExtra(FINAL_MESSAGE, quizDetails!!.gz_text)
                    intent.putExtra(QUIZ_DETAILS, quizDetails)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.question_display_btn_back -> {

                if(noQuestions > 0){
                    noQuestions--
                    questionsshow_current_question.text = (noQuestions + 1).toString()

                    if(noQuestions == 0){
                        question_display_btn_back.visibility = View.GONE
                        question_display_btn_next.text = ">"
                    }else{
                        question_display_btn_next.text = ">"
                    }

                    question_display_title.text = questionsList[noQuestions].question_text
                    questionsshow_points.text = questionsList[noQuestions].question_pts.toString()
                    if(!questionsList[noQuestions].question_image.contentEquals(emptyByteArray)) {
                        question_display_image.visibility = View.VISIBLE
                        question_display_image.setImageBitmap(byteArrayToBitmap(questionsList[noQuestions].question_image!!))
                    }else{
                        question_display_image.visibility = View.GONE
                    }
                    answersRecyclerView(questionsList[noQuestions].question_answers)
                }
            }
        }
    }
    fun getQuizScore(){
        for (i in questionsList){
            quizScore += i.question_pts
        }
    }
    fun getUserScore(){

        for (i in questionsList){

            var correct = 0.0
            var wrong = 1.0
            var allCorrect = 0.0

            for(j in i.question_answers){
                if(j.is_Selected && j.is_Correct){
                    correct++
                }
                else if(j.is_Selected && !j.is_Correct){
                    wrong++
                }

                if(j.is_Correct){
                    allCorrect++
                }
            }
            userScore += i.question_pts * ((correct / wrong)/ allCorrect)
        }
    }

}