/**
 * VentanaPerfil.kt
 *
 * Archivo que contiene funciones para el mostrado de la Pantalla de perfil.
 */

package com.osolar.securityapp.ventanas

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.osolar.securityapp.firebass.ScreenViewModel
import com.osolar.securityapp.firebass.User
import java.util.UUID

/**
 * Pantalla de perfil de usuario que contiene datos personales de este asi como una imagen de perfil.
 * @param navController Controlador de navegación de la aplicación.
 * @param dataViewModel ViewModel para la pantalla de perfil.
 */

@Composable
fun Perfil(navController: NavController, dataViewModel: ScreenViewModel = viewModel()){

    val userName = dataViewModel.userState

    Column (
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top)
    {
        BotonIconoVolverPrincipal(navController = navController)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImagePicker(userName)
    }
}

/**
 * Botón de regreso a la pantalla principal.
 * @param navController Controlador de navegación de la aplicación.
 */

@Composable
fun BotonIconoVolverPrincipal(navController: NavController){
    IconButton(
        onClick = { navController.navigate("principal") }
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
                    top = 10.dp
                )
        )
    }
}

/**
 * Selector de imagen de perfil.
 * @param userName Estado mutable del nombre de usuario.
 */

@Composable
fun ImagePicker(userName: MutableState<User>) {
    val context = LocalContext.current
    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    var imageUri by remember { mutableStateOf(user?.photoUrl) }

    val updateUI = {
        user?.reload()?.addOnSuccessListener {
            imageUri = user!!.photoUrl // Actualiza la URI de la imagen para reflejar el cambio
            Toast.makeText(context, "Imagen actualizada con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
                uploadImageToFirebaseStorage(uri, context, updateUI)
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(text = "Tú perfil", color = Color.White, fontSize = 50.sp)
        Divider(Modifier.padding(bottom = 20.dp))
        Box(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xff5c92a0))
            .padding(all = 20.dp)
        ){
            Text(text = "Nombre: ${userName.value.name}", color = Color.White)
        }
        Box(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xff5c92a0))
            .padding(all = 20.dp)
        ) {
            Text(text = "Teléfono: ${userName.value.phone}", color = Color.White)
        }
        Box(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xff5c92a0))
            .padding(all = 20.dp)
        ) {
            user!!.email?.let { Text(text = "Email: $it", color = Color.White) }
        }
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp).border(2.dp, Color.White, RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Button(
            // Abre el explorador de archivos en la galeria para solo mostrar archivos que sean "image"
            onClick = {
                launcher.launch(arrayOf("image/*"))
            },
            colors = ButtonDefaults.buttonColors(Color(0xff5c92a0))
        ) {
            Text(text = "Cambiar")
        }
    }
}

/**
 * Sube la imagen seleccionada a Firebase Storage.
 * @param imageUri URI de la imagen seleccionada.
 * @param context Contexto de la aplicación.
 * @param updateUI Callback para actualizar la interfaz de usuario.
 */

fun uploadImageToFirebaseStorage(imageUri: Uri, context: Context, updateUI: () -> Unit) {
    val filename = UUID.randomUUID().toString()
    val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

    ref.putFile(imageUri)
        .addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                updateUserProfile(imageUrl, context, updateUI)
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Error al subir imagen: ${it.message}", Toast.LENGTH_SHORT).show()
        }
}

/**
 * Actualiza el perfil de usuario con la nueva imagen.
 * @param imageUrl URL de la imagen en Firebase Storage.
 * @param context Contexto de la aplicación.
 * @param updateUI Callback para actualizar la interfaz de usuario.
 */

fun updateUserProfile(imageUrl: String, context: Context, updateUI: () -> Unit) {
    val user = Firebase.auth.currentUser
    val profileUpdates = userProfileChangeRequest { photoUri = Uri.parse(imageUrl) }

    user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d("ProfileUpdate", "Perfil del usuario actualizado.")
            updateUI() // Llama al callback para actualizar la UI
        } else {
            Log.d("ProfileUpdate", "No se ha podido actualizar.")
            Toast.makeText(context, "Error al actualizar perfil", Toast.LENGTH_SHORT).show()
        }
    }
}

