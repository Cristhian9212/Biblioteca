package com.example.biblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AdminScreen(onOptionClick: (String) -> Unit, onBackClick: () -> Unit) {
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
        Text(
            text = "Opciones de Administrador",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón "Miembros"
        Button(
            onClick = { onOptionClick("Miembros") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF647D96) // Azul grisáceo elegante.
            ),
            elevation = ButtonDefaults.buttonElevation(8.dp) // Sombra suave.
        ) {
            Icon(
                imageVector = Icons.Filled.Person, // Icono de personas para Miembros.
                contentDescription = "Miembros",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Miembros", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón "Libros"
        Button(
            onClick = { onOptionClick("Libros") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF647D96)
            ),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.List, // Icono de libro.
                contentDescription = "Libros",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Libros", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón "Autores"
        Button(
            onClick = { onOptionClick("Autores") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF647D96)
            ),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person, // Icono de persona para Autores.
                contentDescription = "Autores",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Autores", color = Color.White)
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
