package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomCategory
import org.koin.androidx.compose.getViewModel

@Composable
fun LodgingCard(lodging: Lodging, onDetails: (Long) -> Unit,  onEdit: (Lodging) -> Unit) {
    val simplePrice = lodging.roomOptions.find { it.category == RoomCategory.SIMPLE }?.price
        ?: lodging.roomOptions.firstOrNull()?.price ?: 0.0
    val authViewModel: AuthViewModel = getViewModel()
    val userRole by authViewModel.userRole.collectAsState()
    Card(
        modifier = Modifier.fillMaxWidth().clickable { lodging.id?.let(onDetails) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(16.dp))) {
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(lodging.placeImageUri ?: "")
                        .crossfade(true)
                        .build()
                )
                Image(painter = painter, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

                Surface(
                    modifier = Modifier.align(Alignment.TopStart).padding(6.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFB00020)
                ) {
                    Text(lodging.type.name, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                if (lodging.open24h) {
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp),
                        shape = CircleShape,
                        color = Color(0xFF4CAF50)
                    ) {
                        Text("24h", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(lodging.name, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF212121), maxLines = 2, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Color(0xFFB00020), modifier = Modifier.size(18.dp))
                    Text("Bs $simplePrice/hora", color = Color(0xFFB00020), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                lodging.district?.let {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Text(it, color = Color.Gray, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            Spacer(Modifier.width(8.dp))


            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(onClick = { lodging.id?.let(onDetails) }, shape = RoundedCornerShape(14.dp), color = Color(0xFFB00020)) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Ver", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                if(userRole=="ADMIN") {
                    Surface(
                        onClick = { onEdit(lodging) },
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF1976D2)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Editar",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
