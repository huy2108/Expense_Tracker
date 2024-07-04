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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Zinc
            ){
                NavigationBar(
                    containerColor = Color.Transparent
                ) {
                    NavigationBarItem(
                        selected = backStackEntry.value?.destination?.route == "home",
                        onClick = { navController.navigate("home") },
                        icon = { Icon(painter = painterResource(id = R.drawable.home), contentDescription = "home", modifier = Modifier.size(34.dp) ) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black, // Icon color when selected
                            selectedTextColor = Color.Black, // Text color when selected
                            unselectedIconColor = Color.White, // Icon color when not selected
                            unselectedTextColor = Color.White // Text color when not selected
                        )
                    )
                    NavigationBarItem(
                        selected = backStackEntry.value?.destination?.route == "add",
                        onClick = { navController.navigate("add") },
                        icon = { Icon(painter = painterResource(id = R.drawable.add), contentDescription = "add", modifier = Modifier.size(34.dp)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black, // Icon color when selected
                            selectedTextColor = Color.Black, // Text color when selected
                            unselectedIconColor = Color.White, // Icon color when not selected
                            unselectedTextColor = Color.White // Text color when not selected
                        )
                    )
                    NavigationBarItem(
                        selected = backStackEntry.value?.destination?.route == "bookmark",
                        onClick = { navController.navigate("bookmark") },
                        icon = { Icon(painter = painterResource(id = R.drawable.bookmark), contentDescription = "graph", modifier = Modifier.size(34.dp)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black, // Icon color when selected
                            selectedTextColor = Color.Black, // Text color when selected
                            unselectedIconColor = Color.White, // Icon color when not selected
                            unselectedTextColor = Color.White // Text color when not selected
                        )
                    )
                    NavigationBarItem(
                        selected = backStackEntry.value?.destination?.route == "setting",
                        onClick = { navController.navigate("setting") },
                        icon = { Icon(painter = painterResource(id = R.drawable.setting), contentDescription = "setting", modifier = Modifier.size(34.dp)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black, // Icon color when selected
                            selectedTextColor = Color.Black, // Text color when selected
                            unselectedIconColor = Color.White, // Icon color when not selected
                            unselectedTextColor = Color.White // Text color when not selected
                        )
                    )
                }
            }
        },
        content = { innerPadding ->
            NavHost(navController = navController, startDestination = "home") {
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
                composable(
                    route = "edit_expense/{expenseId}",
                    arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val expenseId = backStackEntry.arguments?.getInt("expenseId")
                    // Pass the expenseId to AddExpense composable
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