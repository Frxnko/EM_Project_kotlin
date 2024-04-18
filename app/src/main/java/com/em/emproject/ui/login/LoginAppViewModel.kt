package com.em.emproject.ui.login

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.emproject.data.repository.UserRepository
import com.em.emproject.domain.model.UserClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginAppViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Loading)
    val state: StateFlow<LoginState> = _state

    private val _currentUser = MutableStateFlow<UserClass?>(null)
    val currentUser: StateFlow<UserClass?> = _currentUser

    companion object {
        var USER_CURRENT: UserClass? = null

        const val BUNDLE_NAME = "INFO"

        const val EMAIL_LOGIN = "EMAIL"
        const val NAME_USER = "NAME_USER"
        const val SURNAME_USER = "SURNAME_USER"
        const val NAME_USER_USER = "NAME_USER_USER"
        const val PROVIDER_LOGIN = "PROVIDER"
        const val TYPE_USER = "TYPE_USER"
        const val TIME_REGISTER = "TIME_REGISTER"
        const val IMAGE_USER = "IMAGE_USER"
    }


    fun createUserDB(
        email: String,
        password: String,
        name: String,
        surname: String
    ) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            withContext(Dispatchers.IO) {
                val result = userRepository.createUser(
                    email,
                    password,
                    name,
                    surname,
                    "user"
                )
                if (result != null) {
                    _state.value = LoginState.Success(result)
                } else {
                    _state.value = LoginState.Error("Error")
                }

            }


        }
    }

    fun loginUser(email: String, password: String, context: Context) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            _currentUser.value = userRepository.loginUser(email, password)

            val currentUserAux = _currentUser.value

            if (currentUserAux != null) {
                _state.value = LoginState.SuccessLogin(currentUserAux)
                USER_CURRENT = currentUserAux

            } else {
                _state.value = LoginState.Error("Error")
            }
        }
    }

    fun getUserCurrent(): UserClass? {
        val userCurrent = userRepository.getUserCurrent()
        _currentUser.value = userCurrent
        USER_CURRENT = userCurrent
        return userCurrent
    }

    fun infoCurrentUser(userCurrent: UserClass): Bundle {
        val bundle = Bundle()
        bundle.putString(EMAIL_LOGIN, userCurrent.email)
        bundle.putString(PROVIDER_LOGIN, userCurrent.provider)
        bundle.putString(NAME_USER, userCurrent.name)
        bundle.putString(SURNAME_USER, userCurrent.surname)
        bundle.putString(NAME_USER_USER, userCurrent.userName)
        bundle.putBoolean(TYPE_USER, userCurrent.isExpert)
        bundle.putString(TIME_REGISTER, userCurrent.timeRegister)
        bundle.putString(IMAGE_USER, userCurrent.image)

        return bundle
    }

}