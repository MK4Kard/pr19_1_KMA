package com.example.criminalintent

import java.util.Date
import java.util.UUID

data class Crime(val id: UUID = UUID.randomUUID(), val date: Date = Date()) {

    var title: String = ""
    var isSolved: Boolean = false

}