package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadReportModel
import com.example.licencjat_projekt.Projekt.database.*
import com.example.licencjat_projekt.Projekt.utils.*
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ReportsActivity : AppCompatActivity(), View.OnClickListener {
    private var othersReports: Boolean = false
    private var quizesList = ArrayList<ReadReportModel>()
    private var offsetId = 0L
    private var quizesCount = 0L
    companion object {
        var QUIZ_DETAILS = "quiz_details"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        if (falseToken()){
            val intent = Intent(this,SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("EXIT",true)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        setSupportActionBar(report_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        report_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar!!.title = "Raporty"
        report_firstPage.setOnClickListener(this)
        report_backPage.setOnClickListener(this)
        report_nextPage.setOnClickListener(this)
        report_lastPage.setOnClickListener(this)
        report_user_reports.setOnClickListener(this)
        report_others_reports.setOnClickListener(this)

        report_user_reports.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.purple_05
            )
        )
        getQuizesNumber()
        firstFive()
        quizesRecyclerView(quizesList)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.report_user_reports -> {
                if (othersReports) {
                    quizesList.clear()
                    quizesRecyclerView(quizesList)
                    othersReports = false

                    offsetId = 0L
                    firstFive()
                    quizesRecyclerView(quizesList)

                    report_user_reports.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.purple_05
                        )
                    )
                    report_others_reports.setBackgroundColor(Color.GRAY)
                }
            }
            R.id.report_others_reports -> {
                if (!othersReports) {
                    quizesList.clear()
                    userQuizesRecyclerView(quizesList)
                    othersReports = true

                    offsetId = 0L
                    firstFive()
                    userQuizesRecyclerView(quizesList)

                    report_user_reports.setBackgroundColor(Color.GRAY)
                    report_others_reports.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.purple_05
                        )
                    )
                }
            }
            R.id.report_firstPage -> {
                if (offsetId != 0L) {
                    firstFive()
                    if (othersReports) {
                        userQuizesRecyclerView(quizesList)
                    } else {
                        quizesRecyclerView(quizesList)
                    }
                }
            }
            R.id.report_backPage -> {
                if (offsetId >= 5L) {
                    prevFive()
                    if (othersReports) {
                        userQuizesRecyclerView(quizesList)
                    } else {
                        quizesRecyclerView(quizesList)
                    }
                }
            }
            R.id.report_nextPage -> {
                if (offsetId + 5 <= quizesCount) {
                    nextFive()
                    if (othersReports) {
                        userQuizesRecyclerView(quizesList)
                    } else {
                        quizesRecyclerView(quizesList)
                    }
                }
            }
            R.id.report_lastPage -> {
                if (offsetId + 5 < quizesCount) {
                    lastFive()
                    if (othersReports) {
                        userQuizesRecyclerView(quizesList)
                    } else {
                        quizesRecyclerView(quizesList)
                    }
                }
            }
        }
    }

    private fun firstFive() {
        this.offsetId = 0L
        getQuizesNumber()
        if (othersReports) {
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                Quizes.user eq currentUser!!.id
                            }.limit(5)
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                    }
                }
        } else {
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                QuizeResults.by eq currentUser!!.id
                            }.limit(5)
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                    }
                }
        }
    }

    private fun nextFive() {
        getQuizesNumber()
        this.offsetId += 5L
        if (othersReports) {
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                Quizes.user eq currentUser!!.id
                            }.limit(5, offsetId)
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                        else
                            offsetId -= 5L
                    }
                }
        } else {
            runBlocking {
                newSuspendedTransaction(Dispatchers.IO) {
                    val query =
                        QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                            QuizeResults.by eq currentUser!!.id
                        }.limit(5, offsetId)
                    val x = QuizeResult.wrapRows(query).toList()
                    if (x.isNotEmpty())
                        exposedToModel(x)
                    else
                        offsetId -= 5L
                }
            }
        }
    }

    private fun prevFive() {
        getQuizesNumber()
        this.offsetId -= 5L
        if (othersReports) {
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                Quizes.user eq currentUser!!.id
                            }.limit(5, offsetId)
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                        else
                            offsetId += 5L
                    }
                }
        } else {
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                QuizeResults.by eq currentUser!!.id
                            }.limit(5, offsetId)
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                        else
                            offsetId += 5L
                    }
                }
        }
    }

    private fun lastFive() {
        getQuizesNumber()
        if (quizesCount.mod(5) != 0) {
            this.offsetId = quizesCount - quizesCount.mod(5)
        } else {
            this.offsetId = quizesCount - 5
        }
        if (othersReports) {
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                Quizes.user eq currentUser!!.id
                            }.orderBy(QuizeResults.id to SortOrder.DESC)
                                .limit((quizesCount.mod(5)))
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                    }
                }
        } else {
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                QuizeResults.by eq currentUser!!.id
                            }.orderBy(QuizeResults.id to SortOrder.DESC)
                                .limit((quizesCount.mod(5)))
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                    }
                }
        }
    }

    private fun quizesRecyclerView(quizes: ArrayList<ReadReportModel>) {

        report_rv_quizes.layoutManager = LinearLayoutManager(this)
        report_rv_quizes.setHasFixedSize(true)
        val quizesList = ReportsList(this, quizes)
        report_rv_quizes.adapter = quizesList

        quizesList.setOnClickListener(object : ReportsList.OnClickListener {
            override fun onClick(position: Int, model: ReadReportModel) {
                val intent = Intent(
                    this@ReportsActivity,
                    DetailReportActivity::class.java
                )

                intent.putExtra( //passing object to activity
                    QUIZ_DETAILS,
                    model
                )
                startActivity(intent)
            }
        })
    }

    private fun userQuizesRecyclerView(quizes: ArrayList<ReadReportModel>) {

        report_rv_quizes.layoutManager = LinearLayoutManager(this)
        report_rv_quizes.setHasFixedSize(true)
        val quizesList = CurrentUserReportsList(this, quizes)
        report_rv_quizes.adapter = quizesList

        quizesList.setOnClickListener(object : CurrentUserReportsList.OnClickListener {
            override fun onClick(position: Int, model: ReadReportModel) {
                val intent = Intent(
                    this@ReportsActivity,
                    DetailReportActivity::class.java
                )

                intent.putExtra( //passing object to activity
                    QUIZ_DETAILS,
                    model
                )
                startActivity(intent)
            }
        })
    }

    private fun getQuizTags(q: Quiz): String {
        val tmp = runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                val query = Tags.innerJoin(QuizTags).slice(Tags.columns).select {
                    QuizTags.quiz eq q.id
                }
                return@newSuspendedTransaction Tag.wrapRows(query).toList()
            }
        }

        var retTag = ""
        for (i in tmp) {
            retTag += i.name + " "
        }
        return retTag
    }

    private fun getQuizesNumber() {
        if (othersReports) {
                quizesCount = runBlocking {
                    return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(
                                QuizeResults.columns
                            ).select {
                                Quizes.user eq currentUser!!.id
                            }
                        QuizeResult.wrapRows(query).count()
                    }
                }
        } else {
                quizesCount = runBlocking {
                    return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(
                                QuizeResults.columns
                            ).select {
                                QuizeResults.by eq currentUser!!.id
                            }
                        QuizeResult.wrapRows(query).count()
                    }
                }
        }

    }

    private fun exposedToModel(list: List<QuizeResult>) {

        val quizesArrayList = ArrayList<ReadReportModel>()
        for (i in list) {
            quizesArrayList.add(
                ReadReportModel(
                    id = i.quiz.id.value,
                    title = i.quiz.title,
                    time_limit = i.quiz.time_limit,
                    description = i.quiz.description,
                    tags = getQuizTags(i.quiz),
                    invitation_code = i.quiz.invitation_code,
                    image = i.quiz.image.bytes,
                    author = i.quiz.user.login,
                    no_questions = i.quiz.questions,
                    points = i.points,
                    by = i.by.id.value,
                    max_points = i.quiz.max_points
                )
            )
        }
        quizesList = quizesArrayList
    }

}