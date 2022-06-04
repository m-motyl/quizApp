package com.example.licencjat_projekt.Projekt.Activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.Models.ReadReportModel
import com.example.licencjat_projekt.Projekt.database.*
import com.example.licencjat_projekt.Projekt.utils.CurrentUserReportsList
import com.example.licencjat_projekt.Projekt.utils.QuizesList
import com.example.licencjat_projekt.Projekt.utils.ReportsList
import com.example.licencjat_projekt.Projekt.utils.currentUser
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ReportsActivity : AppCompatActivity(), View.OnClickListener {
    //private var userReports: Boolean = true
    private var othersReports: Boolean = false
    private var searchString: String? = ""
    private var quizesList = ArrayList<ReadReportModel>()
    private var offsetId = 0L
    private var quizesCount = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        setSupportActionBar(report_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        report_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar!!.title = ""
        report_firstPage.setOnClickListener(this)
        report_backPage.setOnClickListener(this)
        report_nextPage.setOnClickListener(this)
        report_lastPage.setOnClickListener(this)
        report_user_reports.setOnClickListener(this)
        report_others_reports.setOnClickListener(this)
        report_btn_search.setOnClickListener(this)

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
            R.id.report_btn_search -> {
                searchString = report_et.text.toString()
                offsetId = 0
                firstFive()
            }
            R.id.report_user_reports -> {
                if (othersReports) {
                    quizesList.clear()
                    quizesRecyclerView(quizesList)
                    //userReports = true
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
                    quizesRecyclerView(quizesList)
                    othersReports = true
                    //userReports = false

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
                getQuizesNumber()
                firstFive()
                if (othersReports) {
                    userQuizesRecyclerView(quizesList)
                } else {
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.report_backPage -> {
                getQuizesNumber()
                prevFive()
                if (othersReports) {
                    userQuizesRecyclerView(quizesList)
                } else {
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.report_nextPage -> {
                getQuizesNumber()
                nextFive()
                if (othersReports) {
                    userQuizesRecyclerView(quizesList)
                } else {
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.report_lastPage -> {
                getQuizesNumber()
                lastFive()
                if (othersReports) {
                    userQuizesRecyclerView(quizesList)
                } else {
                    quizesRecyclerView(quizesList)
                }
            }
        }
    }

    private fun firstFive() {
        this.offsetId = 0L
        getQuizesNumber()
        if (othersReports) {
            if (searchString!!.isEmpty())
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
            else
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                (Quizes.user eq currentUser!!.id) and ((Quizes.title like "$searchString%"))
                            }.limit(5)
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                    }
                }
        } else {
            if (searchString!!.isEmpty())
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
            else
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                (QuizeResults.by eq currentUser!!.id) and ((Quizes.title like "$searchString%"))
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
            if (searchString!!.isEmpty())
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
            else
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                (Quizes.user eq currentUser!!.id) and ((Quizes.title like "$searchString%"))
                            }.limit(5, offsetId)
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                        else
                            offsetId -= 5L
                    }
                }
        } else {
            if (searchString!!.isEmpty())
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
            else
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                QuizeResults.by eq currentUser!!.id and ((Quizes.title like "$searchString%"))
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
            if (searchString!!.isEmpty())
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
            else
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                Quizes.user eq currentUser!!.id and ((Quizes.title like "$searchString%"))
                            }.limit(5, offsetId)
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                        else
                            offsetId += 5L
                    }
                }
        } else {
            if (searchString!!.isEmpty())
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
            else
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                QuizeResults.by eq currentUser!!.id and ((Quizes.title like "$searchString%"))
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
            if (searchString!!.isEmpty())
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
            else
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                Quizes.user eq currentUser!!.id and ((Quizes.title like "$searchString%"))
                            }.orderBy(QuizeResults.id to SortOrder.DESC)
                                .limit((quizesCount.mod(5)))
                        val x = QuizeResult.wrapRows(query).toList()
                        if (x.isNotEmpty())
                            exposedToModel(x)
                    }
                }
        } else {
            if (searchString!!.isEmpty())
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
            else
                runBlocking {
                    newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                QuizeResults.by eq currentUser!!.id and ((Quizes.title like "$searchString%"))
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
        var xd = ""
        for (i in tmp) {
            xd += i.name + " "
        }
        return xd
    }

    private fun getQuizesNumber() {
        if (othersReports) {
            if (searchString!!.isEmpty())
                quizesCount = runBlocking {
                    return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                Quizes.user eq currentUser!!.id
                            }
                        QuizeResult.wrapRows(query).count()
                    }
                }
            else
                quizesCount = runBlocking {
                    return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                (Quizes.user eq currentUser!!.id) and ((Quizes.title like "$searchString%"))
                            }
                        QuizeResult.wrapRows(query).count()
                    }
                }
        } else {
            if (searchString!!.isNotEmpty())

                quizesCount = runBlocking {
                    return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
                                (QuizeResults.by eq currentUser!!.id) and ((Quizes.title like "$searchString%"))
                            }
                        QuizeResult.wrapRows(query).count()
                    }
                }
            else
                quizesCount = runBlocking {
                    return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
                        val query =
                            QuizeResults.innerJoin(Quizes).slice(QuizeResults.columns).select {
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

    companion object {
        var QUIZ_DETAILS = "quiz_details"
    }

}