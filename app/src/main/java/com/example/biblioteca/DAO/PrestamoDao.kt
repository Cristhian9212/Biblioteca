package com.example.biblioteca.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblioteca.Model.Prestamo
import java.util.Date

@Dao
interface PrestamoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prestamo: Prestamo)

    @Query("SELECT * FROM prestamos")
    suspend fun getAllPrestamos(): List<Prestamo>

    @Query("DELETE FROM prestamos WHERE prestamoId = :prestamoId")
    suspend fun deleteById(prestamoId: Int): Int

    @Query("UPDATE prestamos SET libroId = :libroId, miembroId = :miembroId, fechaPrestamo = :fechaPrestamo, fechaDevolucion = :fechaDevolucion WHERE prestamoId = :prestamoId")
    suspend fun updateById(prestamoId: Int, libroId: Int, miembroId: Int, fechaPrestamo: String, fechaDevolucion: String?): Int
}
