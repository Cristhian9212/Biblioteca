package com.example.biblioteca

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Repository.MiembroRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroMiembroScreen(onBackClick: () -> Unit, miembroRepository: MiembroRepository) {
    // Variables para los campos de entrada
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var apellido by remember { mutableStateOf(TextFieldValue("")) }
    var fechaInscripcion by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // Estado para el DatePicker
    var isDatePickerVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Estados para el AlertDialog
    var showAlertDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Fondo degradado
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
                painter = painterResource(id = R.drawable.miembro),
                contentDescription = "Icono",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registro de Miembro",
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

        // Campo para la fecha de inscripción
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = fechaInscripcion,
                onValueChange = { fechaInscripcion = it },
                label = { Text("Fecha de Inscripción", color = Color.Black.copy(alpha = 0.8f)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Botón para seleccionar la fecha
            IconButton(onClick = { isDatePickerVisible = true }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Seleccionar fecha",
                    tint = Color.Gray
                )
            }
        }

        // Muestra el DatePickerDialog si isDatePickerVisible es verdadero
        if (isDatePickerVisible) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    fechaInscripcion = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    isDatePickerVisible = false // Cierra el diálogo después de seleccionar
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

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
                        if (nombre.text.isNotBlank() && apellido.text.isNotBlank() && fechaInscripcion.isNotBlank()) {
                            val miembro = Miembro(nombre = nombre.text, apellido = apellido.text, fechaInscripcion = fechaInscripcion)
                            CoroutineScope(Dispatchers.IO).launch {
                                miembroRepository.insert(miembro)
                                nombre = TextFieldValue("") // Limpiar nombre
                                apellido = TextFieldValue("") // Limpiar apellido
                                fechaInscripcion = "" // Limpiar fecha
                                mensaje = "Registro realizado exitosamente."
                                showSuccessDialog = true // Mostrar diálogo de éxito
                            }
                        } else {
                            showAlertDialog = true // Mostrar diálogo de advertencia
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

    // AlertDialog para advertencia de campos vacíos
    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            title = { Text("Advertencia") },
            text = { Text("Por favor, llena todos los campos.") },
            confirmButton = {
                TextButton(onClick = { showAlertDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Éxito") },
            text = { Text("Registro realizado exitosamente.") },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
