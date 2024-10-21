package com.example.biblioteca

import PrestamosScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.biblioteca.Database.UserDatabase
import com.example.biblioteca.Repository.AutorRepository
import com.example.biblioteca.Repository.LibroRepository
import com.example.biblioteca.Repository.MiembroRepository
import com.example.biblioteca.Repository.PrestamoRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la base de datos y los repositorios
        val db = UserDatabase.getDatabase(this)
        val miembroRepository = MiembroRepository(db.miembroDao())
        val autorRepository = AutorRepository(db.autorDao(), db.libroDao())
        val libroRepository = LibroRepository(db.libroDao())
        val prestamoRepository = PrestamoRepository(db.prestamoDao()) // Añadir repositorio de préstamos

        setContent {
            BibliotecaApp(
                miembroRepository = miembroRepository,
                autorRepository = autorRepository,
                libroRepository = libroRepository,
                prestamoRepository = prestamoRepository // Pasar el repositorio de préstamos
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibliotecaApp(
    miembroRepository: MiembroRepository,
    autorRepository: AutorRepository,
    libroRepository: LibroRepository,
    prestamoRepository: PrestamoRepository // Añadir repositorio de préstamos
) {
    val currentScreen = remember { mutableStateOf("Login") } // Pantalla inicial

    when (currentScreen.value) {
        "Login" -> LoginScreen(
            onAdminClick = { currentScreen.value = "Admin" },
            onPrestamosClick = { currentScreen.value = "Prestamos" }
        )
        "Admin" -> AdminScreen(
            onOptionClick = { option ->
                currentScreen.value = when (option) {
                    "Miembros" -> "Miembros"
                    "Libros" -> "Libros"
                    "Autores" -> "Autores"
                    else -> currentScreen.value
                }
            },
            onBackClick = { currentScreen.value = "Login" }
        )
        "Miembros" -> MiembrosScreen(
            onOptionClick = { option ->
                currentScreen.value = when (option) {
                    "Agregar Miembro" -> "Registro Miembro"
                    "Listar Miembros" -> "Listar Miembros"
                    else -> currentScreen.value
                }
            },
            onBackClick = { currentScreen.value = "Admin" }
        )
        "Registro Miembro" -> RegistroMiembroScreen(
            onBackClick = { currentScreen.value = "Miembros" },
            miembroRepository = miembroRepository
        )
        "Listar Miembros" -> ListarMiembrosScreen(
            onBackClick = { currentScreen.value = "Miembros" },
            miembroRepository = miembroRepository
        )
        "Autores" -> AutoresScreen(
            onOptionClick = { option ->
                currentScreen.value = when (option) {
                    "Agregar Autor" -> "Registro Autores"
                    "Listar Autores" -> "Listar Autores"
                    else -> currentScreen.value
                }
            },
            onBackClick = { currentScreen.value = "Admin" }
        )
        "Libros" -> LibrosScreen(
            onOptionClick = { option ->
                currentScreen.value = when (option) {
                    "Agregar Libro" -> "Registrar Libro"
                    "Listar Libros" -> "Listar Libros"
                    else -> currentScreen.value
                }
            },
            onBackClick = { currentScreen.value = "Admin" }
        )
        "Registrar Libro" -> RegistrarLibrosScreen(
            onBackClick = { currentScreen.value = "Libros" },
            libroRepository = libroRepository,
            autorRepository = autorRepository
        )
        "Listar Libros" -> ListarLibrosScreen(
            onBackClick = { currentScreen.value = "Libros" },
            libroRepository = libroRepository
        )
        "Listar Autores" -> ListarAutoresScreen(
            onBackClick = { currentScreen.value = "Autores" },
            autorRepository = autorRepository
        )
        "Registro Autores" -> RegistroAutoresScreen(
            onBackClick = { currentScreen.value = "Autores" },
            autorRepository = autorRepository
        )
        "Prestamos" -> PrestamosScreen(
            onBackClick = { currentScreen.value = "Login" },
            prestamoRepository = prestamoRepository,
            libroRepository = libroRepository,
            miembroRepository = miembroRepository,
            onListarClick = { currentScreen.value = "Listar Prestamos" } // Navegar a la lista de préstamos
        )
        "Listar Prestamos" -> ListarPrestamosScreen(
            onBackClick = { currentScreen.value = "Prestamos" },
            prestamoRepository = prestamoRepository, // Pasar el repositorio de préstamos a la pantalla de listar
            libroRepository = libroRepository, // Pasar el repositorio de libros
            miembroRepository = miembroRepository // Pasar el repositorio de miembros
        )
    }
}
