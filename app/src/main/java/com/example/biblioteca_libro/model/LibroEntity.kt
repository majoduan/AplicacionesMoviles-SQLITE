package com.example.biblioteca_libro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "libros")
data class LibroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bibliotecaId: Int,  // Relación con la biblioteca
    val titulo: String,
    val autor: String
)
