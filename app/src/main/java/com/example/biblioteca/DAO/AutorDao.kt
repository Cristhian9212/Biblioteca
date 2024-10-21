package com.example.biblioteca.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblioteca.Model.Autor

@Dao
interface AutorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(autor: Autor)

    @Query("SELECT * FROM autores")
    suspend fun getAllAutores(): List<Autor>

    @Query("DELETE FROM autores WHERE autorId = :autorId")
    suspend fun deleteById(autorId: Int): Int

    @Query("UPDATE autores SET nombre = :nombre, apellido = :apellido, nacionalidad = :nacionalidad WHERE autorId = :autorId")
    suspend fun updateById(autorId: Int, nombre: String, apellido: String, nacionalidad: String): Int
}