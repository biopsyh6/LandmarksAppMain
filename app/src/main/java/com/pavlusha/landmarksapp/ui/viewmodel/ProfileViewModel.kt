package com.pavlusha.landmarksapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.usecase.GetCurrentUserUseCase
import com.pavlusha.domain.usecase.SignOutUseCase
import com.pavlusha.domain.usecase.UpdateUserProfileUseCase
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.ProfileEvent
import com.pavlusha.landmarksapp.ui.intent.ProfileIntent
import com.pavlusha.landmarksapp.ui.state.ProfileState
import com.pavlusha.landmarksapp.util.parseToResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<ProfileEvent>(viewModelScope)
    val event = _event.flow

    init {
        onIntent(ProfileIntent.LoadUser)
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadUser -> loadUser()
            is ProfileIntent.SignOut -> signOut()
            is ProfileIntent.OnMenuItemClicked -> navigateTo(intent.route)

            is ProfileIntent.OnEditClicked -> {
                val currentUser = _state.value.user
                _state.update {
                    it.copy(
                        isEditing = true,
                        editName = currentUser?.displayName ?: "",
                        editPhotoUrl = currentUser?.photoUrl
                    )
                }
            }
            is ProfileIntent.OnCancelEditClicked -> {
                _state.update { it.copy(isEditing = false) }
            }
            is ProfileIntent.OnNameChanged -> {
                _state.update { it.copy(editName = intent.name) }
            }
            is ProfileIntent.OnPhotoChanged -> {
                _state.update { it.copy(editPhotoUrl = intent.photoUri) }
            }
            is ProfileIntent.OnSaveProfileClicked -> saveProfile()
        }
    }

    private fun saveProfile() {
        val currentState = _state.value
        val currentUser = currentState.user ?: return

        _state.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            val updatedUser = currentUser.copy(
                displayName = currentState.editName.ifBlank { null },
                photoUrl = currentState.editPhotoUrl,
            )

            val result = updateUserProfileUseCase(updatedUser)

            when (result) {
                is TResult.Success -> {
                    _state.update {
                        it.copy(
                            user = updatedUser,
                            isEditing = false,
                            isSaving = false
                        )
                    }
                    _event.emit(ProfileEvent.ShowToast(R.string.profile_saved))
                }
                is TResult.Error -> {
                    _state.update { it.copy(isSaving = false) }
                    val errorRes = result.exception?.parseToResource() ?: R.string.error_unknown
                    _event.emit(ProfileEvent.ShowToast(errorRes))
                }
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val currentUser = getCurrentUserUseCase()
                _state.update { it.copy(user = currentUser, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = R.string.error_unknown, isLoading = false) }
                _event.emit(ProfileEvent.ShowToast(R.string.error_unknown))
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                signOutUseCase()
                _state.update { it.copy(isLoading = false, user = null) }
                _event.emit(ProfileEvent.NavigateToAuth)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = R.string.error_unknown) }
                _event.emit(ProfileEvent.ShowToast(R.string.error_unknown))
            }
        }
    }

    private fun navigateTo(route: String) {
        viewModelScope.launch {
            _event.emit(ProfileEvent.NavigateTo(route))
        }
    }
}