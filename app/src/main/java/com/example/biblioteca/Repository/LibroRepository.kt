package com.example.biblioteca.Repository

import com.example.biblioteca.DAO.LibroDao
import com.example.biblioteca.Model.Libro

class LibroRepository(private val libroDao: LibroDao) {
    suspend fun insert(libro: Libro) {
        libroDao.insert(libro)
    }

    suspend fun getAllLibros(): List<Libro> {
        return libroDao.getAllLibros()
    }

    suspend fun deleteById(libroId: Int): Int {
        return libroDao.deleteById(libroId)
    }

    suspend fun updateById(libroId: Int, titulo: String, genero: String, autorId: Int): Int {
        return libroDao.updateById(libroId, titulo, genero, autorId)
    }

    // Nuevo método para obtener un libro por ID
    suspend fun getLibroById(libroId: Int): Libro? {
        return libroDao.getLibroById(libroId)
    }
}