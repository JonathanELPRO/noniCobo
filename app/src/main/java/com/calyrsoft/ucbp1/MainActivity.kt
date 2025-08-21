package com.calyrsoft.ucbp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.calyrsoft.ucbp1.presentation.GithubScreen
import com.calyrsoft.ucbp1.presentation.SigninPage
import com.calyrsoft.ucbp1.ui.theme.Ucbp1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ucbp1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SigninPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
