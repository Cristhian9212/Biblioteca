package com.example.biblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Repository.LibroRepository
import com.example.biblioteca.Repository.AutorRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarLibrosScreen(onBackClick: () -> Unit, libroRepository: LibroRepository, autorRepository: AutorRepository) {
    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var selectedAutor by remember { mutableStateOf<Autor?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var autores by remember { mutableStateOf(listOf<Autor>()) }
    var showPopup by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo de error
    var showSuccessDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo de éxito
    val coroutineScope = rememberCoroutineScope()

    // Función para cargar autores desde el repositorio
    fun loadAutores() {
        coroutineScope.launch {
            autores = autorRepository.getAllAutores()
        }
    }

    // Cargar autores al iniciar la pantalla
    LaunchedEffect(Unit) {
        loadAutores()
    }

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ícono redondo grande sobre el título
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White.copy(alpha = 0.5f), shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.libro),
                contentDescription = "Icono",
                tint = Color.Black,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registrar Nuevo Libro",
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para el título del libro
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título del Libro", color = Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para el género del libro
        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text("Género del Libro", color = Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para mostrar el autor seleccionado
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = selectedAutor?.nombre ?: "Selecciona un autor",
                onValueChange = { /* No se permite editar directamente */ },
                label = { Text("Autor", color = Color.Black) },
                modifier = Modifier.weight(1f),
                readOnly = true
            )
            // Icono para abrir el popup
            Icon(
                painter = painterResource(id = R.drawable.autor),
                contentDescription = "Seleccionar Autor",
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 8.dp)
                    .clickable { showPopup = true }
            )
        }

        // Popup para seleccionar un autor
        if (showPopup) {
            AutorSelectionPopup(
                autores = autores,
                onAutorSelected = { autor ->
                    selectedAutor = autor
                    showPopup = false
                },
                onDismissRequest = { showPopup = false }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Fila para los botones "Registrar" y "Volver"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón "Registrar"
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = {
                        if (titulo.isBlank() || genero.isBlank() || selectedAutor == null) {
                            errorMessage = "Por favor completa todos los campos."
                            showErrorDialog = true // Mostrar diálogo de error
                            successMessage = "" // Limpiar mensaje de éxito
                        } else {
                            errorMessage = ""
                            coroutineScope.launch {
                                val libro = Libro(titulo = titulo, genero = genero, autorId = selectedAutor!!.autorId)
                                libroRepository.insert(libro)
                                // Limpiar los campos
                                titulo = ""
                                genero = ""
                                selectedAutor = null
                                // Mostrar mensaje de éxito
                                successMessage = "Libro almacenado con éxito."
                                showSuccessDialog = true // Mostrar diálogo de éxito
                            }
                        }
                    },
                    modifier = Modifier.size(50.dp) // Tamaño del ícono
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Registrar",
                        tint = Color.White
                    )
                }
                Text("Registrar", color = Color.White)
            }

            // Botón "Volver"
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier.size(50.dp) // Tamaño del ícono
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Text("Volver", color = Color.White)
            }
        }
    }

    // Diálogo de error
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Advertencia") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Éxito") },
            text = { Text(successMessage) },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
fun AutorSelectionPopup(autores: List<Autor>, onAutorSelected: (Autor) -> Unit, onDismissRequest: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column {
                Text(
                    text = "Seleccionar Autor",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn {
                    items(autores) { autor ->
                        Text(
                            text = "${autor.nombre} ${autor.apellido}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAutorSelected(autor)
                                }
                                .padding(8.dp)
                        )
                    }
                }
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.End).padding(16.dp)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}