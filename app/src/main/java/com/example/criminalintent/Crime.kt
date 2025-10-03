package com.example.criminalintent

import java.text.DateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class Crime(val id: UUID = UUID.randomUUID(), val date: DateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault())) {

    var title: String = ""
    var isSolved: Boolean = false

}