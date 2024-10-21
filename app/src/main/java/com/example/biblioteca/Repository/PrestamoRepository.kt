package com.example.biblioteca.Repository

import com.example.biblioteca.DAO.PrestamoDao
import com.example.biblioteca.Model.Prestamo
import java.util.Date

class PrestamoRepository(private val prestamoDao: PrestamoDao) {
    suspend fun insert(prestamo: Prestamo) {
        prestamoDao.insert(prestamo)
    }

    suspend fun getAllPrestamos(): List<Prestamo> {
        return prestamoDao.getAllPrestamos()
    }

    suspend fun deleteById(prestamoId: Int): Int {
        return prestamoDao.deleteById(prestamoId)
    }

    suspend fun updateById(prestamoId: Int, libroId: Int, miembroId: Int, fechaPrestamo: String, fechaDevolucion: String?): Int {
        return prestamoDao.updateById(prestamoId, libroId, miembroId, fechaPrestamo, fechaDevolucion)
    }
}