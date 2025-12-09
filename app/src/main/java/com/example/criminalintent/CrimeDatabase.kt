package com.example.criminalintent

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Database
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.SupportSQLiteConnection

@Database(entities = [ Crime::class ], version=2)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}
val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteConnection) {
        database.execSQL (
            "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
        )
    }
}