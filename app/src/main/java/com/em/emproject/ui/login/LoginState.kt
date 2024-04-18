package com.em.emproject.ui.login

import com.em.emproject.domain.model.UserClass

sealed class LoginState {

    data object Loading : LoginState()
    data class Error(val error: String) : LoginState()
    data class Success(val registered: String) : LoginState()
    data class SuccessLogin(val userClass: UserClass) : LoginState()
}