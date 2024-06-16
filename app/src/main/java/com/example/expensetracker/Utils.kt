package com.example.expensetracker

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDateToReadableForm(dateInMillis: Long) : String{
        val dateFormtter = SimpleDateFormat("dd/MM/YY", Locale.getDefault())
        return dateFormtter.format(dateInMillis)
    }

    fun formatToDecimalValue(d: Double): String {
        return String.format("%.2f", d)
    }
}