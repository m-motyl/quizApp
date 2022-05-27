package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_detail_quiz.*

class DetailQuizActivity : AppCompatActivity(), View.OnClickListener {
    private var quizDetails: ReadQuizModel? = null

    companion object {
        var QUESTION_DETAILS = "question_details"
        var QUIZ_DETAILS = "quiz_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_quiz)

        if (intent.hasExtra(MainActivity.QUIZ_DETAILS)) {
            quizDetails = intent.getSerializableExtra(MainActivity.QUIZ_DETAILS) as ReadQuizModel
        }
        if (intent.hasExtra(UserQuizesActivity.QUIZ_DETAILS)) {
            quizDetails = intent.getSerializableExtra(UserQuizesActivity.QUIZ_DETAILS) as ReadQuizModel
        }
        if (intent.hasExtra(SearchActivity.QUIZ_DETAILS)) {
            quizDetails = intent.getSerializableExtra(SearchActivity.QUIZ_DETAILS) as ReadQuizModel
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
            detail_quiz_timer.text = quizDetails!!.time_limit.toString() + " minut(y)"
        }
        question_get_started.setOnClickListener(this)
        detail_quiz_author_name.text = getAuthorName()
        detail_quiz_number_questions.text = getNOQuestions()
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.question_get_started -> {
                val intent = Intent(
                    this,
                    QuestionsShowActivity::class.java
                )
                intent.putExtra(QUESTION_DETAILS, quizDetails)
                startActivity(intent)
                finish()
            }
            R.id.detail_quiz_invite_btn -> {
                val intent = Intent(this,
                CommunityQuizInviteActivity::class.java
                )
                intent.putExtra(QUIZ_DETAILS, quizDetails)
                startActivity(intent)
            }
        }
    }

    private fun getAuthorName(): String{
        return quizDetails!!.author
    }
    private fun getNOQuestions(): String{
        return quizDetails!!.no_questions.toString()
    }
}