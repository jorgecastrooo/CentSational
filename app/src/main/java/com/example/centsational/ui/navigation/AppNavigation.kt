package com.example.centsational.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.centsational.ui.transactions.AddEditScreen
import com.example.centsational.ui.transactions.TransactionsScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "transactions") {
        composable("transactions") {
            TransactionsScreen(modifier = modifier, onAdd = { navController.navigate("addedit") })
        }
        composable("addedit") {
            AddEditScreen(modifier = modifier, onDone = { navController.popBackStack() })
        }
    }
}