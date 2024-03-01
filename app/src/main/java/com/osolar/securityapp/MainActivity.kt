/**
 * MainActivity.kt
 *
 * Este archivo contiene la definición de la actividad principal de la aplicación.
 * Esta actividad maneja la inicialización de la aplicación y la navegación entre las diferentes pantallas.
 *
 * @author Artur Fytsyk
 * Licencia MIT
 */

package com.osolar.securityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.osolar.securityapp.ui.theme.SecurityAppTheme
import com.osolar.securityapp.ventanas.Bienvenida
import com.osolar.securityapp.ventanas.Login
import com.osolar.securityapp.ventanas.Perfil
import com.osolar.securityapp.ventanas.Principal
import com.osolar.securityapp.ventanas.Registro
import com.osolar.securityapp.ventanas.Ubicacion

/**
 * Clase principal de la aplicación. Extiende de ComponentActivity.
 * Carga el navegador de pantallas.
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecurityAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xff41555a))
                {
                    Navigation()
                }
            }
        }
    }

}

/**
 * Funcion principal para navegar entre las distintas pantallas de la aplicacion.
 * Por defecto, entra en la pantalla bienvenida.
 *
 * @param navController Controlador de navegación para gestionar la navegación entre pantallas.
 */

@Preview
@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "bienvenida") {
        composable("bienvenida") { Bienvenida(navController)}
        composable("login") { Login(navController) }
        composable("registro") { Registro(navController) }
        composable("principal") { Principal(navController) }
        composable("perfil") { Perfil(navController) }
        composable("ubicacion") { Ubicacion(navController) }
    }
}



