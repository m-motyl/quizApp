package com.example.licencjat_projekt.Projekt.Activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.licencjat_projekt.Projekt.Models.AnswerModel
import com.example.licencjat_projekt.Projekt.utils.AnswersList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.android.synthetic.main.question_item.*

class QuestionsActivity : AppCompatActivity(), View.OnClickListener{

    private var answersList = ArrayList<AnswerModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        questions_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        questions_add_button.setOnClickListener(this)
        questions_delete_last.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.questions_add_button -> {
                when {
                    questions_add_answer.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Podaj odpowiedź!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> { //TODO: testowo, poprawić później
                        val ans = AnswerModel(
                            questions_add_answer.text.toString()
                        )
                        answersList.add(ans)
                        answersRecyclerView(answersList)
                    }
                }
            }
            R.id.questions_delete_last -> {
                if(answersList.size > 0){
                    answersList.removeLast()
                    answersRecyclerView(answersList)
                }
            }
        }
    }
    private fun answersRecyclerView(answers: ArrayList<AnswerModel>){
        questions_recycler_view.layoutManager = LinearLayoutManager(this)
        questions_recycler_view.setHasFixedSize(true)

        val answersList = AnswersList(this, answers)
        questions_recycler_view.adapter = answersList

        /*answersList.setOnClickListener(object: AnswersList.OnClickListener{
            override fun onClick(position:Int, model: AnswerModel) {

            }
        })*/
    }
}