package com.example.biblioteca_libro.model

import androidx.room.*

import kotlinx.coroutines.flow.Flow

@Dao
interface BibliotecaDao {
    @Query("SELECT * FROM bibliotecas")
    fun getAllBibliotecas(): Flow<List<BibliotecaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBiblioteca(biblioteca: BibliotecaEntity)

    @Update
    suspend fun updateBiblioteca(biblioteca: BibliotecaEntity)

    @Delete
    suspend fun deleteBiblioteca(biblioteca: BibliotecaEntity)

    @Query("SELECT * FROM bibliotecas WHERE id = :bibliotecaId")
    fun getBibliotecaById(bibliotecaId: Int): Flow<BibliotecaEntity>
}