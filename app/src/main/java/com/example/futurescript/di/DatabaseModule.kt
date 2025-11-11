package com.example.futurescript.di

import android.content.Context
import androidx.room.Room
import com.example.futurescript.data.database.AppDatabase
import com.example.futurescript.data.database.dao.LetterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "letter_database.db"
        ).build()
    }

    @Provides
    fun provideLetterDao(database: AppDatabase): LetterDao {
        return database.letterDao()
    }
}
