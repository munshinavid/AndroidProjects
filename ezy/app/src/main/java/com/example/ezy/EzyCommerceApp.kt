package com.example.ezy

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ezy.navigation.Navigation
import com.example.ezy.viewmodel.AuthViewModel

@Composable
fun EzyCommerceApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    Navigation(
        navController = navController,
        authViewModel = authViewModel,
        modifier = modifier
    )
}