package com.example.tinypomodoro

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {

    val handler = Handler()
    var config:Config = Config()
    var pomodoroTimer:PomodoroTimer? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        val remainingTime = findViewById<TextView>(R.id.remaining_time)
        val statusText = findViewById<TextView>(R.id.status)
        val startStopButton = findViewById<Button>(R.id.start_stop)
        val resetButton = findViewById<Button>(R.id.reset)
        val statusButton = findViewById<Button>(R.id.switch_status)

        val runnable = object : Runnable {
            fun init(){
                pomodoroTimer = PomodoroTimer(config.setTimes, config.timerNames)
                pomodoroTimer?.SetStatus(true)
                syncWithViewAndTimer()
            }
            override fun run() {
                if (pomodoroTimer == null) {
                    this.init()
                }
                pomodoroTimer?.AdvanceOneSecond().let {
                    val timeLeft = it

                    if (timeLeft == false) {
                        statusText.text = pomodoroTimer?.GetActiveTimerName()
                    }
                }

                pomodoroTimer?.GetRemainingTime()?.let { it_ ->
                    timeToText(it_).let{
                        remainingTime.text = it!!
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }

        // start
        startStopButton.setOnClickListener {
            if (pomodoroTimer == null) {
                startStopButton.text = "Stop"
                handler.post(runnable)
            } else {
                if (pomodoroTimer?.GetStatus()!!) {
                    handler.removeCallbacks(runnable)
                    // startStopButton.text = "Start"
                    pomodoroTimer?.SetStatus(false)
                    syncWithViewAndTimer()
                } else {
                    handler.post(runnable)
                    // startStopButton.text = "Stop"
                    pomodoroTimer?.SetStatus(true)
                    syncWithViewAndTimer()
                }
            }
        }

        resetButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            pomodoroTimer = null
            timeToText(-1)?.let {
                remainingTime.text = it
            }
            startStopButton.text = "Start"
        }

        statusButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            pomodoroTimer?.SetStatus(false)
            pomodoroTimer?.GetActiveTimerId()?.let {
                pomodoroTimer?.SetActivetimerId(it + 1)
            }
            pomodoroTimer?.Reset()
            syncWithViewAndTimer()
        }

    }

    private fun init() {

        val remainingTime = findViewById<TextView>(R.id.remaining_time)

        timeToText(-1)?.let {
            remainingTime.text = it
        }
    }

    private fun timeToText(time: Int = 0): String? {
        return when {
            time < 0 -> {
                "--:--"
            }
            time == 0 -> {
                "00:00"
            }
            else -> {
                val m = time / 60
                val s = time % 60
                "%1$02d:%2$02d".format(m, s)
            }
        }
    }


    private fun syncWithViewAndTimer() {
        val remainingTime = findViewById<TextView>(R.id.remaining_time)
        val statusText = findViewById<TextView>(R.id.status)
        val startStopButton = findViewById<Button>(R.id.start_stop)

        pomodoroTimer?.GetRemainingTime()?.let { it_ ->
            timeToText(it_).let{
                remainingTime.text = it!!
            }
        }

        statusText.text = pomodoroTimer?.GetActiveTimerName()

        startStopButton.text =
            if(pomodoroTimer == null) "Start"
            else { if (pomodoroTimer?.GetStatus()!!) "Stop" else "Start"}

        statusText.text = pomodoroTimer?.GetActiveTimerName()
    }
}
