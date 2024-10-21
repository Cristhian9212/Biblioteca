package com.example.biblioteca.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "miembros")
data class Miembro(
    @PrimaryKey(autoGenerate = true)
    val miembroId: Int = 0,
    val nombre: String,
    val apellido: String,
    val fechaInscripcion: String // Puedes usar un tipo de dato diferente para fechas
)
