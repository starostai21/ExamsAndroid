package com.example.cinemafilm.films

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cinemafilm.ProfileScreenActiviti
import com.example.cinemafilm.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun mainWindow(view: android.view.View) {
        startActivityForResult(
            Intent(this, MainActivity::class.java),
            1 // идентификатор окна
        )

    }

    fun profileWindow(view: android.view.View) {
        startActivityForResult(
            Intent(this, ProfileScreenActiviti::class.java),
            1 // идентификатор окна
        )
    }

}
