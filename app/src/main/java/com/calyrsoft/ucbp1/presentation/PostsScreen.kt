package com.calyrsoft.ucbp1.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun PostsScreen(
    modifier: Modifier = Modifier,
    vm: PostsViewModel = koinViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.load() }

    Box(modifier = modifier.fillMaxSize()) {
        when (val s = state) {
            is PostsViewModel.UiState.Init ->
                Text("Init", modifier = Modifier.align(Alignment.Center))

            is PostsViewModel.UiState.Loading ->
                Text("Cargando...", modifier = Modifier.align(Alignment.Center))

            is PostsViewModel.UiState.Error ->
                Text("Error: ${s.message}", modifier = Modifier.align(Alignment.Center))

            is PostsViewModel.UiState.Success -> {
                val data = s.data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(data.posts) { post ->
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = post.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = post.body,
                                    fontSize = 14.sp
                                )
                                Spacer(Modifier.height(8.dp))

                                androidx.compose.material3.Button(
                                    onClick = { vm.loadComments(post.id) }
                                ) {
                                    Text(
                                        if (data.expandedPostId == post.id)
                                            "Ocultar comentarios"
                                        else
                                            "Ver comentarios"
                                    )
                                }

                                if (data.expandedPostId == post.id) {
                                    val comments = data.commentsByPost[post.id].orEmpty()
                                    Spacer(Modifier.height(8.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        comments.forEach { comment ->
                                            Card(
                                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(Modifier.padding(8.dp)) {
                                                    Text(
                                                        text = comment.email,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp
                                                    )
                                                    Spacer(Modifier.height(4.dp))
                                                    Text(
                                                        text = comment.body,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
