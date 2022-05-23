package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_user_quizes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserQuizesActivity : AppCompatActivity(), View.OnClickListener {
    private var quizesList = ArrayList<ReadQuizModel>()
    private var offsetId: Long = 0L
    private var quizesCount: Long = 0L
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
        getQuizesNumber()
        firstFive()
        quizesRecyclerView(quizesList)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.userquizes_firstPage -> {
                if(offsetId != 0L) {
                    firstFive()
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.userquizes_backPage -> {
                if(offsetId >= 5L) {
                    previousFive()
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.userquizes_nextPage -> {
                if(offsetId + 5 <= quizesCount) {
                    nextFive()
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.userquizes_lastPage -> {
                if(offsetId + 5 < quizesCount) {
                    lastFive()
                    quizesRecyclerView(quizesList)
                }
            }
        }
    }
    private fun firstFive() {
        this.offsetId = 0L
        runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                val list = Quiz.find { Quizes.user eq currentUser!!.id }.limit(5).toList()
                if (list.isNotEmpty())
                    exposedToModel(list)
            }
        }
    }

    private fun nextFive() {
        this.offsetId += 5L
        runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                val list = Quiz.find { Quizes.user eq currentUser!!.id }.limit(5, offsetId).toList()
                if (list.isNotEmpty())
                    exposedToModel(list)
                else
                    offsetId -= 5L
            }
        }
    }

    private fun previousFive() {
        this.offsetId -= 5L
        runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                val list = Quiz.find { Quizes.user eq currentUser!!.id }.limit(5, offsetId).toList()
                if (list.isNotEmpty())
                    exposedToModel(list)
                else
                    offsetId += 5L
            }
            Log.e("prev", "$offsetId")
        }
    }

    private fun lastFive() {
        if (quizesCount.mod(5) != 0) {
            this.offsetId = quizesCount - quizesCount.mod(5)
        } else {
            this.offsetId = quizesCount - 5
        }
        Log.e("last", "$offsetId")
        runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                var list = emptyList<Quiz>()
                if (quizesCount.mod(5) != 0) {
                    list = Quiz.find { Quizes.user eq currentUser!!.id }.orderBy(Quizes.id to SortOrder.DESC)
                        .limit((quizesCount.mod(5)))
                        .toList()
                } else {
                    list= Quiz.find { Quizes.user eq currentUser!!.id }.orderBy(Quizes.id to SortOrder.DESC)
                        .limit(5)
                        .toList()
                }
                if (list.isNotEmpty())
                    exposedToModel(list.reversed())
            }
        }

    }

    private fun getQuizesNumber() {
        val n = runBlocking {
            return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                Quiz.find { Quizes.user eq currentUser!!.id }.count()
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
        for (i in quizesArrayList) {
            Log.e("xd", i.author)
        }
        quizesList = quizesArrayList
    }

    private fun getQuizTags(q: Quiz): String {
        val tmp = runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                val query = Tags.innerJoin(QuizTags).slice(Tags.columns).select {
                    QuizTags.quiz eq q.id
                }.withDistinct()
                return@newSuspendedTransaction Tag.wrapRows(query).toList()
            }
        }
        var xd = ""
        for (i in tmp) {
            xd += i.name + " "
        }
        return xd
    }

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