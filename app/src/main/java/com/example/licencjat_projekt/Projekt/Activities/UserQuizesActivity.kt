package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.utils.QuizesList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_user_quizes.*

class UserQuizesActivity : AppCompatActivity(), View.OnClickListener {
    private var quizesList = ArrayList<ReadQuizModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_quizes)
        setSupportActionBar(userquizes_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userquizes_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        supportActionBar!!.title = "Moje quizy"
        userquizes_firstPage.setOnClickListener(this)
        userquizes_backPage.setOnClickListener(this)
        userquizes_nextPage.setOnClickListener(this)
        userquizes_lastPage.setOnClickListener(this)

        firstFive()
        quizesRecyclerView(quizesList)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.userquizes_firstPage -> {
                firstFive()
                quizesRecyclerView(quizesList)
            }
            R.id.userquizes_backPage -> {
                prevFive()
                quizesRecyclerView(quizesList)
            }
            R.id.userquizes_nextPage -> {
                nextFive()
                quizesRecyclerView(quizesList)
            }
            R.id.userquizes_lastPage -> {
                lastFive()
                quizesRecyclerView(quizesList)
            }
        }
    }
    //TODO: (WITOLD) quizy użytkownika
    private fun firstFive(){}
    private fun prevFive(){}
    private fun nextFive(){}
    private fun lastFive() {}

    private fun quizesRecyclerView(quizes: ArrayList<ReadQuizModel>) {

        userquizes_rv_quizes.layoutManager = LinearLayoutManager(this)
        userquizes_rv_quizes.setHasFixedSize(true)
        val quizesList = QuizesList(this, quizes)
        userquizes_rv_quizes.adapter = quizesList

        quizesList.setOnClickListener(object : QuizesList.OnClickListener {
            override fun onClick(position: Int, model: ReadQuizModel) {
                val intent = Intent(
                    this@UserQuizesActivity,
                    DetailQuizActivity1::class.java
                )

                intent.putExtra( //passing object to activity
                    UserQuizesActivity.QUIZ_DETAILS,
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