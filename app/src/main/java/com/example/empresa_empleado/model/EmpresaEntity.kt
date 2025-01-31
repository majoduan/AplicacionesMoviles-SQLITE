package com.example.empresa_empleado.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bibliotecas")
data class EmpresaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val direccion: String,
    val ingresoAnual: Double,
    val fechaFundacion: String,
    val esMultinacional: Boolean
)