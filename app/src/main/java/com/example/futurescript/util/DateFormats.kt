package com.example.futurescript.util

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun showDatePicker(
    context: Context,
    initial: LocalDate = LocalDate.now(),
    onPicked: (LocalDate) -> Unit
) {
    DatePickerDialog(
        context,
        { _, y, m, d -> onPicked(LocalDate.of(y, m + 1, d)) },
        initial.year, initial.monthValue - 1, initial.dayOfMonth
    ).show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun localDateToEpochSeconds(ld: LocalDate): Long =
    ld.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
