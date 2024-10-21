package com.example.biblioteca.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.biblioteca.DAO.LibroDao
import com.example.biblioteca.DAO.AutorDao
import com.example.biblioteca.DAO.MiembroDao
import com.example.biblioteca.DAO.PrestamoDao
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Model.Prestamo


@Database(entities = [Libro::class, Autor::class, Miembro::class, Prestamo::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun libroDao(): LibroDao
    abstract fun autorDao(): AutorDao
    abstract fun miembroDao(): MiembroDao
    abstract fun prestamoDao(): PrestamoDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "biblioteca_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}