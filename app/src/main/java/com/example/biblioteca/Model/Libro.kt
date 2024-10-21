package com.example.biblioteca.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "libros")
data class Libro(
    @PrimaryKey(autoGenerate = true)
    val libroId: Int = 0,
    val titulo: String,
    val genero: String,
    val autorId: Int // Clave externa para relacionar con autores
)
