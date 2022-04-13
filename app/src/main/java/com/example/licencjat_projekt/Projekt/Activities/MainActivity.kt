package com.example.licencjat_projekt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.licencjat_projekt.Projekt.Activities.CommunityActivity
import com.example.licencjat_projekt.Projekt.Activities.QuizMainActivity
import com.example.licencjat_projekt.Projekt.Activities.SignInActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.main_nav_view)
        drawerLayout = findViewById(R.id.main_drawer_layout)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "QuizApp"

        //navigation drawer menu

        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
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
                val intent = Intent(
                    this,
                    SignInActivity::class.java
                )
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}