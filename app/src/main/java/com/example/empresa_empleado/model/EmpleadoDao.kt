package com.example.empresa_empleado.model


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpleadoDao {
    @Query("SELECT * FROM empleados WHERE empresaId = :empresaId")
    fun getEmpleadosByEmpresa(empresaId: Int): Flow<List<EmpleadoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpleado(empleado: EmpleadoEntity)

    @Update
    suspend fun updateEmpleado(empleado: EmpleadoEntity)

    @Delete
    suspend fun deleteEmpleado(empleado: EmpleadoEntity)

    @Query("SELECT * FROM empleados WHERE id = :empleadoId LIMIT 1")
    fun getEmpleadoById(empleadoId: String): Flow<EmpleadoEntity>

}
