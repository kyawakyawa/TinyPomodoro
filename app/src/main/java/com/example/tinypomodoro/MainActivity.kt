package com.example.tinypomodoro

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private val handler = Handler()
    private var config: Config = Config()
    private var pomodoroTimer: PomodoroTimer = PomodoroTimer(config.setTimes, config.timerNames)

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

        val vibrator = object : Runnable {
            var c:Int = 1
            override fun run() {
                vibration(c)
            }
        }

        val runnable = object : Runnable {

            override fun run() {
                pomodoroTimer.advanceOneSecond().let {
                    val timeLeft = it

                    if (!timeLeft) {
                        vibrator.c = pomodoroTimer.getActiveTimerId() + 1
                        handler.post(vibrator)
                        statusText.text = pomodoroTimer.getActiveTimerName()
                    }
                }

                pomodoroTimer.getRemainingTime().let {
                    timeToText(it).let { it_ ->
                        remainingTime.text = it_!!
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }

        // start
        startStopButton.setOnClickListener {

            if (pomodoroTimer.getStatus()) {
                handler.removeCallbacks(runnable)
                pomodoroTimer.setStatus(false)
                syncWithViewAndTimer()
            } else {
                handler.post(runnable)
                pomodoroTimer.setStatus(true)
                syncWithViewAndTimer()
            }
        }

        resetButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            pomodoroTimer.setStatus(false)
            pomodoroTimer.reset()
            syncWithViewAndTimer()
        }

        statusButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            pomodoroTimer.setStatus(false)
            pomodoroTimer.getActiveTimerId().let {
                pomodoroTimer.setActivetimerId(it + 1)
            }
            pomodoroTimer.reset()
            syncWithViewAndTimer()
        }

    }

    private fun init() {

        pomodoroTimer.setStatus(false)
        syncWithViewAndTimer()

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

        pomodoroTimer.getRemainingTime().let { it_ ->
            timeToText(it_).let {
                remainingTime.text = it!!
            }
        }

        statusText.text = pomodoroTimer.getActiveTimerName()

        startStopButton.text =
            if (pomodoroTimer.getStatus()) "Stop" else "Start"

        statusText.text = pomodoroTimer.getActiveTimerName()
    }

    private fun vibration(c:Int) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val f = LongArray(c * 2) { 500 }
            val onoff = IntArray(c * 2) { i -> if(i % 2 == 0) DEFAULT_AMPLITUDE else 0}

            val vibrationEffect: VibrationEffect =
                VibrationEffect.createWaveform(f ,onoff, -1)
            vibrator.vibrate(vibrationEffect)
        } else {
            val f = LongArray(c * 2) { i -> if(i > 0) 500 else 0 }
            vibrator.vibrate(f, -1)

        }
    }
}
