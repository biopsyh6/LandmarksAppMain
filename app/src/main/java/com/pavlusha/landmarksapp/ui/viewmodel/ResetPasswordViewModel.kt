package com.pavlusha.landmarksapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.usecase.ResetPasswordUseCase
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.AuthEvent
import com.pavlusha.landmarksapp.ui.intent.ResetPasswordIntent
import com.pavlusha.landmarksapp.ui.state.ResetPasswordState
import com.pavlusha.landmarksapp.util.parseToResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(ResetPasswordState())
    val state: StateFlow<ResetPasswordState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<AuthEvent>(viewModelScope)
    val event = _event.flow

    fun onIntent(intent: ResetPasswordIntent) {
        when(intent) {
            is ResetPasswordIntent.EmailChanged -> {
                _state.update { it.copy(email = intent.value, error = null) }
            }
            is ResetPasswordIntent.ResetPasswordClicked -> handleResetPassword()
            is ResetPasswordIntent.BackToLoginClicked -> handleNavigateToLogin()
        }
    }

    private fun handleNavigateToLogin() {
        viewModelScope.launch {
            _event.emit(AuthEvent.NavigateToLogin)
        }
    }

    private fun handleResetPassword() {
        val email = state.value.email

        viewModelScope.launch(ioDispatcher) {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = resetPasswordUseCase(email)

            when(result) {
                is TResult.Success -> {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                    _event.emit(AuthEvent.ShowToast(R.string.success_reset_link_sent))
                }
                is TResult.Error -> {
                    val errorMessage = result.exception.parseToResource()
                    _state.update { it.copy(isLoading = false, error = errorMessage) }
                    _event.emit(AuthEvent.ShowToast(errorMessage))
                }
            }
        }
    }
}