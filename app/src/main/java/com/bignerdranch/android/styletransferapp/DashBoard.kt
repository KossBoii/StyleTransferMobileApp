package com.bignerdranch.android.styletransferapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashBoard : AppCompatActivity() {

    private lateinit var botNavView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        // Init
        botNavView = findViewById(R.id.bot_nav)
        botNavView.selectedItemId = R.id.dashboard

        botNavView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.dashboard -> {
                    true
                }
                R.id.home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.about -> {
                    startActivity(Intent(applicationContext, About::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
            }
            true
        }
    }
}