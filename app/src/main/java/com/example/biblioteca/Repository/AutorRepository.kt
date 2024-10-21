package com.example.biblioteca.Repository

import com.example.biblioteca.DAO.AutorDao
import com.example.biblioteca.DAO.LibroDao // Asegúrate de que este DAO exista
import com.example.biblioteca.Model.Autor

class AutorRepository(private val autorDao: AutorDao, private val libroDao: LibroDao) {
    suspend fun insert(autor: Autor) {
        autorDao.insert(autor)
    }

    suspend fun getAllAutores(): List<Autor> {
        return autorDao.getAllAutores()
    }

    suspend fun deleteById(autorId: Int): Int {
        return autorDao.deleteById(autorId)
    }

    suspend fun updateById(autorId: Int, nombre: String, apellido: String, nacionalidad: String): Int {
        return autorDao.updateById(autorId, nombre, apellido, nacionalidad)
    }


}
