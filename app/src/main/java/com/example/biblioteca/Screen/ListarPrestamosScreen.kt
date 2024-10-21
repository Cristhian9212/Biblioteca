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
import com.example.biblioteca.Model.Prestamo
import com.example.biblioteca.Repository.LibroRepository
import com.example.biblioteca.Repository.MiembroRepository
import com.example.biblioteca.Repository.PrestamoRepository
import kotlinx.coroutines.launch

@Composable
fun ListarPrestamosScreen(
    onBackClick: () -> Unit,
    prestamoRepository: PrestamoRepository,
    libroRepository: LibroRepository,
    miembroRepository: MiembroRepository
) {
    var prestamos by remember { mutableStateOf(listOf<Prestamo>()) }
    var editingPrestamo by remember { mutableStateOf<Prestamo?>(null) }
    var librosMap by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var miembrosMap by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    val coroutineScope = rememberCoroutineScope()

    // Cargar los préstamos al iniciar la pantalla
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            prestamos = prestamoRepository.getAllPrestamos()

            // Cargar libros y miembros en mapas
            librosMap = libroRepository.getAllLibros().associate { it.libroId to it.titulo }
            miembrosMap = miembroRepository.getAllMiembros().associate { it.miembroId to "${it.nombre} ${it.apellido}" }
        }
    }

    // Diálogo de edición
    if (editingPrestamo != null) {
        EditPrestamoDialog(
            prestamo = editingPrestamo!!,
            onDismiss = { editingPrestamo = null },
            onUpdate = { updatedPrestamo ->
                coroutineScope.launch {
                    prestamoRepository.updateById(
                        updatedPrestamo.prestamoId,
                        updatedPrestamo.libroId,
                        updatedPrestamo.miembroId,
                        updatedPrestamo.fechaPrestamo,
                        updatedPrestamo.fechaDevolucion
                    )
                    prestamos = prestamoRepository.getAllPrestamos() // Actualiza la lista después de la edición
                }
                editingPrestamo = null // Cierra el diálogo después de actualizar
            }
        )
    }

    // Interfaz de Listar Préstamos
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
            text = "Préstamos Registrados",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (prestamos.isNotEmpty()) {
            // LazyColumn para permitir el desplazamiento
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(prestamos) { prestamo ->
                    val libroTitulo = librosMap[prestamo.libroId] ?: "Título no disponible"
                    val miembroNombre = miembrosMap[prestamo.miembroId] ?: "Miembro no disponible"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(105.dp),
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
                                    text = "Título: $libroTitulo",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Miembro: $miembroNombre",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Fecha de Préstamo: ${prestamo.fechaPrestamo}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Fecha de Devolución: ${prestamo.fechaDevolucion ?: "No devuelto"}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    editingPrestamo = prestamo // Configurar el préstamo a editar
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Editar",
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        prestamoRepository.deleteById(prestamo.prestamoId)
                                        prestamos = prestamoRepository.getAllPrestamos() // Actualiza la lista después de eliminar
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
                text = "No hay préstamos registrados.",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

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

// Diálogo para editar préstamo
@Composable
fun EditPrestamoDialog(
    prestamo: Prestamo,
    onDismiss: () -> Unit,
    onUpdate: (Prestamo) -> Unit
) {
    var libroId by remember { mutableStateOf(prestamo.libroId.toString()) }
    var miembroId by remember { mutableStateOf(prestamo.miembroId.toString()) }
    var fechaPrestamo by remember { mutableStateOf(prestamo.fechaPrestamo) }
    var fechaDevolucion by remember { mutableStateOf(prestamo.fechaDevolucion ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Préstamo") },
        text = {
            Column {
                TextField(
                    value = libroId,
                    onValueChange = { libroId = it },
                    label = { Text("ID Libro") }
                )
                TextField(
                    value = miembroId,
                    onValueChange = { miembroId = it },
                    label = { Text("ID Miembro") }
                )
                TextField(
                    value = fechaPrestamo,
                    onValueChange = { fechaPrestamo = it },
                    label = { Text("Fecha de Préstamo") }
                )
                TextField(
                    value = fechaDevolucion,
                    onValueChange = { fechaDevolucion = it },
                    label = { Text("Fecha de Devolución") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(prestamo.copy(
                        libroId = libroId.toInt(),
                        miembroId = miembroId.toInt(),
                        fechaPrestamo = fechaPrestamo,
                        fechaDevolucion = fechaDevolucion.ifBlank { null }
                    ))
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
