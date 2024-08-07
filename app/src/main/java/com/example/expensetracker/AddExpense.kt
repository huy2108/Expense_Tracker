@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expensetracker

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.expensetracker.data.model.ExpenseEntity
import com.example.expensetracker.ui.theme.Zinc
import com.example.expensetracker.viewmodel.AddExpenseViewModel
import com.example.expensetracker.viewmodel.AddExpenseViewModelFactory
import kotlinx.coroutines.launch
import kotlin.math.exp

/**
 * Composable function for adding an expense.
 * Handles input of expense details and addition through ViewModel.
 *
 * @param navController NavController for navigating between screens.
 */
@Composable
fun AddExpense(navController: NavController) {
    // Create ViewModel instance using ViewModelFactory
    val viewModel = AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
    val courinetineScope = rememberCoroutineScope()
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()

            // Top bar with header image
            Image(painter = painterResource(id = R.drawable.heroimage), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                    .size(width = 435.dp, height = 285.dp)
            )

            // Box containing "Add Expense" text
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 60.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ){

                Text(text = "Add Expense",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )

            }

            // Form for entering expense details
            DataForm(modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 5.dp),
                onAddExpenseClick = {
                    // Invoke ViewModel method to add expense
                    courinetineScope.launch {
                        if(viewModel.addExpense(it)){
                            // Navigate back to previous screen
                            navController.navigate("home")
                        }
                    }
                }
            )
        }
    }
}

/**
 * Composable function for displaying the form to input expense details.
 *
 * @param modifier Modifier for styling the UI element.
 * @param onAddExpenseClick Callback invoked when adding expense.
 */
@Composable
fun DataForm(modifier: Modifier, onAddExpenseClick: (model: ExpenseEntity) -> Unit){

    // Mutable state variables for input fields
    val name = remember{
        mutableStateOf("")
    }
    val type = remember {
        mutableStateOf("")
    }
    val date = remember{
        mutableStateOf(0L)
    }
    val dateDialogVisibility = remember {
        mutableStateOf(false)
    }
    val category = remember {
        mutableStateOf("")
    }
    val amount = remember {
        mutableStateOf("")
    }

    Column(modifier = modifier
        .padding(16.dp)
        .shadow(16.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    )
    {

        // Input fields for expense details
        Text(text = "NAME", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = name.value, onValueChange = {name.value = it}, modifier = Modifier
            .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = "AMOUNT", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = amount.value, onValueChange = {amount.value = it}, modifier = Modifier
            .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))


        Text(text = "CATEGORY", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        // Dropdown menu for selecting expense category
        ExpenseDropDown(listOf("--CATEGORY--","Netflix", "Paypal", "Salary", "Upwork","Others"),
            onItemSelected = {
                category.value = it
            })
        Spacer(modifier = Modifier.size(16.dp))


        Text(text = "TYPE", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        // Dropdown menu for selecting expense type
        ExpenseDropDown(listOf("--TYPE--","Income", "Expense"),
            onItemSelected = {
                type.value = it
            })
        Spacer(modifier = Modifier.size(16.dp))


        Text(text = "DATE", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        // Text field for displaying selected date
        OutlinedTextField(value = if(date.value == 0L) "" else Utils.formatDateToReadableForm(date.value), onValueChange = {}, modifier = Modifier
            .fillMaxWidth()
            .clickable { dateDialogVisibility.value = true },
            enabled = false
        )
        Spacer(modifier = Modifier.size(16.dp))

        // Button to add expense
        Button(
            onClick = {
                      val model = ExpenseEntity(
                          null,
                          name.value,
                          amount.value.toDoubleOrNull() ?: 0.0,
                          date.value,
                          category.value,
                          type.value
                      )
                onAddExpenseClick(model)

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Zinc // Set the button background color to Zinc
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .padding(top = 25.dp)
        )
        {
            Text(text = "Add Expense", fontSize = 14.sp)
        }
    }

    // Display date picker dialog if visibility is true
    if(dateDialogVisibility.value){
        ExpenseDatePickerDialog(onDateSelected =
        {
            date.value = it
            dateDialogVisibility.value = false
        }, onDismiss = {
            dateDialogVisibility.value = false
        })
    }
}

/**
 * Composable function for date picker dialog.
 *
 * @param onDateSelected Callback invoked when date is selected.
 * @param onDismiss Callback invoked when dialog is dismissed.
 */
@Composable
fun ExpenseDatePickerDialog(
    onDateSelected: (date: Long) -> Unit,
    onDismiss: () -> Unit
){
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    DatePickerDialog(onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDateSelected(selectedDate) }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDateSelected(selectedDate) }) {
                Text(text = "Cancel")
            }
        }

    ) {
        DatePicker(state = datePickerState)
    }
}

/**
 * Composable function for dropdown menu to select expense category or type.
 *
 * @param listOfItems List of items to display in dropdown menu.
 * @param onItemSelected Callback invoked when item is selected.
 */
@Composable
fun ExpenseDropDown(listOfItems: List<String>, onItemSelected: (item: String) -> Unit){
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf(listOfItems[0])
    }
    ExposedDropdownMenuBox(expanded =  expanded.value, onExpandedChange = { expanded.value = it }) {
        TextField(value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            })
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { }) {
            listOfItems.forEach{
                DropdownMenuItem(text = { Text(text = it) }, onClick = {
                    selectedItem.value = it
                    onItemSelected(selectedItem.value)
                    expanded.value = false
                })
            }
        }
    }
}

/**
 * Preview function to display AddExpense composable in preview mode.
 */
@Composable
@Preview(showBackground = true)
fun AddExpensePreview(){
    AddExpense(rememberNavController())
}