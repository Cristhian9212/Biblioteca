package com.example.biblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Repository.AutorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroAutoresScreen(onBackClick: () -> Unit, autorRepository: AutorRepository) {
    // Variables para los campos de entrada
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var apellido by remember { mutableStateOf(TextFieldValue("")) }
    var nacionalidad by remember { mutableStateOf(TextFieldValue("")) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // Fondo degradado unificado
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3E4A61), // Color superior
                        Color(0xFF4B6A88), // Color medio
                        Color(0xFF2C2F33)  // Color inferior
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícono redondo grande sobre el título
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White.copy(alpha = 0.5f), shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(90.dp), // Tamaño del ícono
                painter = painterResource(id = R.drawable.autor),
                contentDescription = "Icono",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registro de Autor",
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el nombre
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre", color = Color.Black.copy(alpha = 0.8f)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para el apellido
        TextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido", color = Color.Black.copy(alpha = 0.8f)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para la nacionalidad
        TextField(
            value = nacionalidad,
            onValueChange = { nacionalidad = it },
            label = { Text("Nacionalidad", color = Color.Black.copy(alpha = 0.8f)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fila para los botones "Guardar" y "Volver"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly, // Espacio entre botones
            verticalAlignment = Alignment.CenterVertically // Alinear verticalmente los botones
        ) {
            // Botón "Guardar" como ícono
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = {
                        if (nombre.text.isNotBlank() && apellido.text.isNotBlank() && nacionalidad.text.isNotBlank()) {
                            val autor = Autor(nombre = nombre.text, apellido = apellido.text, nacionalidad = nacionalidad.text)
                            CoroutineScope(Dispatchers.IO).launch {
                                autorRepository.insert(autor)
                                // Limpiar campos solo si el registro fue exitoso
                                dialogMessage = "Autor agregado exitosamente."
                                showDialog = true
                                nombre = TextFieldValue("") // Limpiar el campo de nombre
                                apellido = TextFieldValue("") // Limpiar el campo de apellido
                                nacionalidad = TextFieldValue("") // Limpiar el campo de nacionalidad
                            }
                        } else {
                            dialogMessage = "Por favor, llena todos los campos."
                            showDialog = true
                        }
                    },
                    modifier = Modifier.size(50.dp) // Tamaño del ícono
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done, // Icono de guardar
                        contentDescription = "Guardar",
                        tint = Color.White
                    )
                }
                Text("Guardar", color = Color.White) // Etiqueta debajo del ícono
            }

            // Botón "Volver" como ícono
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(50.dp) // Tamaño del ícono
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, // Icono de flecha para volver
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Text("Volver", color = Color.White) // Etiqueta debajo del ícono
            }
        }
    }

    // Diálogo para mostrar mensajes
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Información") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
