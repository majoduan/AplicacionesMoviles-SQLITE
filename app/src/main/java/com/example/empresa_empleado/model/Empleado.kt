package com.example.empresa_empleado.model

data class Empleado(
    val id: Int,
    var nombre: String,
    var departamento: String,
    var salario: Double,
    var fechaContratacion: String // Usaremos String para simplificar
)
