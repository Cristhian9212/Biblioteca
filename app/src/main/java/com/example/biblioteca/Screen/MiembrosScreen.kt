package com.example.biblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MiembrosScreen(onOptionClick: (String) -> Unit, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3E4A61), // Azul grisáceo suave.
                        Color(0xFF4B6A88), // Azul profundo.
                        Color(0xFF2C2F33)  // Gris oscuro.
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícono grande sobre el título
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFF647D96), CircleShape), // Fondo circular
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person, // Ícono grande
                contentDescription = "Agregar",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Opciones de Miembros",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Fila para los botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Espacio entre botones
        ) {
            // Botón "Agregar Miembro"
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { onOptionClick("Agregar Miembro") },
                    modifier = Modifier
                        .size(100.dp) // Tamaño fijo para el botón
                        .padding(16.dp), // Espaciado alrededor del botón
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF647D96) // Azul grisáceo elegante.
                    ),
                    shape = MaterialTheme.shapes.small // Forma redondeada
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add, // Icono de agregar.
                        contentDescription = "Agregar Miembro",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
                Text(
                    text = "Agregar",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botón "Listar Miembros"
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { onOptionClick("Listar Miembros") },
                    modifier = Modifier
                        .size(100.dp) // Tamaño fijo para el botón
                        .padding(16.dp), // Espaciado alrededor del botón
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF647D96) // Azul grisáceo elegante.
                    ),
                    shape = MaterialTheme.shapes.small // Forma redondeada
                ) {
                    Icon(
                        imageVector = Icons.Filled.List, // Icono de listar.
                        contentDescription = "Listar Miembros",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
                Text(
                    text = "Listar",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para volver con ícono
        Button(
            onClick = { onBackClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4B6A88) // Azul más oscuro para destacar.
            ),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, // Icono de flecha para volver.
                contentDescription = "Volver",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Volver", color = Color.White)
        }
    }
}
