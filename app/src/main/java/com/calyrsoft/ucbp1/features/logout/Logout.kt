package com.calyrsoft.ucbp1.features.logout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.auth.presentation.LoginViewModel2

@Composable
fun Logout(
    authViewModel: AuthViewModel,
    onLogoutComplete: () -> Unit
){
    LaunchedEffect(Unit) {
        authViewModel.logout()
        onLogoutComplete()
    }
}
