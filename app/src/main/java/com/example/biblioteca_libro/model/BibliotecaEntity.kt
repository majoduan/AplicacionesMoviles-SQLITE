package com.example.biblioteca_libro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bibliotecas")
data class BibliotecaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val direccion: String,
    val fechaInauguracion: String,
    val abiertaAlPublico: Boolean
)