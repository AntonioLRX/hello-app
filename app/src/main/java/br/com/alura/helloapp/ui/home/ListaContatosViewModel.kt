package br.com.alura.helloapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.helloapp.database.ContactDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaContatosViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {
    private var searchJob: Job? = null
    private val _uiState = MutableStateFlow(ListaContatosUiState())
    val uiState: StateFlow<ListaContatosUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val contacts = contactDao.searchAll().onStart {
                _uiState.value = _uiState.value.copy(
                    loading = true
                )
            }
            contacts.collect {
                _uiState.value = _uiState.value.copy(
                    contacts = it, loading = false
                )
            }

        }
    }

    fun onChangeValue(value: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(
            searchValue = value
        )
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (value.isNotEmpty()) {
                contactDao.searchByName(value).collect { result ->
                    _uiState.value = _uiState.value.copy(
                        contacts = result
                    )
                }
            } else {
                contactDao.searchAll().collect { result ->
                    _uiState.value = _uiState.value.copy(
                        contacts = result
                    )
                }
            }
        }

    }
}