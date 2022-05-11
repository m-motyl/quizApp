package com.example.licencjat_projekt.Projekt.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_question_display.*
import kotlinx.android.synthetic.main.activity_quiz_main.*

class QuestionDisplayActivity : AppCompatActivity() {
    private var answersList = ArrayList<AnswerModel>()
    private val numberOfColumns = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_display)

        quizmain_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }

        if(null!=question_display_image.drawable)
        {
            question_display_image.visibility = View.VISIBLE
        }else{
            question_display_image.visibility = View.GONE
        }
        answersRecyclerView(answersList)
    }

    private fun answersRecyclerView(answers: ArrayList<AnswerModel>){
        question_display_recycler_view.layoutManager = GridLayoutManager(this, numberOfColumns, GridLayoutManager.VERTICAL, false)
        question_display_recycler_view.setHasFixedSize(true)

        val answersList = AnswersList(this, answers)
        question_display_recycler_view.adapter = answersList

        answersList.setOnClickListener(object: AnswersList.OnClickListener{
            override fun onClick(position: Int, model: AnswerModel) {
            }
        })
    }
}