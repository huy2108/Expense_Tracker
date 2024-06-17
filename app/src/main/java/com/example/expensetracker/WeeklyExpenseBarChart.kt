import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


@Composable
fun WeeklyExpenseBarChart(expenses: List<Pair<Long, Double>>) {
    AndroidView(factory = { context ->
        BarChart(context).apply {
            description.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            axisRight.isEnabled = false
            legend.isEnabled = false
        }
    }) { chart ->
        val entries = expenses.mapIndexed { index, expense ->
            BarEntry(index.toFloat(), expense.second.toFloat())
        }

        val dataSet = BarDataSet(entries, "Expenses").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
        }

        val barData = BarData(dataSet)
        chart.data = barData
        chart.invalidate() // Refresh the chart
    }
}
