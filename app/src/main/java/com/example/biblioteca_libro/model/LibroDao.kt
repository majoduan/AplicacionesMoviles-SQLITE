package com.example.biblioteca_libro.model


import androidx.room.*
import com.example.biblioteca_libro.model.LibroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LibroDao {
    @Query("SELECT * FROM libros WHERE bibliotecaId = :bibliotecaId")
    fun getLibrosByBiblioteca(bibliotecaId: Int): Flow<List<LibroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibro(libro: LibroEntity)

    @Update
    suspend fun updateLibro(libro: LibroEntity)

    @Delete
    suspend fun deleteLibro(libro: LibroEntity)

    @Query("SELECT * FROM libros WHERE id = :libroId LIMIT 1")
    fun getLibroById(libroId: String): Flow<LibroEntity>

}
