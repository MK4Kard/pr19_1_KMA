package com.example.criminalintent

import androidx.room.Entity
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Entity
data class Crime(val id: UUID = UUID.randomUUID()) {

    var title: String = ""
    var date: Date = Date()
    var isSolved: Boolean = false
    var name: String = ""

}