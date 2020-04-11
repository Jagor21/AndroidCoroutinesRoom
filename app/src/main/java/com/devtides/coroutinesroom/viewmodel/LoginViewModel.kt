package com.devtides.coroutinesroom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.devtides.coroutinesroom.model.LoginState
import com.devtides.coroutinesroom.model.User
import com.devtides.coroutinesroom.model.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val loginComplete = MutableLiveData<Boolean>()
    val error = MutableLiveData<LoginErrors>()

    private val db = UserDatabase(application).userDao()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun login(username: String, password: String) {
        error.value = when {
            username.isNullOrEmpty() -> LoginErrors.EMPTY_USERNAME
            password.isNullOrEmpty() -> LoginErrors.EMPTY_PASSWORD
            else -> null
        }
        coroutineScope.launch {
            val user: User? = db.getUser(username)
            withContext(Dispatchers.Main) {
                if (user == null) {
                    error.value = LoginErrors.USER_NOT_FOUND
                } else {
                    if (user?.passwordHash == password.hashCode()) {
                        LoginState.login(user)
                        loginComplete.value = true
                    } else {
                        error.value = LoginErrors.WRONG_PASSWORD
                    }
                }
            }
        }
    }
}

enum class LoginErrors(id: Int) {
    WRONG_PASSWORD(0),
    EMPTY_PASSWORD(1),
    EMPTY_USERNAME(2),
    USER_NOT_FOUND(3)
}