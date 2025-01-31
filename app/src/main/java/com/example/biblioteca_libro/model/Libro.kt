package com.example.biblioteca_libro.model

data class Libro(
    val id: Int,
    var titulo: String,
    var autor: String,
    var precio: Double,
    var fechaPublicacion: String // Usaremos String para simplificar
)
