package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.viewmodel.AddExpenseViewModel
import com.example.expensetracker.viewmodel.AddExpenseViewModelFactory
import kotlinx.coroutines.launch

/**
 * Composable function for the Setting screen.
 *
 * @param navController NavController used for navigation.
 */
@Composable
fun Setting(navController: NavController){
    // Create an instance of AddExpenseViewModel using AddExpenseViewModelFactory
    val viewModel = AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            // Define references for ConstraintLayout
            val (nameRow, list, card, topBar) = createRefs()

            // Top bar with an image header
            Image(painter = painterResource(id = R.drawable.heroimage), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                    .size(width = 435.dp, height = 285.dp)
            )

            // Title text "Setting" centered in a Box
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 80.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ){
                Text(text = "Setting",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }

            // Content area for data manipulation
            DataManipulation(modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 30.dp),
                viewModel,
                navController
            )
        }
    }
}

/**
 * Composable function for displaying data manipulation options.
 *
 * @param modifier Modifier for styling and layout.
 * @param viewModel Instance of AddExpenseViewModel for handling data operations.
 * @param navController NavController used for navigation.
 */
@Composable
fun DataManipulation(modifier: Modifier, viewModel: AddExpenseViewModel, navController: NavController){
    Column(modifier = modifier
        .padding(16.dp)
        .shadow(16.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ){
        // Button to delete all expenses
        androidx.compose.material3.Button(
            onClick = {
                viewModel.deleteAllExpenses()
                navController.navigate("home") // Navigate back to home screen after deletion
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red // Set the button background color to Red for delete all
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            androidx.compose.material3.Text(text = "Erase All Data", fontSize = 14.sp)
        }
    }
}

/**
 * Preview function to display a preview of the Setting screen.
 */
@Composable
@Preview(showBackground = true)
fun PreviewSettingScreen(){
    Setting(rememberNavController())
}
