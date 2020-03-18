package com.example.tinypomodoro

import java.sql.Time
import kotlin.math.max

class Timer(setTime_:Int) {
    // private var status:Boolean =false
    private var remainingTime:Int = setTime_
    private var setTime:Int = setTime_

    // public fun GetStatus(): Boolean {
    //     return status
    // }

    // public  fun SetStatus(status_:Boolean) {
    //     status = status_
    // }

    public fun GetSetTime():Int {
        return setTime
    }

    public fun SetSetTime(setTIme_:Int) {
        // TODO exception
        setTime = max(setTIme_,0)
    }

    public fun GetRemainingTime():Int {
        return remainingTime
    }

    public fun AdvanceOneSecond() :Boolean {
        if (remainingTime > 0) remainingTime--
        return remainingTime > 0
    }

    public fun Reset() {
        remainingTime = setTime
    }


}