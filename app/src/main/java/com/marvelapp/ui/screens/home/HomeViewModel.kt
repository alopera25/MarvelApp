package com.marvelapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvelapp.data.Character
import com.marvelapp.data.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var offset = 0
    private val limit = 18
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> get() = _state.asStateFlow()

    private val repository = CharacterRepository()

    fun onUiReady() {
        if (_state.value.characters.isEmpty()) {
            loadCharacters()
        }
    }

    fun loadMoreCharacters() {
        offset += limit
        loadCharacters()
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val fetchedCharacters = repository.fetchCharacter(offset, limit)
            if (fetchedCharacters != null) {
                val updatedCharacters = if (offset == 0) {
                    fetchedCharacters
                } else {
                    _state.value.characters + fetchedCharacters
                }
                _state.value = _state.value.copy(loading = false, characters = updatedCharacters)
            } else {
                _state.value = _state.value.copy(
                    loading = false,
                    error = "No se pudo cargar más personajes. Verifica tu conexión a internet."
                )
            }
        }
    }

    fun initState(characters: List<Character>) {
        _state.value = UiState(characters = characters)
    }

    data class UiState(
        val loading: Boolean = false,
        val characters: List<Character> = emptyList(),
        val error: String? = null
    )

}
