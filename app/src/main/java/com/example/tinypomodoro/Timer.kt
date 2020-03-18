package com.example.tinypomodoro

import kotlin.math.max

class Timer(setTime_: Int) {
    // private var status:Boolean =false
    private var remainingTime: Int = setTime_
    private var setTime: Int = setTime_

    // public fun getStatus(): Boolean {
    //     return status
    // }

    // public  fun setStatus(status_:Boolean) {
    //     status = status_
    // }

    fun getSetTime(): Int {
        return setTime
    }

    fun setSetTime(setTIme_: Int) {
        // TODO exception
        setTime = max(setTIme_, 0)
    }

    fun getRemainingTime(): Int {
        return remainingTime
    }

    fun advanceOneSecond(): Boolean {
        if (remainingTime > 0) remainingTime--
        return remainingTime > 0
    }

    fun reset() {
        remainingTime = setTime
    }


}