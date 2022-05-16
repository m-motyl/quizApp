package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.utils.QuizesList
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), View.OnClickListener {
    var searchString: String? = null
    private var offsetId: Long = 0L
    private var searchCode: Boolean = false
    private var quizesList = ArrayList<ReadQuizModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(search_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        search_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        supportActionBar!!.title = ""
        search_firstPage.setOnClickListener(this)
        search_backPage.setOnClickListener(this)
        search_nextPage.setOnClickListener(this)
        search_lastPage.setOnClickListener(this)
        search_btn_search.setOnClickListener(this)
        search_checkBox.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.search_btn_search -> {
                searchString = search_et.text.toString()
                if(searchString != null){
                    firstFive(searchString!!)
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.search_checkBox -> {
                searchCode = search_checkBox.isChecked
            }
            R.id.search_firstPage -> {
                if(searchString != null){
                    firstFive(searchString!!)
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.search_backPage -> {
                if(searchString != null){
                    prevFive(searchString!!)
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.search_nextPage -> {
                if(searchString != null){
                    nextFive(searchString!!)
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.search_lastPage -> {
                if(searchString != null){
                    lastFive(searchString!!)
                    quizesRecyclerView(quizesList)
                }
            }
        }
    }
    //TODO (WITOLD) wyszukiwanie ze stringiem
    private fun firstFive(str: String){
        if(searchCode) {
            //wyszukiwanie prywatnych i publicznych z identycznym kodem zaproszenia (1 wynik)
            //quizesList
        }else{
            //wyszukiwanie publicznych z podobnym stringiem
        }
    }
    private fun prevFive(str: String){
        if(searchCode) {
        }else{
        }
    }
    private fun nextFive(str: String){
        if(searchCode) {
        }else{
        }
    }
    private fun lastFive(str: String){
        if(searchCode) {
        }else{
        }
    }
    private fun quizesRecyclerView(quizes: ArrayList<ReadQuizModel>) {

        search_rv_quizes.layoutManager = LinearLayoutManager(this)
        search_rv_quizes.setHasFixedSize(true)
        val quizesList = QuizesList(this, quizes)
        search_rv_quizes.adapter = quizesList

        quizesList.setOnClickListener(object : QuizesList.OnClickListener {
            override fun onClick(position: Int, model: ReadQuizModel) {

            }
        })
    }
    companion object {
        var QUIZ_DETAILS = "quiz_details"
    }
}