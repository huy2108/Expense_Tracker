package com.example.expensetracker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Text // Importing Text from Material3 library
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.data.model.ExpenseEntity
import com.example.expensetracker.ui.theme.Zinc
import com.example.expensetracker.viewmodel.HomeViewModel
import com.example.expensetracker.viewmodel.HomeViewModelFactory

/**
 * Composable function for displaying the bookmarked expenses screen.
 *
 * @param navController NavController used for navigation between composables.
 */
@Composable
fun BookMark(navController: NavController){
    val viewModel : HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)

    // Surface composable for the entire screen
    Surface(modifier = Modifier.fillMaxSize()) {
        // ConstraintLayout for positioning components
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs() // Declaring constraints

            // Fetching bookmarked expenses using ViewModel
            val expensesByBookmarkTrue by viewModel.expensesByBookmarkTrue.collectAsState(initial = emptyList())

            // Top bar with image and title
            Image(painter = painterResource(id = R.drawable.heroimage), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                    .size(width = 435.dp, height = 285.dp)
            )

            // Box containing the title "Bookmark"
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 60.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ){
                Text(text = "Bookmark",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }

            // Column for displaying bookmarked transactions
            Column(
                modifier = Modifier
                    .padding(30.dp)
                    .shadow(10.dp, RoundedCornerShape(16.dp)) // Shadow applied outside
                    .clip(RoundedCornerShape(16.dp)) // Clip applied to ensure rounded corners
                    .background(Zinc) // Apply border color to the background of the clipped area
                    .border(BorderStroke(1.dp, Zinc)) // Border around the clipped area
                    .width(360.dp)
                    .constrainAs(list) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
            ) {
                // LazyColumn to lazily load and display items in a vertical list
                TransactionBookMarkList(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White) // Apply white background to inner column
                        .padding(16.dp), // Set padding for the TransactionBookMarkList
                    list = expensesByBookmarkTrue, // List of bookmarked expenses
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

/**
 * Composable function for displaying a list of bookmarked transactions.
 *
 * @param modifier Modifier for styling the list.
 * @param list List of bookmarked expenses to display.
 * @param viewModel ViewModel instance for handling business logic.
 * @param navController NavController used for navigation.
 */
@Composable
fun TransactionBookMarkList(modifier: Modifier, list: List<ExpenseEntity>, viewModel: HomeViewModel, navController: NavController){

    // LazyColumn for efficiently scrolling through a list of items
    LazyColumn(modifier = modifier) {
        // Iterating through each item in the list and displaying TransactionItem composable
        items(list){ item ->
            TransactionItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = viewModel.getItemIcon(item),
                date = Utils.formatDateToReadableForm(item.date),
                color = if(item.type == "Income") Color.Green else Color.Red, // Color based on type (Income/Expense)
                onClick = {
                    navController.navigate("edit_expense/${item.id}") // Navigate to edit expense screen on item click
                }
            )
        }
    }
}

/**
 * Preview function for the BookMark composable.
 */
@Composable
@Preview(showBackground = true)
fun PreviewGraphScreen(){
    BookMark(rememberNavController()) // Previewing the BookMark composable
}
