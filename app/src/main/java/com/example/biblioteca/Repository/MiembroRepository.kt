package com.example.biblioteca.Repository

import com.example.biblioteca.DAO.MiembroDao
import com.example.biblioteca.Model.Miembro

class MiembroRepository(private val miembroDao: MiembroDao) {

    // Método para insertar un nuevo miembro
    suspend fun insert(miembro: Miembro) {
        miembroDao.insert(miembro)
    }

    // Método para obtener todos los miembros
    suspend fun getAllMiembros(): List<Miembro> {
        return miembroDao.getAllMiembros()
    }

    // Método para eliminar un miembro por ID
    suspend fun deleteById(miembroId: Int): Int {
        return miembroDao.deleteById(miembroId)
    }

    // Método para actualizar un miembro por ID
    suspend fun updateById(miembroId: Int, nombre: String, apellido: String, fechaInscripcion: String): Int {
        return miembroDao.updateById(miembroId, nombre, apellido, fechaInscripcion)
    }
}
