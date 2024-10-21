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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Repository.MiembroRepository
import kotlinx.coroutines.launch

@Composable
fun ListarMiembrosScreen(onBackClick: () -> Unit, miembroRepository: MiembroRepository) {
    var miembros by remember { mutableStateOf(listOf<Miembro>()) }
    var editingMiembro by remember { mutableStateOf<Miembro?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Cargar los miembros al iniciar la pantalla
    LaunchedEffect(Unit) {
        miembros = miembroRepository.getAllMiembros()
    }

    // Diálogo de edición
    if (editingMiembro != null) {
        EditMiembroDialog(
            miembro = editingMiembro!!,
            onDismiss = { editingMiembro = null },
            onUpdate = { updatedMiembro ->
                coroutineScope.launch {
                    miembroRepository.updateById(
                        updatedMiembro.miembroId,
                        updatedMiembro.nombre,
                        updatedMiembro.apellido,
                        updatedMiembro.fechaInscripcion
                    )
                    miembros = miembroRepository.getAllMiembros() // Actualiza la lista después de la edición
                }
                editingMiembro = null // Cierra el diálogo después de actualizar
            }
        )
    }

    // Interfaz de Listar Miembros
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
            text = "Miembros Registrados",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (miembros.isNotEmpty()) {
            LazyColumn {
                items(miembros) { miembro ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp) // Puedes ajustar este valor si necesitas más o menos espacio
                            .height(80.dp), // Establecer un alto fijo para las tarjetas
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF647D96))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween // Para espaciar elementos
                        ) {
                            Column(
                                modifier = Modifier.weight(1f) // Para que la columna ocupe el espacio disponible
                            ) {
                                Text(
                                    text = "${miembro.nombre} ${miembro.apellido}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Fecha de Inscripción: ${miembro.fechaInscripcion}",
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
                                    editingMiembro = miembro // Configurar el miembro a editar
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Editar",
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = {
                                    // Implementar la lógica para eliminar el miembro
                                    coroutineScope.launch {
                                        miembroRepository.deleteById(miembro.miembroId)
                                        miembros = miembroRepository.getAllMiembros() // Actualiza la lista después de eliminar
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
                text = "No hay miembros registrados.",
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
           // .align(Alignment.BottomEnd)
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



// Diálogo para editar miembro
@Composable
fun EditMiembroDialog(
    miembro: Miembro,
    onDismiss: () -> Unit,
    onUpdate: (Miembro) -> Unit
) {
    var nombre by remember { mutableStateOf(miembro.nombre) }
    var apellido by remember { mutableStateOf(miembro.apellido) }
    var fechaInscripcion by remember { mutableStateOf(miembro.fechaInscripcion) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Miembro") },
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
                    value = fechaInscripcion,
                    onValueChange = { fechaInscripcion = it },
                    label = { Text("Fecha de Inscripción") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(miembro.copy(nombre = nombre, apellido = apellido, fechaInscripcion = fechaInscripcion))
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
