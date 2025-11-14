package com.example.futurescript.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.futurescript.data.database.dao.LetterDao
import com.example.futurescript.data.database.entities.Letter

@Database(entities = [Letter::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun letterDao(): LetterDao
}
