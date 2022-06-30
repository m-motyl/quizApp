package com.example.licencjat_projekt.Projekt.Activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.database.*
import com.example.licencjat_projekt.Projekt.utils.QuizesList
import com.example.licencjat_projekt.Projekt.utils.falseToken
import com.example.licencjat_projekt.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private var offsetId: Long = 0L
    private var quizesCount: Long = 0L
    private var quizesList = ArrayList<ReadQuizModel>()

    companion object {
        var QUIZ_DETAILS = "quiz_details"
        var QUIZ_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (falseToken()){
            val intent = Intent(this,SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("EXIT",true)
            startActivity(intent)
        }
        setContentView(R.layout.activity_main)


        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.main_nav_view)
        drawerLayout = findViewById(R.id.main_drawer_layout)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Let'sQuiz"

        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        main_firstPage.setOnClickListener(this)
        main_backPage.setOnClickListener(this)
        main_nextPage.setOnClickListener(this)
        main_lastPage.setOnClickListener(this)

        getQuizesNumber()
        firstFive()
        if(quizesList.size > 0) {
            quizesRecyclerView(quizesList)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen((GravityCompat.START))) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_menu_create_quiz -> run {
                val intent = Intent(
                    this,
                    QuizMainActivity::class.java
                )
                startActivityForResult(intent, QUIZ_CODE)
            }
            R.id.drawer_menu_profile -> run {
                val intent = Intent(
                    this,
                    ProfileActivity::class.java
                )
                startActivity(intent)
            }
            R.id.drawer_menu_community -> run {
                val intent = Intent(
                    this,
                    CommunityActivity::class.java
                )
                startActivity(intent)
            }
            R.id.drawer_menu_logout -> run {
                val intent = Intent(this,
                    SignInActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra("EXIT", true)
                startActivity(intent)
            }
            R.id.drawer_menu_search -> run {
                val intent = Intent(
                    this,
                    SearchActivity::class.java
                )
                startActivity(intent)
            }
            R.id.drawer_menu_reports -> run {
                val intent = Intent(
                    this,
                    ReportsActivity::class.java
                )
                startActivity(intent)
            }
            R.id.drawer_menu_user_quizes -> run {
                val intent = Intent(
                    this,
                    UserQuizesActivity::class.java
                )
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.main_firstPage -> {
                if(offsetId != 0L) {
                    firstFive()
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.main_backPage -> {
                if (offsetId >= 5L) {
                    previousFive()
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.main_nextPage -> {
                if (offsetId + 5 <= quizesCount) {
                    nextFive()
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.main_lastPage -> {
                if(offsetId + 5 < quizesCount){
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
                val list = Quiz.find {
                    Quizes.private eq false
                }.limit(5).toList()
                if (list.isNotEmpty())
                    exposedToModel(list)
            }
        }
    }

    private fun nextFive() {
        this.offsetId += 5L
        runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                val list = Quiz.find {
                    Quizes.private eq false
                }.limit(5, offsetId).toList()
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
                val list = Quiz.find {
                    Quizes.private eq false
                }.limit(5, offsetId).toList()
                if (list.isNotEmpty())
                    exposedToModel(list)
                else
                    offsetId += 5L
            }
        }
    }

    private fun lastFive() {
        if (quizesCount.mod(5) != 0) {
            this.offsetId = quizesCount - quizesCount.mod(5)
        } else {
            this.offsetId = quizesCount - 5
        }
        runBlocking {
            newSuspendedTransaction(Dispatchers.IO) {
                val list: List<Quiz>
                if (quizesCount.mod(5) != 0) {
                    list = Quiz.find {
                        Quizes.private eq false
                    }.orderBy(Quizes.id to SortOrder.DESC)
                        .limit((quizesCount.mod(5)))
                        .toList()
                } else {
                    list= Quiz.find {
                        Quizes.private eq false
                    }.orderBy(Quizes.id to SortOrder.DESC)
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
                Quiz.find { Quizes.private eq false }.count()
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
        var tags = ""
        for (i in tmp) {
            tags += i.name + " "
        }
        return tags
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == QUIZ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                firstFive()
                getQuizesNumber()
                if(quizesList.size > 0){
                    quizesRecyclerView(quizesList)
                }
            }
        }
    }

    private fun quizesRecyclerView(quizes: ArrayList<ReadQuizModel>) {
        main_rv_quizes.layoutManager = LinearLayoutManager(this)
        main_rv_quizes.setHasFixedSize(true)
        val quizesList = QuizesList(this, quizes)
        main_rv_quizes.adapter = quizesList

        quizesList.setOnClickListener(object : QuizesList.OnClickListener {
            override fun onClick(position: Int, model: ReadQuizModel) {
                val intent = Intent(
                    this@MainActivity,
                    DetailQuizActivity::class.java
                )

                intent.putExtra(
                    QUIZ_DETAILS,
                    model
                )
                startActivity(intent)
            }
        })
    }
}