package com.example.biblioteca.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "prestamos")
data class Prestamo(
    @PrimaryKey(autoGenerate = true)
    val prestamoId: Int = 0,
    val libroId: Int, // Clave externa para relacionar con libros
    val miembroId: Int, // Clave externa para relacionar con miembros
    val fechaPrestamo: String, // Tipo de dato para la fecha de préstamo
    val fechaDevolucion: String? // Opcional, ya que puede no estar definido al momento del préstamo
)
