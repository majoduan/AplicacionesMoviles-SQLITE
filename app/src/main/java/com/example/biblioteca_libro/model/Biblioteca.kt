package com.example.biblioteca_libro.model

data class Biblioteca(
    val id: Int,
    var nombre: String,
    var direccion: String,
    var fechaInauguracion: String, // Usaremos String para simplificar
    var abiertaAlPublico: Boolean,
    val libros: MutableList<Libro> = mutableListOf()
)
