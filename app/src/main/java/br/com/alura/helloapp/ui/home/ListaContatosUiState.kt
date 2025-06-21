package br.com.alura.helloapp.ui.home

import br.com.alura.helloapp.data.Contact

data class ListaContatosUiState(
    val contacts: List<Contact> = emptyList(),
    val loading: Boolean = false,
    val logado: Boolean = true,
    val searchValue: String = "",
)