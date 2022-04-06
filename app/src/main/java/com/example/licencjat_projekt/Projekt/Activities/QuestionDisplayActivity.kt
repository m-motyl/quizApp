package com.example.licencjat_projekt.Projekt.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_quiz_main.*

class QuestionDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_display)

        quizmain_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}