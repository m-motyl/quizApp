package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.utils.QuizesList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.activity_search.*

class ReportsActivity : AppCompatActivity(), View.OnClickListener {
    private var userReports: Boolean = true
    private var othersReports: Boolean = false
    private var quizesList = ArrayList<ReadQuizModel>()
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
        if(userReports){
            //quizesList = raporty z quizow rozwiazanych przez usera
        }
        if(othersReports){
            //quizesList = raporty z quizów usera rozwiązanych przez innych
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
    companion object {
        var QUIZ_DETAILS = "quiz_details"
    }
}