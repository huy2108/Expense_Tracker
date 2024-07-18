package com.example.expensetracker

import android.media.Image
import androidx.compose.foundation.Image // Image related components
import androidx.compose.foundation.background // Background related components
import androidx.compose.foundation.border // Border related components
import androidx.compose.foundation.clickable // Clickable related components
import androidx.compose.foundation.layout.Box // Box related components
import androidx.compose.foundation.layout.Column // Column related components
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align // Aligning components
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align // Aligning components
import androidx.compose.foundation.layout.Row // Row related components
import androidx.compose.foundation.layout.Spacer // Spacing related components
import androidx.compose.foundation.layout.fillMaxSize // Max sizing
import androidx.compose.foundation.layout.fillMaxWidth // Max width
import androidx.compose.foundation.layout.height // Setting height
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.layout.size // Sizing components
import androidx.compose.foundation.lazy.LazyColumn // Lazy column
import androidx.compose.foundation.lazy.items // List items
import androidx.compose.foundation.shape.RoundedCornerShape // Rounded corner shape
import androidx.compose.material3.DropdownMenu // Dropdown menu
import androidx.compose.material3.DropdownMenuItem // Dropdown menu item
import androidx.compose.material3.Surface // Surface
import androidx.compose.material3.Text // Text components
import androidx.compose.runtime.Composable // Composable function
import androidx.compose.runtime.MutableState // Mutable state
import androidx.compose.runtime.collectAsState // Collect state as state
import androidx.compose.runtime.getValue // Getting value
import androidx.compose.runtime.mutableStateOf // Mutable state of
import androidx.compose.runtime.remember // Remember
import androidx.compose.runtime.setValue // Setting value
import androidx.compose.ui.Alignment // Alignment
import androidx.compose.ui.tooling.preview.Preview // Preview
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.draw.clip // Clipping
import androidx.compose.ui.graphics.Color // Color
import androidx.compose.ui.platform.LocalContext // Local context
import androidx.compose.ui.res.painterResource // Painter resource
import androidx.compose.ui.text.font.FontWeight // Font weight
import androidx.compose.ui.unit.dp // Density-independent pixels
import androidx.compose.ui.unit.sp // Scaled pixels
import androidx.constraintlayout.compose.ConstraintLayout // Constraint layout
import androidx.constraintlayout.compose.Dimension // Dimension
import androidx.navigation.NavController // Navigation controller
import androidx.navigation.compose.rememberNavController // Remember navigation controller
import com.example.expensetracker.data.model.ExpenseEntity // Expense entity
import com.example.expensetracker.ui.theme.Zinc // Theme
import com.example.expensetracker.viewmodel.HomeViewModel // Home view model
import com.example.expensetracker.viewmodel.HomeViewModelFactory // Home view model factory
import java.time.format.TextStyle // Text style

/**
 * Composable function for rendering the home screen.
 */
@Composable
fun HomeScreen(navController: NavController){
    // Creating view model instance for home screen
    val viewModel : HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)

    // Surface to cover entire screen
    Surface(modifier = Modifier.fillMaxSize()) {
        // Constraint layout to position UI elements
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            // References for layout constraints
            val(nameRow, list, card, topBar, addIcon) = createRefs()

            // Collecting expenses state
            val state = viewModel.allExpenses.collectAsState(initial = emptyList())
            // Calculating total expense
            val expense = viewModel.getTotalExpense(state.value)
            // Calculating total income
            val income = viewModel.getTotalIncome(state.value)
            // Calculating balance
            val balance = viewModel.getBalance(state.value)
            // Collecting expenses by type state
            val expensesByType by viewModel.expensesByType.collectAsState(initial = emptyList())
            // Mutable state for expense type
            val type = remember {
                mutableStateOf("")
            }

            // Top bar image
            Image(painter = painterResource(id = R.drawable.ic_header), contentDescription = null,
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            // Box for name row
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
                // Column for displaying user info
                Column{
                    Text(text = "Hello There!,", fontSize = 18.sp, color = Color.White)
                    Text(text = "Lê Quang Huy", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                // Image aligned to center end
                Image(painter = painterResource(id = R.drawable.frame_4),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                )
            }

            // Card item for displaying financial data
            CardItem(modifier = Modifier
                .constrainAs(card){
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, "${balance}", "${income}", "${expense}", type, viewModel
            )

            // Getting all expenses by type
            viewModel.getAllExpensesByType(type.value)

            // Checking type for income or expense
            if(type.value == "Income" || type.value == "Expense"){
                // Transaction list for specific type
                TransactionList(modifier = Modifier
                    .constrainAs(list){
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },expensesByType, viewModel, type, navController)
            }else {
                // Transaction list for all types
                TransactionList(modifier = Modifier
                    .constrainAs(list){
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },list = state.value, viewModel, type,navController)
            }
        }
    }
}

/**
 * Composable function for rendering the card item.
 */
@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expense: String, type: MutableState<String>,viewModel: HomeViewModel){
    // Column for card item
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Zinc)
        .padding(16.dp)
    ){
        // Mutable state for showing menu
        var showMenu by remember {
            mutableStateOf(false)
        }

        // Box for balance and type
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            // Column for aligning start
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                Text(
                    text = balance,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            // Column for aligning end
            Column (modifier = Modifier.align(Alignment.CenterEnd)){
                Image(painter = painterResource(id = R.drawable.ic_dot),
                    contentDescription = null,
                    modifier = Modifier
//                    .align(Alignment.CenterEnd)
                        .clickable { showMenu = !showMenu }
                )
                // Dropdown for expense
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
        // Box for amount
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){
            // Row item
            CardRowItem(modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image = R.drawable.frame_7,
                textStyle = Alignment.Start
            )
            // Row item
            CardRowItem(modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expense,
                image = R.drawable.frame_5,
                textStyle = Alignment.End
            )
        }

    }
}

/**
 * Composable function for rendering the dropdown menu for expenses.
 */
@Composable
fun ExpenseDropDown(
    modifier: Modifier = Modifier,
    items: List<String>,
    showMenu: Boolean,
    onDismissRequest: () -> Unit,
    onItemSelected: (String) -> Unit
) {
    // Dropdown menu item
    DropdownMenu(
        modifier = modifier,
        expanded = showMenu,
        onDismissRequest = onDismissRequest
    ) {
        // Dropdown menu items
        items.forEach { item ->
            DropdownMenuItem(
                text = {Text(text = item)},
                onClick = {
                    onItemSelected(item)
                })
        }
    }
}

/**
 * Composable function for rendering the transaction list.
 */
@Composable
fun TransactionList(modifier: Modifier, list: List<ExpenseEntity>, viewModel: HomeViewModel, type: MutableState<String>,navController: NavController){

    // Lazy column for transaction list
    LazyColumn(modifier = modifier) {
        // Item for transactions
        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp))
            {
                // Checking type for income or expense
                if(type.value == "Income" || type.value == "Expense"){
                    Text(text = type.value , fontSize = 22.sp)
                }
                // Else displaying all transactions
                else {
                    Text(text = "Recent Transactions", fontSize = 22.sp)
                }

                // Text for all transactions
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
        // Items for transactions
        items(list){ item ->
            // Transaction item
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

/**
 * Composable function for rendering the card row item.
 */
@Composable
fun CardRowItem(modifier: Modifier,
                title:String,
                amount:String,
                image:Int,
                textStyle: Alignment.Horizontal = Alignment.CenterHorizontally)
{
    // Column for card row
    Column(modifier = modifier) {
        // Row item
        Row{
            // Image for card row
            Image(painter = painterResource(id = image), contentDescription = null)
            // Spacer for card row
            Spacer(modifier = Modifier.size(8.dp))
            // Text for title
            Text(text = title, fontSize = 16.sp, color = Color.White)
        }
        // Text for amount
        Text(text = amount, fontWeight = FontWeight.Medium,fontSize = 24.sp, color = Color.White, modifier = Modifier.align(textStyle))
    }
}

/**
 * Composable function for rendering the transaction item.
 */
@Composable
fun TransactionItem(title: String, amount:String, icon: Int, date: String, color: Color, onClick: () -> Unit){
    // Box for transaction item
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable { onClick() }
    ){
        // Row for transaction item
        Row{
            // Image for transaction item
            Image(painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            // Spacer for transaction item
            Spacer(modifier = Modifier.size(8.dp))
            // Column for transaction item
            Column {
                // Text for transaction title
                Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                // Text for transaction date
                Text(text = date, fontSize = 14.sp)
            }
        }
        // Text for transaction amount
        Text(text = if(color == Color.Red) "- $ ${amount}" else "+ $ ${amount}",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Preview function for rendering the home screen.
 */
@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen(){
    // Previewing home screen with navigation controller
    HomeScreen(rememberNavController())
}
