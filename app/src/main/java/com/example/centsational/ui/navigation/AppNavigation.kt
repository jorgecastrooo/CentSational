package com.example.centsational.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.centsational.ui.transactions.AddEditScreen
import com.example.centsational.ui.transactions.TransactionsScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "transactions") {
        composable("transactions") {
            TransactionsScreen(
                modifier = modifier,
                onAdd = { navController.navigate("addedit") },
                onEdit = { id -> navController.navigate("addedit?transactionId=$id") }
            )
        }
        composable(
            route = "addedit?transactionId={transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            AddEditScreen(modifier = modifier, onDone = { navController.popBackStack() })
        }
    }
}