package com.example.timerly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var resetButton: Button
    private var timerJob: Job? = null
    private var elapsedTime: Long = 0L
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startButton)
        timerTextView = findViewById(R.id.timerTextView)
        resetButton = findViewById(R.id.resetButton)

        startButton.setOnClickListener {
            if (isTimerRunning) {
                stopTimer()
            }   else {
                startTimer()
            }
        }

        resetButton.setOnClickListener {
            if (!isTimerRunning) {
                timerTextView.text = "0:00"
                elapsedTime = 0L
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        isTimerRunning = false
        startButton.text = "Start"
    }
    private fun startTimer() {
        timerJob?.cancel()
        isTimerRunning = true
        startButton.text = "Stop"
        timerJob = GlobalScope.launch {
            while (true) {
                delay(1000) // Update every second
                elapsedTime++
                updateTimerText()
            }
        }
    }

    private suspend fun updateTimerText() {
        withContext(Dispatchers.Main) {
            val minutes = elapsedTime / 60
            val seconds = elapsedTime % 60
            timerTextView.text = String.format("%d:%02d", minutes, seconds)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJob?.cancel()
    }
}