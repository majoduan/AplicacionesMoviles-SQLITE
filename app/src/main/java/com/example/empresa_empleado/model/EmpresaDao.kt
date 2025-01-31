package com.example.empresa_empleado.model

import androidx.room.*

import kotlinx.coroutines.flow.Flow

@Dao
interface EmpresaDao {
    @Query("SELECT * FROM bibliotecas")
    fun getAllEmpresas(): Flow<List<EmpresaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpresa(empresa: EmpresaEntity)

    @Update
    suspend fun updateEmpresa(empresa: EmpresaEntity)

    @Delete
    suspend fun deleteEmpresa(empresa: EmpresaEntity)

    @Query("SELECT * FROM bibliotecas WHERE id = :empresaId")
    fun getEmpresaById(empresaId: Int): Flow<EmpresaEntity>
}