/**
 * VentanaRegistro.kt
 *
 * Archivo que contiene funciones para el mostrado de la Pantalla de Registro, registra un usuario en Firebase.
 */

package com.osolar.securityapp.ventanas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.osolar.securityapp.R
import com.osolar.securityapp.firebass.LoginScreenViewModel

/**
 * Funcion que contiene estructuradas las funciones de la pantalla de registro
 *
 * @param navController El controlador de navegación para gestionar la navegación entre pantallas.
 */

@Composable
fun Registro(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.Start,
    ) {
        BotonIconoVolver(navController)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostrar el logotipo de la aplicación
        Logo()
        TextFieldsRegistro(navController)
    }
}

/**
 * Boton para volver a la pantalla de bienvenida
 *
 * @param navController El controlador de navegación para gestionar la navegación entre pantallas.
 */

@Composable
fun BotonIconoVolver(navController: NavController){
    IconButton(
        onClick = { navController.navigate("bienvenida") }
    ) {
        Icon(
            Icons.Default.ArrowBack,
            contentDescription = "volver",
            tint = Color.White,
            modifier = Modifier
                .width(75.dp)
                .height(75.dp)
                .padding(
                    start = 10.dp,
                    top = 10.dp)

        )
    }
}

/**
 * Funcion para el mostrado adecuado del logotipo
 */

@Composable
fun Logo(){
    Image(
        painter = painterResource(id = R.drawable.pc_guardian_logo),
        contentDescription = "Logo",
        modifier = Modifier
            .padding(bottom = 40.dp)
            .size(150.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
    )
}

/**
 * Muestra los campos necesarios del formulario para el registro del usuario
 *
 * @param navController El controlador de navegación para gestionar la navegación entre pantallas.
 * @param viewModel El ViewModel para manejar la lógica de inicio de sesión.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldsRegistro(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }

    var show by rememberSaveable { mutableStateOf(false) }
    val errorState = viewModel.errorState.value

    if (errorState != null) {
        AlertDialogRegistro(show,
            onDismissRequest = { show = false },
            onConfirmation = { show = false },
            dialogTitle = "Error",
            dialogText = "Algun campo esta incorrecto"
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
        value = email,
        onValueChange = { email = it },
        label = {
            Text(
                "Email",
                color = Color.White
            ) },
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
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
        modifier = Modifier
            .widthIn(max = 275.dp)
    )

    Spacer(modifier = Modifier.height(20.dp))

    TextField(
        value = telefono,
        onValueChange = { telefono = it },
        label = {
            Text(
                "Teléfono",
                color = Color.White
            ) },
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Call,
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

    TextField(
        value = pass2,
        onValueChange = { pass2 = it },
        label = { Text("Repite Contraseña",
            color = Color.White) },
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Check,
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

    ElevatedButton(
        onClick = {
            viewModel.createUserWithEmailAndPassword(email, pass, telefono){ success ->
                if (success) {
                    navController.navigate("principal")
                } else {
                    show = true
                    navController.navigate("registro")
                }
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff5c92a0))
    ) {
        Text("Registrarse")
    }
}

/**
 * Muestra una alerta si ha ocurrido un error durante el registro del usuario
 *
 * @param show Indica si el diálogo de alerta debe mostrarse o no.
 * @param onDismissRequest Función de callback para cerrar el diálogo de alerta.
 * @param onConfirmation Función de callback para confirmar el diálogo de alerta.
 * @param dialogTitle El título del diálogo de alerta.
 * @param dialogText El texto del diálogo de alerta.
 */

@Composable
fun AlertDialogRegistro(show:Boolean,
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