package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
//import androidx.compose.material.ButtonDefaults

@Composable
fun EditExpense(navController: NavController, expenseId: Int?){
    val viewModel = AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)

    val name = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }
    val date = remember { mutableStateOf(0L) }
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val category = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val currentExpense = remember { mutableStateOf<ExpenseEntity?>(null) }


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
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_header), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 60.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ){
                Text(
                    text = "Edit Expense",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
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
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = amount.value,
                    onValueChange = { amount.value = it },
                    label = { Text("Amount") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                    Text(
                        text = "Date",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
                    )
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


                    Text(
                        text = "Category",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Start).padding(start = 16.dp, top = 16.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    ExpenseDropDownEdit(
                        listOfItems = listOf("--CATEGORY--","Netflix", "Paypal", "Salary", "Upwork","Others"),
                        value = category.value,
                        onItemSelected = { category.value = it },
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                    )

                    Text(
                        text = "Type",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Start).padding(start = 16.dp, top = 16.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    ExpenseDropDownEdit(
                        listOfItems = listOf("--TYPE--", "Income", "Expense"),
                        value = type.value,
                        onItemSelected = { type.value = it },
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                    )


                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    androidx.compose.material3.Button(
                        onClick = {
                            currentExpense.value?.let {
                                viewModel.deleteExpense(it)
                                navController.popBackStack()
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
                            navController.popBackStack()
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





@Composable
@Preview(showBackground = true)
fun PreviewEditScreen(){
    EditExpense(rememberNavController(), expenseId = null)
}