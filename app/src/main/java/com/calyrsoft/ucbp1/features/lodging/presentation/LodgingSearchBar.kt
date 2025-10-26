import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun LodgingSearchBar(vm: LodgingListViewModel) {
    var query by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = getViewModel()
    val userId by authViewModel.userId.collectAsState()
    val userRole by authViewModel.userRole.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Buscar alojamiento...") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = Color(0xFFB00020)
            )
        )

        Button(onClick = {
            val q = query.trim()
            if(userRole=="ADMIN" && userId!=null) {
                // Búsqueda para ADMIN por ID de usuario
                if (q.isEmpty()) {
                    vm.load(userId!!)
                } else {
                    vm.searchByNameAndAdminId(q,userId!!)
                }
            }else if (q.isEmpty()) vm.loadAll() else vm.searchByName(q)
        }, colors =ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))) {
            Icon(Icons.Default.Search, contentDescription = "Buscar")
        }
    }
}
