package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_quiz_main.*

class QuizMainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_main)

        quizmain_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        quizmain_start_creating.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.quizmain_start_creating ->{
                val intent = Intent(this, QuestionsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}