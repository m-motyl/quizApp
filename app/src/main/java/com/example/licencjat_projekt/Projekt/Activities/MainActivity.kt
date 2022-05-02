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
import com.example.licencjat_projekt.Projekt.Activities.CommunityActivity
import com.example.licencjat_projekt.Projekt.Activities.QuizMainActivity
import com.example.licencjat_projekt.Projekt.Activities.SignInActivity
import com.example.licencjat_projekt.Projekt.Models.ReadQuizModel
import com.example.licencjat_projekt.Projekt.utils.QuizesList
import com.example.licencjat_projekt.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private var offsetId: Long = 0L
    private var quizesCount: Long = 0L
    private var quizesList = ArrayList<ReadQuizModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.main_nav_view)
        drawerLayout = findViewById(R.id.main_drawer_layout)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        //navigation drawer menu

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
                startActivity(intent)
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
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.main_firstPage -> {
                firstFive()
                quizesRecyclerView(quizesList)

            }
            R.id.main_backPage -> {
                if(offsetId >= 5L) {
                    previousFive()
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.main_nextPage -> {
                if(offsetId + 5 <= quizesCount) {
                    nextFive()
                    quizesRecyclerView(quizesList)
                }
            }
            R.id.main_lastPage -> {
                lastFive()
                quizesRecyclerView(quizesList)
            }
        }
    } //TODO: read pages WITEK
    private fun firstFive() {}
    private fun previousFive() {}
    private fun nextFive() {}
    private fun lastFive() {}

    //TODO: get number of quizes WITEK
    private fun getQuizesNumber(){
        quizesCount = 0
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

                intent.putExtra( //passing object to activity
                    QUIZ_DETAILS,
                    model
                )
                startActivityForResult(intent, QUIZ_CODE)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == QUIZ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                firstFive()
                getQuizesNumber()
                if (quizesList.size > 0) {
                    //changing no places string with the recycler view list
                    main_rv_quizes.visibility = View.VISIBLE
                    main_no_quizes.visibility = View.GONE
                    quizesRecyclerView(quizesList)
                } else {
                    //when there are no places right announcement shown
                    main_rv_quizes.visibility = View.GONE
                    main_no_quizes.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        var QUIZ_DETAILS = "quiz_details"
        var QUIZ_CODE = 3
    }
}