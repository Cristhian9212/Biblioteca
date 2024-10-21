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
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Repository.AutorRepository
import kotlinx.coroutines.launch

@Composable
fun ListarAutoresScreen(onBackClick: () -> Unit, autorRepository: AutorRepository) {
    var autores by remember { mutableStateOf(listOf<Autor>()) }
    var editingAutor by remember { mutableStateOf<Autor?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Cargar los autores al iniciar la pantalla
    LaunchedEffect(Unit) {
        autores = autorRepository.getAllAutores()
    }

    // Diálogo de edición
    if (editingAutor != null) {
        EditAutorDialog(
            autor = editingAutor!!,
            onDismiss = { editingAutor = null },
            onUpdate = { updatedAutor ->
                coroutineScope.launch {
                    autorRepository.updateById(
                        updatedAutor.autorId,
                        updatedAutor.nombre,
                        updatedAutor.apellido,
                        updatedAutor.nacionalidad
                    )
                    autores = autorRepository.getAllAutores() // Actualiza la lista después de la edición
                }
                editingAutor = null // Cierra el diálogo después de actualizar
            }
        )
    }

    // Interfaz de Listar Autores
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
            text = "Autores Registrados",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (autores.isNotEmpty()) {
            LazyColumn {
                items(autores) { autor ->
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
                                    text = "${autor.nombre} ${autor.apellido}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Nacionalidad: ${autor.nacionalidad}",
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
                                    editingAutor = autor // Configurar el autor a editar
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Editar",
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = {
                                    // Implementar la lógica para eliminar el autor
                                    coroutineScope.launch {
                                        autorRepository.deleteById(autor.autorId)
                                        autores = autorRepository.getAllAutores() // Actualiza la lista después de eliminar
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
                text = "No hay autores registrados.",
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

// Diálogo para editar autor
@Composable
fun EditAutorDialog(
    autor: Autor,
    onDismiss: () -> Unit,
    onUpdate: (Autor) -> Unit
) {
    var nombre by remember { mutableStateOf(autor.nombre) }
    var apellido by remember { mutableStateOf(autor.apellido) }
    var nacionalidad by remember { mutableStateOf(autor.nacionalidad) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Autor") },
        text = {
            Column {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                TextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") }
                )
                TextField(
                    value = nacionalidad,
                    onValueChange = { nacionalidad = it },
                    label = { Text("Nacionalidad") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(autor.copy(nombre = nombre, apellido = apellido, nacionalidad = nacionalidad))
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
