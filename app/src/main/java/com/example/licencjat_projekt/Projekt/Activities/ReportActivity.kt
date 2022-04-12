package com.example.licencjat_projekt.Projekt.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        report_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}