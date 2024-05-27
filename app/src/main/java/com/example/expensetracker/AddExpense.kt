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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun AddExpense() {
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
                Image(painter = painterResource(id = R.drawable.chevron_left), contentDescription = null, Modifier.align(Alignment.CenterStart))
                Text(text = "Add Expense",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(painter = painterResource(id = R.drawable.ic_dot), contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd))
            }
            DataForm(modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 60.dp)
            )
        }
    }
}

@Composable
fun DataForm(modifier: Modifier){

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
        Text(text = "TYPE", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = type.value, onValueChange = {type.value = it}, modifier = Modifier
            .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))


        Text(text = "NAME", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = name.value, onValueChange = {name.value = it}, modifier = Modifier
            .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))


        Text(text = "CATEGORY", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = "c", onValueChange = {}, modifier = Modifier
            .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))


        Text(text = "AMOUNT", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = "c", onValueChange = {}, modifier = Modifier
            .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))



        Text(text = "DATE", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = if(date.value == 0L) "" else Utils.formatDateToReadableForm(date.value), onValueChange = {}, modifier = Modifier
            .fillMaxWidth()
            .clickable { dateDialogVisibility.value = true },
            enabled = false
        )
        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = { /*TODO*/ },
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .padding(top = 25.dp)

        )
        {
            Text(text = "Add Expense", fontSize = 14.sp)
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

@Composable
@Preview(showBackground = true)
fun AddExpensePreview(){
    AddExpense()
}