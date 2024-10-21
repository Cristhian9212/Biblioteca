import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Model.Prestamo
import com.example.biblioteca.R
import com.example.biblioteca.Repository.LibroRepository
import com.example.biblioteca.Repository.MiembroRepository
import com.example.biblioteca.Repository.PrestamoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun PrestamosScreen(
    onBackClick: () -> Unit,
    onListarClick: () -> Unit,
    prestamoRepository: PrestamoRepository,
    libroRepository: LibroRepository,
    miembroRepository: MiembroRepository
) {
    var libroTitulo by remember { mutableStateOf("") }
    var miembroNombre by remember { mutableStateOf("") }
    var fechaPrestamo by remember { mutableStateOf("") }
    var fechaDevolucion by remember { mutableStateOf("") }
    var showPopupLibro by remember { mutableStateOf(false) }
    var showPopupMiembro by remember { mutableStateOf(false) }

    // Para manejar el diálogo de alerta
    var showAlertDialog by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    // Para manejar el mensaje de registro exitoso
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Variables para almacenar listas de libros y miembros
    var libros by remember { mutableStateOf(listOf<Libro>()) }
    var miembros by remember { mutableStateOf(listOf<Miembro>()) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Cargar libros y miembros al iniciar la pantalla
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            libros = libroRepository.getAllLibros()
            miembros = miembroRepository.getAllMiembros()
        }
    }

    // Diálogo de alerta
    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            title = { Text("Advertencia") },
            text = { Text(alertMessage) },
            confirmButton = {
                TextButton(onClick = { showAlertDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    // Mensaje de registro exitoso
    if (showSuccessMessage) {
        Toast.makeText(context, "Registro realizado exitosamente", Toast.LENGTH_SHORT).show()
        showSuccessMessage = false
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.prestamo),
            contentDescription = "Imagen del título",
            modifier = Modifier.size(100.dp)
        )

        Text("Gestión de Préstamos", style = MaterialTheme.typography.titleLarge, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para título del Libro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = libroTitulo,
                onValueChange = { libroTitulo = it },
                label = { Text("Selecciona el libro") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text) // Cambiar a texto
            )
            IconButton(onClick = { showPopupLibro = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.libro),
                    contentDescription = "Seleccionar libro",
                    tint = Color.Black
                )
            }
        }

        // Popup para seleccionar un libro
        if (showPopupLibro) {
            LibroSelectionPopup(
                libros = libros,
                onLibroSelected = { selectedLibro ->
                    libroTitulo = selectedLibro.titulo // Asigna el título del libro seleccionado
                    showPopupLibro = false
                },
                onDismissRequest = { showPopupLibro = false }
            )
        }

        // Campo para nombre del Miembro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = miembroNombre,
                onValueChange = { miembroNombre = it },
                label = { Text("Selecciona el miembro") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text) // Cambiar a texto
            )
            IconButton(onClick = { showPopupMiembro = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.miembro),
                    contentDescription = "Seleccionar miembro",
                    tint = Color.Black
                )
            }
        }

        // Popup para seleccionar un miembro
        if (showPopupMiembro) {
            MiembroSelectionPopup(
                miembros = miembros,
                onMiembroSelected = { selectedMiembro ->
                    miembroNombre = "${selectedMiembro.nombre} ${selectedMiembro.apellido}"
                    showPopupMiembro = false
                },
                onDismissRequest = { showPopupMiembro = false }
            )
        }

        // Campo para fecha de préstamo
        TextField(
            value = fechaPrestamo,
            onValueChange = { fechaPrestamo = it },
            label = { Text("Fecha de Préstamo") },
            trailingIcon = {
                IconButton(onClick = {
                    val calendar = Calendar.getInstance()
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            fechaPrestamo = "$dayOfMonth/${month + 1}/$year"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.show()
                }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Fecha")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para fecha de devolución
        TextField(
            value = fechaDevolucion,
            onValueChange = { fechaDevolucion = it },
            label = { Text("Fecha de Devolución") },
            trailingIcon = {
                IconButton(onClick = {
                    val calendar = Calendar.getInstance()
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            fechaDevolucion = "$dayOfMonth/${month + 1}/$year"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.show()
                }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Fecha")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones en fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Para espaciar los botones
        ) {
            // Botón para registrar préstamo
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    if (libroTitulo.isBlank() || miembroNombre.isBlank() || fechaPrestamo.isBlank() || fechaDevolucion.isBlank()) {
                        // Mostrar el mensaje de alerta
                        alertMessage = "Todos los campos deben ser llenados."
                        showAlertDialog = true
                    } else {
                        coroutineScope.launch(Dispatchers.IO) {
                            val prestamo = Prestamo(
                                libroId = libros.first { it.titulo == libroTitulo }.libroId, // Obtener el ID del libro por título
                                miembroId = miembros.first { "${it.nombre} ${it.apellido}" == miembroNombre }.miembroId, // Obtener el ID del miembro por nombre
                                fechaPrestamo = fechaPrestamo,
                                fechaDevolucion = fechaDevolucion
                            )
                            prestamoRepository.insert(prestamo)

                            // Limpiar los campos
                            libroTitulo = ""
                            miembroNombre = ""
                            fechaPrestamo = ""
                            fechaDevolucion = ""

                            // Mostrar mensaje de éxito
                            showSuccessMessage = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Guardar",
                        tint = Color.White
                    )
                }
                Text("Guardar", color = Color.White)
            }

            // Botón para listar préstamos
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onListarClick) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = "Listar Préstamos",
                        tint = Color.White
                    )
                }
                Text("Listar", color = Color.White)
            }

            // Botón para regresar
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }
                Text("Volver", color = Color.White)
            }
        }
    }
}

@Composable
fun LibroSelectionPopup(libros: List<Libro>, onLibroSelected: (Libro) -> Unit, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Seleccionar Libro") },
        text = {
            Column {
                libros.forEach { libro ->
                    TextButton(onClick = { onLibroSelected(libro) }) {
                        Text(libro.titulo)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun MiembroSelectionPopup(miembros: List<Miembro>, onMiembroSelected: (Miembro) -> Unit, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Seleccionar Miembro") },
        text = {
            Column {
                miembros.forEach { miembro ->
                    TextButton(onClick = { onMiembroSelected(miembro) }) {
                        Text("${miembro.nombre} ${miembro.apellido}")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cerrar")
            }
        }
    )
}
