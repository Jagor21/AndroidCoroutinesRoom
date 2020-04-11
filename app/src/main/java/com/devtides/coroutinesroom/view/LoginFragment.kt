package com.devtides.coroutinesroom.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.devtides.coroutinesroom.R
import com.devtides.coroutinesroom.viewmodel.LoginErrors
import com.devtides.coroutinesroom.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBtn.setOnClickListener { onLogin() }
        gotoSignupBtn.setOnClickListener { onGotoSignup(it) }

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginComplete.observe(viewLifecycleOwner, Observer { isComplete ->
            if (isComplete) {
                val action = LoginFragmentDirections.actionGoToMain()
                Navigation.findNavController(loginPassword).navigate(action)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            val errorMessage = when (error) {
                LoginErrors.WRONG_PASSWORD -> getString(R.string.wrong_password_error)
                LoginErrors.EMPTY_USERNAME -> getString(R.string.empty_password_error)
                LoginErrors.EMPTY_PASSWORD -> getString(R.string.empty_username_error)
                LoginErrors.USER_NOT_FOUND -> getString(R.string.user_not_found_error)
                else -> ""
            }
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onLogin() {
        val username = loginUsername.text.toString()
        val password = loginPassword.text.toString()
        viewModel.login(username, password)
    }

    private fun onGotoSignup(v: View){
        val action = LoginFragmentDirections.actionGoToSignup()
        Navigation.findNavController(v).navigate(action)
    }
}
