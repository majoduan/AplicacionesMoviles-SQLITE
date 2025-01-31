package com.example.empresa_empleado.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "empleados")
data class EmpleadoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val empresaId: Int,  // Relación con la empresa
    val nombre: String,
    val departamento: String,
    var salario: Double,
    )
