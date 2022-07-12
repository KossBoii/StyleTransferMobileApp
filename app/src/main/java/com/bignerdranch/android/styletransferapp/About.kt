package com.bignerdranch.android.styletransferapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class About : AppCompatActivity() {

    private lateinit var botNavView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Init
        botNavView = findViewById(R.id.bot_nav)
        botNavView.selectedItemId = R.id.about

        botNavView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.dashboard -> {
                    startActivity(Intent(applicationContext, DashBoard::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.about -> {
                    true
                }
            }
            true
        }
    }
}