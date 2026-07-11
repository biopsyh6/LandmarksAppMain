package com.pavlusha.landmarksapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.usecase.SignUpUseCase
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.AuthEvent
import com.pavlusha.landmarksapp.ui.intent.RegisterIntent
import com.pavlusha.landmarksapp.ui.state.RegisterState
import com.pavlusha.landmarksapp.util.parseToResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<AuthEvent>(viewModelScope)
    val event = _event.flow

    fun onIntent(intent: RegisterIntent) {
        when(intent) {
            is RegisterIntent.EmailChanged -> _state.update { it.copy(email = intent.value, error = null) }
            is RegisterIntent.PasswordChanged -> _state.update { it.copy(password = intent.value, error = null) }
            is RegisterIntent.ConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = intent.value, error = null) }
            is RegisterIntent.SignUpClicked -> handleSignUp()
            is RegisterIntent.GoToLoginClicked -> handleNavigateToLogin()
        }
    }

    private fun handleSignUp() {
        val email = state.value.email
        val password = state.value.password
        val confirmPassword = state.value.confirmPassword

        if (password != confirmPassword) {
            _state.update { it.copy(error = R.string.error_passwords_dont_match) }
            return
        }

        viewModelScope.launch(ioDispatcher) {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = signUpUseCase(email, password)

            when(result) {
                is TResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(AuthEvent.NavigateToHome)
                }
                is TResult.Error -> {
                    val errorMessage = result.exception.parseToResource()
                    _state.update { it.copy(isLoading = false, error = errorMessage) }
                    _event.emit(AuthEvent.ShowToast(errorMessage))
                }
            }
        }
    }

    private fun handleNavigateToLogin() {
        viewModelScope.launch {
            _event.emit(AuthEvent.NavigateToLogin)
        }
    }
}