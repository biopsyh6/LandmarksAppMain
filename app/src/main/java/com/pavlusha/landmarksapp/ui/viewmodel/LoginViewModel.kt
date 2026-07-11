package com.pavlusha.landmarksapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.usecase.SignInUseCase
import com.pavlusha.domain.usecase.SignInWithGoogleUseCase
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.AuthEvent
import com.pavlusha.landmarksapp.ui.intent.LoginIntent
import com.pavlusha.landmarksapp.ui.state.LoginState
import com.pavlusha.landmarksapp.util.parseToResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<AuthEvent>(viewModelScope)
    val event = _event.flow

    fun onIntent(intent: LoginIntent) {
        when(intent) {
            is LoginIntent.EmailChanged -> _state.update { it.copy(email = intent.value, error = null) }
            is LoginIntent.PasswordChanged -> _state.update { it.copy(password = intent.value, error = null) }
            is LoginIntent.SignInClicked -> handleSignIn()
            is LoginIntent.GoToRegisterClicked -> handleNavigateToRegister()
            is LoginIntent.ForgotPasswordClicked -> handleNavigateToResetPassword()
        }
    }

    private fun handleSignIn() {
        val email = state.value.email
        val password = state.value.password

        viewModelScope.launch(ioDispatcher) {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = signInUseCase(email, password)

            when(result) {
                is TResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(AuthEvent.NavigateToHome)
                }
                is TResult.Error -> {
                    val errorMessage = result.exception.parseToResource()
                    _state.update { it.copy(isLoading = false, error = errorMessage) }
                    _event.emit(AuthEvent.ShowToast(
                        message = errorMessage
                    ))
                }
            }
        }
    }

    private fun handleNavigateToRegister() {
        viewModelScope.launch {
            _event.emit(AuthEvent.NavigateToRegister)
        }
    }

    private fun handleNavigateToResetPassword() {
        viewModelScope.launch {
            _event.emit(AuthEvent.NavigateToResetPassword)
        }
    }
}