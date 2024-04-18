package com.em.emproject.di

import javax.inject.Inject

class ComparateClass @Inject constructor() {

    fun isNumber(s: String): Boolean {
        return try {
            s.toInt()
            true
        } catch (ex: NumberFormatException) {
            false
        }
    }
}