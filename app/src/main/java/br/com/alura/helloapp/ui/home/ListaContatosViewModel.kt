package br.com.alura.helloapp.ui.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.helloapp.database.ContactDao
import br.com.alura.helloapp.preferences.PreferencesKey.USER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaContatosViewModel @Inject constructor(
    private val contactDao: ContactDao,
    private val dataStore: DataStore<Preferences>,
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
        viewModelScope.launch {
            loadUserName()
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

    fun showDialog(show: Boolean) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(
            showDialog = show
        )
    }

    fun logout() = viewModelScope.launch {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("logged")] = false
        }
    }

    private suspend fun loadUserName() {
        val userName = dataStore.data.first()[USER]
        userName?.let {
            _uiState.value = _uiState.value.copy(
                userName = userName
            )
        }
    }
}