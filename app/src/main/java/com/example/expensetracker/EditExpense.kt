package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.data.model.ExpenseEntity
import com.example.expensetracker.ui.theme.Zinc
import com.example.expensetracker.viewmodel.AddExpenseViewModel
import com.example.expensetracker.viewmodel.AddExpenseViewModelFactory
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.ColorFilter

//import androidx.compose.material.ButtonDefaults

/**
 * Composable function for editing an expense.
 *
 * @param navController The NavController used for navigating between composables.
 * @param expenseId The ID of the expense to edit.
 */
@Composable
fun EditExpense(navController: NavController, expenseId: Int?){
    val viewModel = AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)

    // Mutable state variables for holding form data and UI state
    val name = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }
    val date = remember { mutableStateOf(0L) }
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val category = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val bookmark = remember { mutableStateOf(false) }
    val currentExpense = remember { mutableStateOf<ExpenseEntity?>(null) }
    val selectedIconColor = Color.White


    // Effect to fetch expense details when expenseId changes
    LaunchedEffect(expenseId) {
        expenseId?.let {
            viewModel.getExpenseById(it).collect { expense ->
                expense?.let {
                    currentExpense.value = it
                    name.value = it.title
                    type.value = it.type
                    date.value = it.date
                    category.value = it.category
                    amount.value = it.amount.toString()
                    bookmark.value = it.bookmark
                }
            }
        }
    }

    // Surface composable for the entire screen
    Surface(modifier = Modifier.fillMaxSize()) {
        // ConstraintLayout for positioning components
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()

            // Top bar with image and title
            Image(painter = painterResource(id = R.drawable.ic_header), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            // Box containing navigation back button, title, and bookmark toggle
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 60.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ){
                Image(painter = painterResource(id = R.drawable.chevron_left), contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            navController.navigate("home")
                        }
                )
                Text(
                    text = "Edit Expense",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = if (bookmark.value) R.drawable.bookmarkfilled else R.drawable.bookmarkoutlined),
                    contentDescription = null,
                    colorFilter = if (bookmark.value) ColorFilter.tint(selectedIconColor) else null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
                        .clickable {
                            bookmark.value = !bookmark.value
                            val editedExpense = ExpenseEntity(
                                id = expenseId,
                                title = name.value,
                                amount = amount.value.toDoubleOrNull() ?: 0.0,
                                date = date.value,
                                category = category.value,
                                type = type.value,
                                bookmark = bookmark.value
                            )
                            viewModel.updateExpense(editedExpense)
                        }
                )

            }

            // Column for form fields
            Column(modifier = Modifier
                .padding(30.dp)
                .shadow(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .verticalScroll(rememberScrollState())
                .background(color = Color.White)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                // OutlinedTextField for Name field
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                // OutlinedTextField for Amount field
                OutlinedTextField(
                    value = amount.value,
                    onValueChange = { amount.value = it },
                    label = { Text("Amount") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Text for Date field
                Text(
                    text = "Date",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )
                // OutlinedTextField for Date field
                OutlinedTextField(
                    value = if (date.value == 0L) "" else Utils.formatDateToReadableForm(date.value),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dateDialogVisibility.value = true }
                        .padding(start = 16.dp, end = 16.dp)
                    ,
                    enabled = false
                )

                // Text for Category field
                Text(
                    text = "Category",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp, top = 16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                // Dropdown for Category selection
                ExpenseDropDownEdit(
                    listOfItems = listOf("--CATEGORY--","Netflix", "Paypal", "Salary", "Upwork","Others"),
                    value = category.value,
                    onItemSelected = { category.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                )

                // Text for Type field
                Text(
                    text = "Type",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp, top = 16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                // Dropdown for Type selection
                ExpenseDropDownEdit(
                    listOfItems = listOf("--TYPE--", "Income", "Expense"),
                    value = type.value,
                    onItemSelected = { type.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                )

                // Row for Delete and Save buttons
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Button for Delete action
                    androidx.compose.material3.Button(
                        onClick = {
                            currentExpense.value?.let {
                                viewModel.deleteExpense(it)
                                navController.navigate("home")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red // Set the button background color to Red for delete
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .padding(end = 8.dp)
                    ) {
                        androidx.compose.material3.Text(text = "Delete", fontSize = 14.sp)
                    }
                    // Button for Save action
                    androidx.compose.material3.Button(
                        onClick = {
                            val editedExpense = ExpenseEntity(
                                id = expenseId,
                                title = name.value,
                                amount = amount.value.toDoubleOrNull() ?: 0.0,
                                date = date.value,
                                category = category.value,
                                type = type.value
                            )
                            viewModel.updateExpense(editedExpense)
                            navController.navigate("home")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Zinc // Set the button background color to Zinc
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .padding(end = 8.dp)
                    ) {
                        androidx.compose.material3.Text(text = "Save", fontSize = 14.sp)
                    }
                }

                // Display date picker dialog if dateDialogVisibility is true
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
        }

    }
}

/**
 * Composable function for displaying an editable dropdown list.
 *
 * @param listOfItems The list of items to display in the dropdown.
 * @param value The currently selected value from the dropdown.
 * @param onItemSelected Callback function triggered when an item is selected.
 * @param modifier Modifier for styling the dropdown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDownEdit(listOfItems: List<String>, value: String, onItemSelected: (item: String) -> Unit, modifier: Modifier) {
    val expanded = remember { mutableStateOf(false) }
    val selectedItem = remember(value) { mutableStateOf(value) }

    ExposedDropdownMenuBox(modifier = modifier,expanded = expanded.value, onExpandedChange = { expanded.value = it }) {
        TextField(
            value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            }
        )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            listOfItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedItem.value = item
                        onItemSelected(item)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

/**
 * Preview function for EditExpense composable.
 */
@Composable
@Preview(showBackground = true)
fun PreviewEditScreen(){
    EditExpense(rememberNavController(), expenseId = null)
}
