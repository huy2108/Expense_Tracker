package com.example.expensetracker

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import java.time.format.TextStyle

@Composable
fun HomeScreen(navController: NavController){
    val viewModel : HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val(nameRow, list, card, topBar, addIcon) = createRefs()
            val state = viewModel.allExpenses.collectAsState(initial = emptyList())
            val expense = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)
            val expensesByType by viewModel.expensesByType.collectAsState(initial = emptyList())
            val type = remember {
                mutableStateOf("")
            }

            Image(painter = painterResource(id = R.drawable.ic_header), contentDescription = null,
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){
                Column{
                    Text(text = "Hello There!,", fontSize = 18.sp, color = Color.White)
                    Text(text = "Lê Quang Huy", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Image(painter = painterResource(id = R.drawable.frame_4),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                )
            }
            CardItem(modifier = Modifier
                .constrainAs(card){
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, "${balance}", "${income}", "${expense}", type, viewModel
            )

            viewModel.getAllExpensesByType(type.value)
            if(type.value == "Income" || type.value == "Expense"){
                TransactionList(modifier = Modifier
                    .constrainAs(list){
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },expensesByType, viewModel, type, navController)
            }else {
                TransactionList(modifier = Modifier
                    .constrainAs(list){
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },list = state.value, viewModel, type,navController)
            }


//            Image(painter = painterResource(id = R.drawable.group_10),
//                contentDescription = null,
//                Modifier
//                    .constrainAs(addIcon) {
//                        bottom.linkTo(parent.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//                    .clickable {
//                        navController.navigate("/addExpense")
//                    }
//                    .size(100.dp)
//
//            )
        }
    }
}

@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expense: String, type: MutableState<String>,viewModel: HomeViewModel){
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Zinc)
        .padding(16.dp)
    ){
        var showMenu by remember {
            mutableStateOf(false)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                Text(
                    text = balance,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Column (modifier = Modifier.align(Alignment.CenterEnd)){
                Image(painter = painterResource(id = R.drawable.ic_dot),
                    contentDescription = null,
                    modifier = Modifier
//                    .align(Alignment.CenterEnd)
                        .clickable { showMenu = !showMenu }
                )
                ExpenseDropDown(
                    items = listOf("--All--","Income", "Expense"),
                    showMenu = showMenu,
                    onDismissRequest = { showMenu = false },
                    onItemSelected = { selectedType ->
                        type.value = selectedType
                        showMenu = false
                    }
                )
            }

        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){
            CardRowItem(modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image = R.drawable.frame_7,
                textStyle = Alignment.Start
            )
            CardRowItem(modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expense,
                image = R.drawable.frame_5,
                textStyle = Alignment.End
            )
        }

    }
}

@Composable
fun ExpenseDropDown(
    modifier: Modifier = Modifier,
    items: List<String>,
    showMenu: Boolean,
    onDismissRequest: () -> Unit,
    onItemSelected: (String) -> Unit
) {
    DropdownMenu(
        modifier = modifier,
        expanded = showMenu,
        onDismissRequest = onDismissRequest
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = {Text(text = item)},
                onClick = {
                    onItemSelected(item)
                })
        }
    }
}
@Composable
fun TransactionList(modifier: Modifier, list: List<ExpenseEntity>, viewModel: HomeViewModel, type: MutableState<String>,navController: NavController){

    LazyColumn(modifier = modifier) {
        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp))
            {
                if(type.value == "Income" || type.value == "Expense"){
                    Text(text = type.value , fontSize = 22.sp)
                }
                else {
                    Text(text = "Recent Transactions", fontSize = 22.sp)
                }

                Text(text = "See All",
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            type.value = "--All--"
                        }
                )
            }
        }
        items(list){ item ->
            TransactionItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = viewModel.getItemIcon(item),
                date = Utils.formatDateToReadableForm(item.date),
                color = if(item.type == "Income") Color.Green else Color.Red,
                onClick = {
                    navController.navigate("edit_expense/${item.id}")
                }
            )
            
        }
    }
}
@Composable
fun CardRowItem(modifier: Modifier,
                title:String,
                amount:String,
                image:Int,
                textStyle: Alignment.Horizontal = Alignment.CenterHorizontally)
                {
                    Column(modifier = modifier) {
                        Row{
                            Image(painter = painterResource(id = image), contentDescription = null)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = title, fontSize = 16.sp, color = Color.White)
                        }
                        Text(text = amount, fontWeight = FontWeight.Medium,fontSize = 24.sp, color = Color.White, modifier = Modifier.align(textStyle))
                    }
                }

@Composable
fun TransactionItem(title: String, amount:String, icon: Int, date: String, color: Color, onClick: () -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable { onClick() }
    ){
        Row{
            Image(painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                Text(text = date, fontSize = 14.sp)
            }
        }
        Text(text = if(color == Color.Red) "- $ ${amount}" else "+ $ ${amount}",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen(){
    HomeScreen(rememberNavController())
}