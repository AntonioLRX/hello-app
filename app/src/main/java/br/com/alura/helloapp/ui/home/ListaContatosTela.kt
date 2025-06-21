package br.com.alura.helloapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alura.helloapp.R
import br.com.alura.helloapp.data.Contact
import br.com.alura.helloapp.sampleData.contatosExemplos
import br.com.alura.helloapp.ui.components.AsyncImagePerfil
import br.com.alura.helloapp.ui.theme.HelloAppTheme

@Composable
fun ListaContatosTela(
    state: ListaContatosUiState,
    modifier: Modifier = Modifier,
    onClickDesloga: () -> Unit = {},
    onClickAbreDetalhes: (Long) -> Unit = {},
    onClickAbreCadastro: () -> Unit = {},
    onChangeValue: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppBarListaContatos(
                value = state.searchValue,
                onChangeValue = onChangeValue,
                onClickDesloga = onClickDesloga
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                onClick = { onClickAbreCadastro() },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.adicionar_novo_contato)
                )
            }
        }) { paddingValues ->
        if (state.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Carregando contatos...")
                }
            }
            return@Scaffold
        }

        if (state.contacts.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nenhum contato encontrado",
                        style = MaterialTheme.typography.h5,
                        color = Color.Gray
                    )
                    Text(
                        text = "Tente outro nome 😉",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }
                return@Scaffold
            }
        }

        LazyColumn(modifier.padding(paddingValues)) {
            items(state.contacts) { contato ->
                ContatoItem(contato) { idContato ->
                    onClickAbreDetalhes(idContato)
                }
            }
        }
    }
}

@Composable
fun AppBarListaContatos(
    value: String,
    onChangeValue: (String) -> Unit,
    onClickDesloga: () -> Unit
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                modifier = Modifier,
                textStyle = TextStyle(color = Color.White),
                placeholder = {
                    Text(text = "Procurar contato", color = Color.White)
                },
                value = value,
                onValueChange = onChangeValue
            )
        },
        actions = {
            IconButton(
                onClick = onClickDesloga
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.deslogar)
                )
            }
        }
    )
}

@Composable
fun ContatoItem(
    contact: Contact,
    onClick: (Long) -> Unit
) {
    Card(
        Modifier.clickable { onClick(contact.id) },
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            Modifier.padding(16.dp),
        ) {
            AsyncImagePerfil(
                urlImagem = contact.photo,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Column(
                Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = contact.name,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = contact.lastName
                )
            }
        }
    }
}

@Preview
@Composable
fun ListaContatosPreview() {
    HelloAppTheme {
        val value = remember { mutableStateOf("") }
        ListaContatosTela(
            state = ListaContatosUiState(
                contatosExemplos,
                searchValue = value.value,
            ),
            onChangeValue = {
                value.value = it
            }
        )
    }
}

@Preview
@Composable
fun ContatoItemPreview() {
    HelloAppTheme {
        ContatoItem(contatosExemplos.first()) {}
    }
}
