package com.example.expensetracker

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Utility object containing helper functions for formatting data.
 */
object Utils {

    /**
     * Formats a given date in milliseconds to a readable string format.
     *
     * @param dateInMillis The date in milliseconds to format.
     * @return A string representation of the formatted date.
     */
    fun formatDateToReadableForm(dateInMillis: Long) : String{
        // Create a date formatter using the specified format and locale
        val dateFormatter = SimpleDateFormat("dd/MM/YY", Locale.getDefault())
        // Format the date and return the formatted string
        return dateFormatter.format(dateInMillis)
    }

    /**
     * Formats a double value to a string with two decimal places.
     *
     * @param d The double value to format.
     * @return A string representation of the formatted decimal value.
     */
    fun formatToDecimalValue(d: Double): String {
        // Format the double value to a string with two decimal places
        return String.format("%.2f", d)
    }
}
