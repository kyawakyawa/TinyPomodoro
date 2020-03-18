package com.example.tinypomodoro

class PomodoroTimer(setTimes_:Array<Int>,timerNames_:Array<String>) {
    private var setTimes:Array<Int> = setTimes_
    private var timerNames:Array<String> = timerNames_
    private var timers:Array<Timer> = Array(setTimes.size) { i -> Timer(setTimes[i])}
    private var activeTimerId:Int = 0
    private var status = false


    public fun GetSetTimes(): Array<Int> {
        return setTimes
    }

    public fun SetSetTimes(setTimes_: Array<Int>) {
        setTimes = setTimes_
        timers = Array(setTimes.size) { i -> Timer(setTimes[i])}
    }

    public fun GetActiveTimerId() :Int {
        return activeTimerId
    }

    public fun SetActivetimerId(activeTimerId_:Int) {
        activeTimerId = activeTimerId_ % timers.size
    }

    public fun GetActiveTimerName() :String {
        return timerNames[activeTimerId]
    }

    public fun GetRemainingTime() :Int {
        return timers[activeTimerId].GetRemainingTime()
    }

    public fun GetStatus(): Boolean {
        return status
    }

    public  fun SetStatus(status_:Boolean) {
        status = status_
    }

    public fun AdvanceOneSecond() :Boolean {
        val time_left = timers[activeTimerId].AdvanceOneSecond()
        if (!time_left) {
            timers[activeTimerId].Reset()
            activeTimerId = (activeTimerId + 1) % timers.size
        }
        return time_left
    }

    public fun Reset() {
        for (timer in timers) {
            timer.Reset()
        }
    }

}