package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.database.*
import com.example.licencjat_projekt.Projekt.utils.QuizesList
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ReportsActivity : AppCompatActivity(), View.OnClickListener {
    private var userReports: Boolean = true
    private var othersReports: Boolean = false
    private var quizesList = ArrayList<ReadQuizModel>()
    private var offsetId = 0L
    private var quizesCount = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        setSupportActionBar(report_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        report_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        supportActionBar!!.title = "Raporty"
        report_firstPage.setOnClickListener(this)
        report_backPage.setOnClickListener(this)
        report_nextPage.setOnClickListener(this)
        report_lastPage.setOnClickListener(this)
        report_user_reports.setOnClickListener(this)
        report_others_reports.setOnClickListener(this)

        report_user_reports.setBackgroundColor(Color.RED)
        firstFive()
        quizesRecyclerView(quizesList)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.report_user_reports -> {
                if(!userReports) {
                    userReports = true
                    othersReports = false

                    firstFive()
                    quizesRecyclerView(quizesList)

                    report_user_reports.setBackgroundColor(Color.RED)
                    report_others_reports.setBackgroundColor(Color.GRAY)
                }
            }
            R.id.report_others_reports -> {
                if(!othersReports) {
                    othersReports = true
                    userReports = false

                    firstFive()
                    quizesRecyclerView(quizesList)

                    report_user_reports.setBackgroundColor(Color.GRAY)
                    report_others_reports.setBackgroundColor(Color.RED)
                }
            }
            R.id.report_firstPage -> {
                firstFive()
                quizesRecyclerView(quizesList)
            }
            R.id.report_backPage -> {
                prevFive()
                quizesRecyclerView(quizesList)
            }
            R.id.report_nextPage -> {
                nextFive()
                quizesRecyclerView(quizesList)
            }
            R.id.report_lastPage -> {
                lastFive()
                quizesRecyclerView(quizesList)
            }
        }
    }
    //TODO: (WITOLD) reports display
    private fun firstFive(){
        this.offsetId = 0L
        if(userReports){
            //quizesList = raporty z quizow rozwiazanych przez usera
            runBlocking {
                newSuspendedTransaction(Dispatchers.IO) {
                    val query = Quizes.innerJoin(QuizeResults).slice(Quizes.columns).select {
                        QuizeResults.by eq currentUser!!.id
                    }.limit(5).withDistinct()
                    val x = Quiz.wrapRows(query).toList()
                    if(x.isNotEmpty())
                        exposedToModel(x)
                }
            }
        }
        if(othersReports){
            //quizesList = raporty z quizów usera rozwiązanych przez innych
            runBlocking {
                newSuspendedTransaction(Dispatchers.IO) {
                    val query = Quizes.innerJoin(QuizeResults).slice(Quizes.columns).select {
                        Quizes.user eq currentUser!!.id
                    }.limit(5).withDistinct()
                    val x = Quiz.wrapRows(query).toList()
                    if(x.isNotEmpty())
                        exposedToModel(x)
                }
            }
        }


    }
    private fun prevFive(){

    }
    private fun nextFive(){

    }
    private fun lastFive(){

    }
    private fun quizesRecyclerView(quizes: ArrayList<ReadQuizModel>) {

        report_rv_quizes.layoutManager = LinearLayoutManager(this)
        report_rv_quizes.setHasFixedSize(true)
        val quizesList = QuizesList(this, quizes)
        report_rv_quizes.adapter = quizesList

        quizesList.setOnClickListener(object : QuizesList.OnClickListener {
            override fun onClick(position: Int, model: ReadQuizModel) {
                val intent = Intent(
                    this@ReportsActivity,
                    DetailQuizActivity::class.java
                )

                intent.putExtra( //passing object to activity
                    ReportsActivity.QUIZ_DETAILS,
                    model
                )
                startActivity(intent)
            }
        })
    }
    private fun getQuizTags(q: Quiz): String {
        val tmp = runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                val query = Tags.innerJoin(QuizTags).slice(Tags.columns).select {
                    QuizTags.quiz eq q.id
                }
                return@newSuspendedTransaction Tag.wrapRows(query).toList()
            }
        }
        var xd = ""
        for (i in tmp) {
            xd += i.name + " "
        }
        return xd
    }

    private fun getQuizesNumber(str: String) {
        val n = runBlocking {
            return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                Quiz.find { (Quizes.title.lowerCase() like "$str%") and (Quizes.private eq false) }
                    .count()
            }
        }
        quizesCount = n
    }

    private fun exposedToModel(list: List<Quiz>) {

        val quizesArrayList = ArrayList<ReadQuizModel>()
        for (i in list) {
            getQuizTags(i)
            quizesArrayList.add(
                ReadQuizModel(
                    i.id.value,
                    i.title,
                    i.time_limit,
                    i.description,
                    getQuizTags(i),
                    i.gz_text,
                    i.private,
                    i.invitation_code,
                    i.image.bytes,
                    i.user.login,
                    i.questions,
                )
            )
        }
        quizesList = quizesArrayList
    }
    companion object {
        var QUIZ_DETAILS = "quiz_details"
    }
}