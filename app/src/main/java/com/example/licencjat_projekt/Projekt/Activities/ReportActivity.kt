package com.example.licencjat_projekt.Projekt.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.licencjat_projekt.Projekt.Models.CreateQuizModel
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.Models.ReportModel
import com.example.licencjat_projekt.Projekt.database.Quiz
import com.example.licencjat_projekt.Projekt.database.QuizeResult
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ReportActivity : AppCompatActivity(), View.OnClickListener {

    private var reportModel: ReportModel? = null
    private var quizDetails: ReadQuizModel? = null
    private var userIsAuthor: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        if(intent.hasExtra(QuestionsShowActivity.USER_DETAILS)){
            userIsAuthor = intent.getSerializableExtra(QuestionsShowActivity.USER_DETAILS)
                    as Boolean
        }
        if(intent.hasExtra(QuestionsShowActivity.QUIZ_SCORE)) {
            reportModel = intent.getSerializableExtra(QuestionsShowActivity.QUIZ_SCORE)
                    as ReportModel
            if(!userIsAuthor) {
                if (!reportModel!!.userPoints.isNaN()) {
                    report_user_score.text = reportModel!!.userPoints.toString()
                } else {
                    report_user_score.text = "0.0"
                }
            }else{
                report_slash.visibility = View.GONE
                report_user_score.textSize = 20F
                report_user_score.text = "Punkty do zdobycia:"
            }
            report_max_score.text = reportModel!!.maxPoints.toString() + ".0"
        }

        if (intent.hasExtra(QuestionsShowActivity.QUIZ_SCORE)) {
            quizDetails = intent.getSerializableExtra(QuestionsShowActivity.QUIZ_DETAILS)
                    as ReadQuizModel
            report_author_message.text = quizDetails!!.gz_text.toString()
        }

        if(reportModel != null && !userIsAuthor){
            saveQuizResult()
        }
        report_home.setOnClickListener(this)
    }

    private fun saveQuizResult() = runBlocking {
        Database.connect(
            "jdbc:postgresql://10.0.2.2:5432/db", driver = "org.postgresql.Driver",
            user = "postgres", password = "123"
        )
        newSuspendedTransaction(Dispatchers.IO) {
            QuizeResult.new {
                points = reportModel!!.userPoints
                by = currentUser!!
                quiz = Quiz.findById(quizDetails!!.id)!!
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.report_home -> {
                finish()
            }
        }
    }
}