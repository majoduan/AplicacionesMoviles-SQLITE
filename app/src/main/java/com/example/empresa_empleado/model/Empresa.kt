package com.example.empresa_empleado.model

data class Empresa(
    val id: Int,
    var nombre: String,
    var direccion: String,
    var fechaFundacion: String, // Usaremos String para simplificar
    val ingresoAnual: Double,
    var esMultinacional: Boolean,
    val empleados: MutableList<Empleado> = mutableListOf()
)
