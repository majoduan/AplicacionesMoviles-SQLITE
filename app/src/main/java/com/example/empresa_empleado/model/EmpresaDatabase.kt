package com.example.empresa_empleado.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EmpresaEntity::class, EmpleadoEntity::class], version = 1, exportSchema = false)
abstract class EmpresaDatabase : RoomDatabase() {
    abstract fun empresaDao(): EmpresaDao
    abstract fun empleadoDao(): EmpleadoDao

    companion object {
        @Volatile
        private var INSTANCE: EmpresaDatabase? = null

        fun getDatabase(context: Context): EmpresaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmpresaDatabase::class.java,
                    "empresa_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

