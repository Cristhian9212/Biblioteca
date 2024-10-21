package com.example.biblioteca.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblioteca.Model.Miembro

@Dao
interface MiembroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(miembro: Miembro)

    @Query("SELECT * FROM miembros")
    suspend fun getAllMiembros(): List<Miembro>

    @Query("DELETE FROM miembros WHERE miembroId = :miembroId")
    suspend fun deleteById(miembroId: Int): Int

    @Query("UPDATE miembros SET nombre = :nombre, apellido = :apellido, fechaInscripcion = :fechaInscripcion WHERE miembroId = :miembroId")
    suspend fun updateById(miembroId: Int, nombre: String, apellido: String, fechaInscripcion: String): Int
}
