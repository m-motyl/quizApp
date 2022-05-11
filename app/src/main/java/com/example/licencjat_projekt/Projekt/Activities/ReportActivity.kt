package com.example.licencjat_projekt.Projekt.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity(), View.OnClickListener {

    private var quizScore = 0
    private var userScore = 0.0
    private var gz_text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        if (intent.hasExtra(QuestionsShowActivity.QUIZ_SCORE)) {
            quizScore = intent.getSerializableExtra(QuestionsShowActivity.QUIZ_SCORE) as Int
            report_max_score.text = quizScore.toString() + ".0"
        }
        if (intent.hasExtra(QuestionsShowActivity.USER_SCORE)) {
            userScore = intent.getSerializableExtra(QuestionsShowActivity.USER_SCORE) as Double
            if(!userScore.isNaN()) {
                report_user_score.text = userScore.toString()
            }else{
                report_user_score.text = "0.0"
            }
        }
        if (intent.hasExtra(QuestionsShowActivity.FINAL_MESSAGE)) {
            gz_text = intent.getSerializableExtra(QuestionsShowActivity.FINAL_MESSAGE) as String
            report_author_message.text = gz_text
        }

        report_home.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.report_home -> {
                finish()
            }
        }
    }
}