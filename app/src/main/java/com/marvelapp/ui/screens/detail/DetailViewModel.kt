package com.marvelapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvelapp.data.Character
import com.marvelapp.data.CharacterRepository
import com.marvelapp.data.Comic
import com.marvelapp.data.Event
import com.marvelapp.data.Serie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(private val id: Int) : ViewModel() {

    private val repository: CharacterRepository = CharacterRepository()

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val character: Character? = null,
        val error: String? = null,
        val message: String? = null
    )

    init {
        fetchCharacterDetails()
    }

    private fun fetchCharacterDetails() {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            try {
                val character = repository.fetchCharacterById(id)
                _state.value = UiState(loading = false, character = character)
            } catch (e: Exception) {
                _state.value = UiState(loading = false, error = "Failed to fetch character details")
            }
        }
    }

    suspend fun fetchComicDetails(comicId: Int): Comic? {
        return repository.fetchComicDetails(comicId)
    }

    suspend fun fetchSerieDetails(serieId: Int): Serie? {
        return repository.fetchSerieDetails(serieId)
    }

    suspend fun fetchEventDetails(serieId: Int): Event? {
        return repository.fetchEventDetails(serieId)
    }

    fun onFavoriteClicked() {
        _state.update { it.copy(message = "Favorite clicked") }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null) }
    }
}
