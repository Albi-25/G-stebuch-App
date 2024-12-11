package com.example.aufgabe3.model

import java.time.LocalDate

data class BookingEntry(
    val id: Int, // Unique identifier
    val name: String,
    val arrivalDate: LocalDate,
    val departureDate: LocalDate
)
