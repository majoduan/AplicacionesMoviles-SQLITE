package com.example.biblioteca_libro.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(entities = [BibliotecaEntity::class, LibroEntity::class], version = 1, exportSchema = false)
abstract class BibliotecaDatabase : RoomDatabase() {
    abstract fun bibliotecaDao(): BibliotecaDao
    abstract fun libroDao(): LibroDao

    companion object {
        @Volatile
        private var INSTANCE: BibliotecaDatabase? = null

        fun getDatabase(context: Context): BibliotecaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BibliotecaDatabase::class.java,
                    "biblioteca_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

