package com.calyrsoft.ucbp1.presentation

import android.R.attr.text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.annotations.Async
import org.koin.androidx.compose.koinViewModel

@Composable
fun GithubScreen(modifier: Modifier,
                 vm : GithubViewModel = koinViewModel()) {

    //podemos decir que en modules.kt se pone todo lo necesario para que alguien pueda inyectarse un GithubViewModel
    //y entramos a ese archivo con koinViewModel
    var nickname by remember { mutableStateOf("") }
    //by remember hace que este valor se mantenga si salieramos y volvieramos a entrar a la misma pantalla
//    mutableStateOf = estado local de la pantalla, directo en UI.
//    MutableStateFlow = estado global del ViewModel, para manejar lógica y notificar a la UI.

    val state by vm.state.collectAsState()
    //Tomo el StateFlow que está en el ViewModel (vm.state), lo convierto en
    // un estado observable de Compose (collectAsState()) y lo guardo en state
    //hice el collectAsState porque eso ayuda a este @Composable a que si el view model cambia su state
    //este igual se actualice y se redibuje en la pantalla con el when de mas abajo
    //por asi decirlo finalmente le estamos dando un uso al observable del viewModel

    Column {
        Text("")
        OutlinedTextField(
            value = nickname,
            onValueChange = {
                    it ->
                nickname = it
            },
        )
        OutlinedButton(onClick = {
            vm.fetchAlias(nickname)
            //ahora si estamos usando el fetch alias del view model que recordemos hace dos cosas cambia
            //el state del viewmodel a loading porque esta bien la pantalla tras apretar el boton deberia ponerse en loading
            //y recien luego llamamos al caso de uso, tras llamar al caso de uso el state puede volver a cambiar, no importa si cambia o no
            //este estara siendo monitoreado por _state y a su vez por el state de esta clase Screen
        }) {
            Text("")
        }
        when(val st = state){
            is GithubViewModel.GithubStateUI.Error -> {
                Text(st.message)
            }
            GithubViewModel.GithubStateUI.Init -> {
                Text("Init")
            }
            GithubViewModel.GithubStateUI.Loading -> {
                Text("Loading")
            }
            is GithubViewModel.GithubStateUI.Success -> {
                Text(st.github.nickname)
                AsyncImage(
                    model = st.github.pathUrl,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
            }

        }
    }
}
