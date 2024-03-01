package com.osolar.securityapp.ventanas

/**
 * VentanaLogin.kt
 *
 * Archivo que contiene funciones para el mostrado de la Pantalla de inicio de sesión.
 */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.osolar.securityapp.firebass.LoginScreenViewModel

/**
 * Funcion que tiene estructurado todos los componentes de la pantalla de Login
 * @param navController Controlador de navegación de la aplicación.
 */

@Composable
fun Login(
    navController: NavController
){
    Column(
        horizontalAlignment = Alignment.Start,
    ) {
        BotonIconoVolver(navController)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Logo()
        TextFieldsLogin(navController)
    }
}

/**
 * Campos de texto para el inicio de sesión.
 * @param navController Controlador de navegación de la aplicación.
 * @param viewModel ViewModel para la pantalla de inicio de sesión.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldsLogin(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    var usuario by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    var show by rememberSaveable { mutableStateOf(false) }
    val errorState = viewModel.errorState.value

    if (errorState != null) {
        AlertDialogLogin(show,
            onDismissRequest = { show = false },
            onConfirmation = { show = false },
            dialogTitle = "Error",
            dialogText = "La contraseña o email son incorrectos"
        )
    }

    TextField(
        value = usuario,
        onValueChange = { usuario = it },
        label = {
            Text(
                "Usuario",
                color = Color.White
            ) },
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "user",
                tint = Color.White
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xff5c92a0),
            focusedIndicatorColor = Color.White,
            cursorColor = Color.White,
            unfocusedIndicatorColor = Color.White
        ),
        modifier = Modifier
            .widthIn(max = 275.dp)
    )

    Spacer(modifier = Modifier.height(20.dp))

    TextField(
        value = pass,
        onValueChange = { pass = it },
        label = { Text("Contraseña",
            color = Color.White) },
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "email",
                tint = Color.White
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xff5c92a0),
            focusedIndicatorColor = Color.White,
            cursorColor = Color.White,
            unfocusedIndicatorColor = Color.White
        ),
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .widthIn(max = 275.dp)
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Se comprueba si el inicio de sesion fue exitoso y sino se muestra un alert dialog
    ElevatedButton(
        onClick = {
            viewModel.signInWithEmailAndPass(usuario, pass) { success ->
                if (success) {
                    navController.navigate("principal")
                } else {
                    show = true
                }
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff5c92a0))
    ) {
        Text("Iniciar Sesión")
    }

}

/**
 * Cuadro de diálogo para mostrar un error de inicio de sesión.
 * @param show Estado de visualización del diálogo.
 * @param onDismissRequest Función para cerrar el diálogo.
 * @param onConfirmation Función de confirmación.
 * @param dialogTitle Título del diálogo.
 * @param dialogText Texto del diálogo.
 */

@Composable
fun AlertDialogLogin(show:Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    if (show){
        AlertDialog(
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirm")
                }
            }
        )
    }
}