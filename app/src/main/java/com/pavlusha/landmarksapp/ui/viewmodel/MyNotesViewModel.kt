package com.pavlusha.landmarksapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.UserNoteDomainModel
import com.pavlusha.domain.usecase.DeleteNoteUseCase
import com.pavlusha.domain.usecase.GetAllNotesWithLandmarksUseCase
import com.pavlusha.domain.usecase.UpdateNoteUseCase
import com.pavlusha.landmarksapp.ui.SingleFlowEvent
import com.pavlusha.landmarksapp.ui.event.MyNotesEvent
import com.pavlusha.landmarksapp.ui.intent.MyNotesIntent
import com.pavlusha.landmarksapp.ui.state.MyNotesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyNotesViewModel(
    private val getAllNotesWithLandmarksUseCase: GetAllNotesWithLandmarksUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyNotesState())
    val state: StateFlow<MyNotesState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<MyNotesEvent>(viewModelScope)
    val event = _event.flow

    init {
        onIntent(MyNotesIntent.LoadNotes)
    }

    fun onIntent(intent: MyNotesIntent) {
        when (intent) {
            is MyNotesIntent.LoadNotes -> loadNotes()
            is MyNotesIntent.DeleteNote -> deleteNote(intent.noteId)
            is MyNotesIntent.UpdateNote -> updateNote(intent.note, intent.newText)
            is MyNotesIntent.OnLandmarkClicked -> navigateToDetails(intent.landmarkId, intent.source)
            is MyNotesIntent.OnBackClicked -> navigateBack()
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = getAllNotesWithLandmarksUseCase()

            when (result) {
                is TResult.Success -> {
                    _state.update { it.copy(isLoading = false, notes = result.data) }
                }
                is TResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(MyNotesEvent.ShowToast("Ошибка загрузки заметок"))
                }
            }
        }
    }

    private fun deleteNote(noteId: String) {
        viewModelScope.launch {
            val result = deleteNoteUseCase(noteId)
            if (result is TResult.Success) {
                loadNotes()
                _event.emit(MyNotesEvent.ShowToast("Заметка удалена"))
            } else {
                _event.emit(MyNotesEvent.ShowToast("Ошибка при удалении"))
            }
        }
    }

    private fun updateNote(note: UserNoteDomainModel, newText: String) {
        viewModelScope.launch {
            val result = updateNoteUseCase(note, newText)
            if (result is TResult.Success) {
                loadNotes()
                _event.emit(MyNotesEvent.ShowToast("Заметка обновлена"))
            } else {
                _event.emit(MyNotesEvent.ShowToast("Ошибка при обновлении"))
            }
        }
    }

    private fun navigateToDetails(landmarkId: String, source: LandmarkSource) {
        viewModelScope.launch {
            _event.emit(MyNotesEvent.NavigateToDetails(landmarkId, source))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _event.emit(MyNotesEvent.NavigateBack)
        }
    }
}