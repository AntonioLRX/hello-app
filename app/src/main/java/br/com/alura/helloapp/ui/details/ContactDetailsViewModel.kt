package br.com.alura.helloapp.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.helloapp.database.ContactDao
import br.com.alura.helloapp.util.ID_CONTATO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val contactDao: ContactDao
) : ViewModel() {

    private val contactId = savedStateHandle.get<Long>(ID_CONTATO)

    private val _uiState = MutableStateFlow(DetalhesContatoUiState())
    val uiState: StateFlow<DetalhesContatoUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadContact()
        }
    }

    private suspend fun loadContact() {
        contactId?.let {
            val contact = contactDao.searchById(contactId)
            contact.collect {
                it?.let {
                    with(it) {
                        _uiState.value = _uiState.value.copy(
                            id = id,
                            nome = name,
                            sobrenome = lastName,
                            aniversario = birthday,
                            fotoPerfil = photo,
                            telefone = phone,
                        )
                    }
                }
            }
        }
    }

    suspend fun removeContact() {
        contactId?.let { contactDao.remove(it) }
    }
}
