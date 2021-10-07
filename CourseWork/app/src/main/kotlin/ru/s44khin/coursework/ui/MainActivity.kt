package ru.s44khin.coursework.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.s44khin.coursework.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}