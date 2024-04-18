package com.em.emproject.di

import java.util.Calendar
import javax.inject.Inject

class RegisterTime @Inject constructor() {

    fun getTimeRegister(): String {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)
        val min = calendar.get(Calendar.MINUTE)
        val seg = calendar.get(Calendar.SECOND)

        return "$year-$month-$day $hour:$min:$seg"
    }
}