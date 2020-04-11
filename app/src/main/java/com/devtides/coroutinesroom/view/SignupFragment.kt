package com.devtides.coroutinesroom.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.devtides.coroutinesroom.R
import com.devtides.coroutinesroom.viewmodel.SignupErrors
import com.devtides.coroutinesroom.viewmodel.SignupViewModel
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment : Fragment() {

    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signupBtn.setOnClickListener { onSignup(it) }
        gotoLoginBtn.setOnClickListener { onGotoLogin(it) }

        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.signupComplete.observe(viewLifecycleOwner, Observer { isComplete ->
            Toast.makeText(activity, getString(R.string.signup_completed), Toast.LENGTH_SHORT)
                .show()
            val action = SignupFragmentDirections.actionGoToMain()
            Navigation.findNavController(signupUsername).navigate(action)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            val errorMessage = when (error) {
                SignupErrors.USER_EXIST -> getString(R.string.user_exist_error)
            }
            Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    private fun onSignup(v: View) {
        val userName = signupUsername.text.toString()
        val password = signupPassword.text.toString()
        val info = otherInfo.text.toString()
        var toastMessage = when {
            textFieldsIsEmpty(
                listOf(
                    userName,
                    password,
                    info
                )
            ) -> getString(R.string.please_fill_all_fields)
            userName.isNullOrEmpty() -> getString(R.string.please_fill_username_field)
            password.isNullOrEmpty() -> getString(R.string.please_fill_password_field)
            info.isNullOrEmpty() -> getString(R.string.please_fill_info_field)
            else -> null
        }
        if (toastMessage != null) {
            Toast.makeText(activity, toastMessage, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.signup(userName, password, info)
        }
    }

    private fun onGotoLogin(v: View) {
        val action = SignupFragmentDirections.actionGoToLogin()
        Navigation.findNavController(v).navigate(action)
    }

    private fun textFieldsIsEmpty(textFields: List<String>): Boolean {
        var counter = 0
        textFields.forEach {
            if (!it.isNullOrEmpty()) return false
        }
        return true
    }
}
