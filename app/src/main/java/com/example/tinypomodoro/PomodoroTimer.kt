package com.example.tinypomodoro

class PomodoroTimer(setTimes_: Array<Int>, timerNames_: Array<String>) {
    private var setTimes: Array<Int> = setTimes_
    private var timerNames: Array<String> = timerNames_
    private var timers: Array<Timer> = Array(setTimes.size) { i -> Timer(setTimes[i]) }
    private var activeTimerId: Int = 0
    private var status = false


    fun getSetTimes(): Array<Int> {
        return setTimes
    }

    fun setSetTimes(setTimes_: Array<Int>) {
        setTimes = setTimes_
        timers = Array(setTimes.size) { i -> Timer(setTimes[i]) }
    }

    fun getActiveTimerId(): Int {
        return activeTimerId
    }

    fun setActivetimerId(activeTimerId_: Int) {
        activeTimerId = activeTimerId_ % timers.size
    }

    fun getActiveTimerName(): String {
        return timerNames[activeTimerId]
    }

    fun getRemainingTime(): Int {
        return timers[activeTimerId].getRemainingTime()
    }

    fun getStatus(): Boolean {
        return status
    }

    fun setStatus(status_: Boolean) {
        status = status_
    }

    fun advanceOneSecond(): Boolean {
        val time_left = timers[activeTimerId].advanceOneSecond()
        if (!time_left) {
            timers[activeTimerId].reset()
            activeTimerId = (activeTimerId + 1) % timers.size
        }
        return time_left
    }

    fun reset() {
        for (timer in timers) {
            timer.reset()
        }
    }

}