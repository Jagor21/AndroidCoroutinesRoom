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

class SignupViewModel(application: Application) : AndroidViewModel(application) {

    val signupComplete = MutableLiveData<Boolean>()
    val error = MutableLiveData<SignupErrors>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy { UserDatabase(getApplication()).userDao() }

    fun signup(username: String, password: String, info: String) {
        coroutineScope.launch {
            var user = db.getUser(username)
            if (user != null) {
                withContext(Dispatchers.Main) {
                    error.value = SignupErrors.USER_EXIST
                }
            } else {
                user = User(userName = username, passwordHash = password.hashCode(), info = info)
                val userId = db.insertUser(user)
                LoginState.login(user)
                withContext(Dispatchers.Main) {
                    signupComplete.value = true
                }
            }
        }
    }
}

enum class SignupErrors(id: Int) {
    USER_EXIST(0)
}