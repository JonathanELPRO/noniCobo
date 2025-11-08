package com.calyrsoft.ucbp1.features.lodging.presentation

import LodgingSearchBar
import android.net.Uri
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomCategory
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LodgingListScreen(
    vm: LodgingListViewModel = koinViewModel(),
    onDetails: (Long) -> Unit = {},
    onEdit: (Lodging) -> Unit,
    onBack: () -> Unit = {},
) {
    val authViewModel: AuthViewModel = getViewModel()
    val userId by authViewModel.userId.collectAsState()
    val userRole by authViewModel.userRole.collectAsState()

    val state by vm.state.collectAsState()
    val adUrl by vm.adUrl.collectAsState()
    val showAd by vm.showAd.collectAsState()

    val filteredList by vm.filteredList.collectAsState()
    val isFilterActive by vm.isFilterActive.collectAsState()

    var showFilterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.startAdListener()
        vm.resetAdVisibility()
    }

    LaunchedEffect(userId) {
        if (userId != null) {
            if (userRole == "ADMIN") vm.load(userId!!) else vm.loadAll()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF4F4F4),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Color(0xFFB00020),
                        shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                    )
            ) {
                Text(
                    "ALOJAMIENTOS",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Banner de anuncio
            AnimatedVisibility(
                visible = showAd && !adUrl.isNullOrBlank(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                AdBanner(imageUrl = adUrl!!, onClose = { vm.dismissAd() })
            }

            // Barra de búsqueda y filtro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    LodgingSearchBar(vm = vm)
                }
                Spacer(Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = { showFilterDialog = true },
                    modifier = Modifier.size(48.dp),
                    containerColor = if (isFilterActive) Color(0xFFB00020) else Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar",
                        tint = if (isFilterActive) Color.White else Color(0xFFB00020)
                    )
                }
            }

            // Diálogo de filtros
            if (showFilterDialog) {
                FilterDialog(
                    initial = vm.filters.collectAsState().value,
                    availableAddresses = state
                        .let { if (it is LodgingListViewModel.LodgingListStateUI.Success) it.lodgings.mapNotNull { l -> l.address }.toSet() else emptySet() },
                    onDismiss = { showFilterDialog = false },
                    onApply = { filter ->
                        vm.updateFilters(filter)
                        showFilterDialog = false
                    }
                )
            }

            Spacer(Modifier.height(4.dp))

            // Contenido principal
            when (state) {
                is LodgingListViewModel.LodgingListStateUI.Init,
                is LodgingListViewModel.LodgingListStateUI.Loading -> LoadingStateLoader()

                is LodgingListViewModel.LodgingListStateUI.Success -> {
                    if (filteredList.isEmpty()) {
                        EmptyState(isFilterActive) { vm.clearFilters() }
                    } else {
                        LodgingListContent(
                            lodgings = filteredList,
                            isFilterActive = isFilterActive,
                            onClearFilters = { vm.clearFilters() },
                            onDetails = onDetails,
                            onEdit= onEdit
                        )
                    }
                }

                is LodgingListViewModel.LodgingListStateUI.Error -> {
                    val msg = (state as LodgingListViewModel.LodgingListStateUI.Error).message
                    ErrorStateCard(msg) { userId?.let { vm.load(it) } }
                }
            }
        }
    }
}



