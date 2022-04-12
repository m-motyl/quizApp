package com.example.licencjat_projekt.Projekt.Activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.AnswerModel
import com.example.licencjat_projekt.Projekt.utils.AnswersList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_questions.*

class QuestionsActivity : AppCompatActivity(), View.OnClickListener{

    private var answersList = ArrayList<AnswerModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        questions_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        questions_add_button.setOnClickListener(this)
        questions_delete.setOnClickListener(this)
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
                    else -> { //TODO: repair adding models, just testing
                        val ans = AnswerModel(
                            questions_add_answer.text.toString()
                        )
                        questions_add_answer.text.clear()
                        answersList.add(ans)
                        answersRecyclerView(answersList)
                    }
                }
            }
            R.id.questions_delete -> {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Usuń:")
                val items = arrayOf(
                    "Pytanie",
                    "Ostatnia odpowiedź"
                )
                alert.setItems(items) { _, n ->
                    when (n) {
                        0 -> removeQuestion()
                        1 -> removeLastAnswer()
                    }
                }
                alert.show()
            }
        }
    }
    private fun removeQuestion(){
        //TODO: remove question repair
        questions_description.text.clear()
        questions_add_answer.text.clear()
        if(answersList.size > 0) {
            answersList.clear()
            answersRecyclerView(answersList)
        }
    }
    private fun removeLastAnswer(){
        //TODO: remove last answer repair
        if(answersList.size > 0) {
            answersList.removeLast()
            answersRecyclerView(answersList)
        }
    }

    private fun answersRecyclerView(answers: ArrayList<AnswerModel>){
        questions_recycler_view.layoutManager = LinearLayoutManager(this)
        questions_recycler_view.setHasFixedSize(true)

        val answersList = AnswersList(this, answers)
        questions_recycler_view.adapter = answersList

        answersList.setOnClickListener(object: AnswersList.OnClickListener{
            override fun onClick(position:Int, model: AnswerModel) {
                Log.e("item", model.answer)
            }
        })
    }
}