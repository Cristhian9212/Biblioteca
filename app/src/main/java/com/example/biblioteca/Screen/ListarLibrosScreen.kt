package com.example.biblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Repository.LibroRepository
import kotlinx.coroutines.launch

@Composable
fun ListarLibrosScreen(onBackClick: () -> Unit, libroRepository: LibroRepository) {
    var libros by remember { mutableStateOf(listOf<Libro>()) }
    var editingLibro by remember { mutableStateOf<Libro?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Cargar los libros al iniciar la pantalla
    LaunchedEffect(Unit) {
        libros = libroRepository.getAllLibros()
    }

    // Diálogo de edición
    if (editingLibro != null) {
        EditLibroDialog(
            libro = editingLibro!!,
            onDismiss = { editingLibro = null },
            onUpdate = { updatedLibro ->
                coroutineScope.launch {
                    libroRepository.updateById(
                        updatedLibro.libroId,
                        updatedLibro.titulo,
                        updatedLibro.genero,
                        updatedLibro.autorId
                    )
                    libros = libroRepository.getAllLibros() // Actualiza la lista después de la edición
                }
                editingLibro = null // Cierra el diálogo después de actualizar
            }
        )
    }

    // Interfaz de Listar Libros
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3E4A61),
                        Color(0xFF4B6A88),
                        Color(0xFF2C2F33)
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Libros Registrados",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (libros.isNotEmpty()) {
            LazyColumn {
                items(libros) { libro ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(80.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF647D96))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "${libro.titulo}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Género: ${libro.genero}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            // Agregar iconos para editar y eliminar
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    editingLibro = libro // Configurar el libro a editar
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Editar",
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = {
                                    // Implementar la lógica para eliminar el libro
                                    coroutineScope.launch {
                                        libroRepository.deleteById(libro.libroId)
                                        libros = libroRepository.getAllLibros() // Actualiza la lista después de eliminar
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                text = "No hay libros registrados.",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Botón flotante para volver
    FloatingActionButton(
        onClick = { onBackClick() },
        modifier = Modifier
            .padding(16.dp),
        containerColor = Color(0xFF4B6A88)
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Volver",
            tint = Color.White
        )
    }
}

// Diálogo para editar libro
@Composable
fun EditLibroDialog(
    libro: Libro,
    onDismiss: () -> Unit,
    onUpdate: (Libro) -> Unit
) {
    var titulo by remember { mutableStateOf(libro.titulo) }
    var genero by remember { mutableStateOf(libro.genero) }
    var autorId by remember { mutableStateOf(libro.autorId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Libro") },
        text = {
            Column {
                TextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") }
                )
                TextField(
                    value = genero,
                    onValueChange = { genero = it },
                    label = { Text("Género") }
                )
                TextField(
                    value = autorId.toString(),
                    onValueChange = { autorId = it.toIntOrNull() ?: libro.autorId },
                    label = { Text("ID Autor") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(libro.copy(titulo = titulo, genero = genero, autorId = autorId))
                }
            ) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
