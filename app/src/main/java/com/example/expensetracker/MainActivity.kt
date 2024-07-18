package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.ui.theme.Zinc

/**
 * MainActivity serves as the entry point for the Expense Tracker application.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content of the activity to ExpenseTrackerTheme
        setContent {
            ExpenseTrackerTheme {
                MainScreen()
            }
        }
    }
}

/**
 * MainScreen is the main composable function for rendering the entire application screen.
 */
@Composable
fun MainScreen() {
    // Create a NavHostController to manage navigation
    val navController = rememberNavController()

    // Get the current back stack entry to track navigation state
    val backStackEntry = navController.currentBackStackEntryAsState()

    // Scaffold that contains a BottomAppBar with a NavigationBar for navigation items
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Zinc
            ) {
                NavigationBar(
                    containerColor = Color.Transparent
                ) {
                    // Navigation bar items for different destinations
                    NavigationBarItem(
                        selected = backStackEntry.value?.destination?.route == "home",
                        onClick = { navController.navigate("home") },
                        icon = { Icon(painter = painterResource(id = R.drawable.home), contentDescription = "home", modifier = Modifier.size(34.dp) ) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                    NavigationBarItem(
                        selected = backStackEntry.value?.destination?.route == "add",
                        onClick = { navController.navigate("add") },
                        icon = { Icon(painter = painterResource(id = R.drawable.add), contentDescription = "add", modifier = Modifier.size(34.dp)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                    NavigationBarItem(
                        selected = backStackEntry.value?.destination?.route == "bookmark",
                        onClick = { navController.navigate("bookmark") },
                        icon = { Icon(painter = painterResource(id = R.drawable.bookmark), contentDescription = "graph", modifier = Modifier.size(34.dp)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                    NavigationBarItem(
                        selected = backStackEntry.value?.destination?.route == "setting",
                        onClick = { navController.navigate("setting") },
                        icon = { Icon(painter = painterResource(id = R.drawable.setting), contentDescription = "setting", modifier = Modifier.size(34.dp)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                }
            }
        },
        content = { innerPadding ->
            // NavHost for handling navigation destinations
            NavHost(navController = navController, startDestination = "home") {
                // Composable for displaying the HomeScreen
                composable("home") {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.Red
                    ) {
                        HomeScreen(navController)
                    }
                }
                // Composable for displaying the AddExpense screen
                composable("add") {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.White
                    ) {
                        AddExpense(navController)
                    }
                }
                // Composable for displaying the Setting screen
                composable("setting") {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.White
                    ) {
                        Setting(navController)
                    }
                }
                // Composable for displaying the Bookmark screen
                composable("bookmark") {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.White
                    ) {
                        BookMark(navController)
                    }
                }
                // Composable for editing an expense with dynamic expenseId argument
                composable(
                    route = "edit_expense/{expenseId}",
                    arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val expenseId = backStackEntry.arguments?.getInt("expenseId")
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        EditExpense(navController, expenseId)
                    }
                }
            }
        }
    )
}
