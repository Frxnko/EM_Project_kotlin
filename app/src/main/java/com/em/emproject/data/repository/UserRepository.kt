package com.em.emproject.data.repository

import com.em.emproject.domain.model.UserClass
import com.em.emventas.data.network.FireDataBaseService
import javax.inject.Inject

class UserRepository @Inject constructor(private val fireDataBaseService: FireDataBaseService) {


    suspend fun createUser(
        email: String, password: String, name: String, surname: String, typeUser: String
    ): String? {
        val user = fireDataBaseService.createUser(email, password, name, surname, "user")
        return if (user != null) email
        else null
    }

    suspend fun loginUser(email: String, password: String): UserClass? {
        val result = fireDataBaseService.loginUser(email, password)
        return result.getOrNull()
    }

    fun getUserCurrent(): UserClass? = fireDataBaseService.getCurrentUser()

    fun logoutUser() = fireDataBaseService.logoutUser()

}