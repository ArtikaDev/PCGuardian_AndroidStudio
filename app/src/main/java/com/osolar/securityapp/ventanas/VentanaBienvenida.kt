/**
 * VentanaBienvenida.kt
 *
 * Archivo que contiene los procedimientos para el mostrado de la pantalla de bienvenida. Incluye el botón de
 * registro y el de inicio de sesión, además de un icono de la aplicación.
 */


package com.osolar.securityapp.ventanas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Funcion que tiene estructurado todos los componentes de la pantalla Bienvenida
 * @param navController Controlador de navegación de la aplicación.
 */

@Composable
fun Bienvenida(navController: NavController){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BotonRegistro(navController)

        Spacer(modifier = Modifier.height(20.dp))

        BarraSerparadora()

        Spacer(modifier = Modifier.height(20.dp))

        BotonInicioSesion(navController)
    }
}

/**
 * Botón que dirige a la pantalla de registro.
 * @param navController Controlador de navegación de la aplicación.
 */

@Composable
fun BotonRegistro(navController: NavController){
    ElevatedButton(
        onClick = {
            navController.navigate("registro")
        },
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff5c92a0)),
        modifier = Modifier
            .height(300.dp)
            .width(300.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Text(
                "Registrarse",
                fontSize = 33.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "registro",
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
        }

    }
}

/**
 * Botón que dirige a la pantalla de inicio de sesión.
 * @param navController Controlador de navegación de la aplicación.
 */

@Composable
fun BotonInicioSesion(navController: NavController){
    ElevatedButton(
        onClick = {
            navController.navigate("login")
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff5c92a0)
        ),
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .height(300.dp)
            .width(300.dp),


    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Text(
                "Iniciar Sesión",
                fontSize = 33.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "inicio de sesion",
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
        }
    }
}

/**
 * Barra separadora personalizada entre los botones para un diseño más organizado
 */

@Composable
fun BarraSerparadora(){
    Row {
        Divider(
            color = Color.White,
            thickness = 1.dp,
            modifier = Modifier
                .padding(
                    vertical = 15.dp,
                    horizontal = 10.dp
                )
                .width(125.dp)
        )

        Text(
            "o",
            color = Color.White,
            fontSize = 20.sp

        )

        Divider(
            color = Color.White,
            thickness = 1.dp,
            modifier = Modifier
                .padding(
                    vertical = 15.dp,
                    horizontal = 10.dp
                )
                .width(125.dp)
        )
    }
}