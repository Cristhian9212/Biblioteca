package com.example.biblioteca.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblioteca.Model.Libro

@Dao
interface LibroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(libro: Libro)

    @Query("SELECT * FROM libros")
    suspend fun getAllLibros(): List<Libro>

    @Query("DELETE FROM libros WHERE libroId = :libroId")
    suspend fun deleteById(libroId: Int): Int

    @Query("UPDATE libros SET titulo = :titulo, genero = :genero, autorId = :autorId WHERE libroId = :libroId")
    suspend fun updateById(libroId: Int, titulo: String, genero: String, autorId: Int): Int

    // Método para obtener un libro por ID
    @Query("SELECT * FROM libros WHERE libroId = :libroId LIMIT 1")
    suspend fun getLibroById(libroId: Int): Libro?
}
