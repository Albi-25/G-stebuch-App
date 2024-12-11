package com.example.aufgabe3.ui.add

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.model.BookingEntry
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    var name by remember { mutableStateOf("") }
    var arrivalDate by remember { mutableStateOf<LocalDate?>(null) }
    var departureDate by remember { mutableStateOf<LocalDate?>(null) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Booking Entry") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = if (arrivalDate != null && departureDate != null) {
                    "${arrivalDate!!.format(dateFormatter)} - ${departureDate!!.format(dateFormatter)}"
                } else {
                    ""
                },
                onValueChange = {},
                label = { Text("Select Date Range") },
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDateRangePicker(
                            context = context,
                            onDateRangeSelected = { start, end ->
                                arrivalDate = start
                                departureDate = end
                            }
                        )
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && arrivalDate != null && departureDate != null) {
                        sharedViewModel.addBookingEntry(
                            BookingEntry(
                                id = System.currentTimeMillis().toInt(),
                                name = name,
                                arrivalDate = arrivalDate!!,
                                departureDate = departureDate!!
                            )
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

private fun showDateRangePicker(
    context: android.content.Context,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit
) {
    val calendar = Calendar.getInstance()
    val startYear = calendar.get(Calendar.YEAR)
    val startMonth = calendar.get(Calendar.MONTH)
    val startDay = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, year, month, dayOfMonth ->
        val startDate = LocalDate.of(year, month + 1, dayOfMonth)

        DatePickerDialog(context, { _, endYear, endMonth, endDayOfMonth ->
            val endDate = LocalDate.of(endYear, endMonth + 1, endDayOfMonth)
            onDateRangeSelected(startDate, endDate)
        }, startYear, startMonth, startDay).show()
    }, startYear, startMonth, startDay).show()
}
