package com.example.licencjat_projekt.Projekt.Activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresPermission
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_detail_quiz.*

class DetailQuizActivity1 : AppCompatActivity(){
    private var quizDetails: ReadQuizModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_quiz)

        if(intent.hasExtra(MainActivity.QUIZ_DETAILS)){
            quizDetails = intent.getSerializableExtra(MainActivity.QUIZ_DETAILS) as ReadQuizModel
        }
        if(quizDetails != null){
            setSupportActionBar(detail_quiz_toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            detail_quiz_toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            supportActionBar!!.title = quizDetails!!.title
            detail_quiz_image.setImageBitmap(byteArrayToBitmap(quizDetails!!.image))
            detail_quiz_title.text = quizDetails!!.title
            detail_quiz_description.text = quizDetails!!.description
            detail_quiz_tags.text = quizDetails!!.tags
        }
    }

    private fun byteArrayToBitmap(
        data: ByteArray
    ): Bitmap {
        return BitmapFactory.decodeByteArray(
            data,
            0,
            data.size
        )
    }

}