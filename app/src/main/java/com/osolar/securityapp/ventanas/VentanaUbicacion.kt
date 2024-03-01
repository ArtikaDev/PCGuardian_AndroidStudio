/**
 * VentanaUbicacion.kt
 *
 * Archivo que contiene funciones para el mostrado de la Pantalla de Ubicacion, muestra un mapa
 * del servicio de GoogleMaps API para que el usuario pueda elegir su ubicacion.
 */

package com.osolar.securityapp.ventanas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Contiene un componente view para el mostrado del mapa y un campo que rescata la ubicacion.
 *
 * @param navController El controlador de navegación para gestionar la navegación entre pantallas.
 * @param ubicacionSeleccionadaTexto El texto que representa la ubicación seleccionada por el usuario.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ubicacion(navController: NavController){
    var ubicacionSeleccionadaTexto by remember { mutableStateOf("") }
    val context = LocalContext.current
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Solicitar permisos de ubicación
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permiso concedido
            } else {
                // Permiso denegado
            }
        }
    )


    LaunchedEffect(key1 = true) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // Tenemos permiso, no hacemos nada aquí por ahora.
            }
            else -> {
                // No tenemos permiso, entonces lo solicitamos
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column (
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top)
    {
        BotonIconoVolverPrincipal(navController = navController)

        TextField(
            value = ubicacionSeleccionadaTexto,
            onValueChange = { ubicacionSeleccionadaTexto = it },
            label = { Text("Ubicación Seleccionada") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        // Muestra el mapa segun la ubicación del usuario con un zoom indicado
        AndroidView(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            factory = { context ->
                MapView(context).apply {
                    onCreate(Bundle())
                    getMapAsync { googleMap ->
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    val userLatLng = LatLng(location.latitude, location.longitude)
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12f))
                                    googleMap.addMarker(MarkerOptions().position(userLatLng))
                                    ubicacionSeleccionadaTexto = "${location.latitude}, ${location.longitude}"
                                }
                            }
                        }
                        googleMap.setOnMapClickListener { latLng ->
                            ubicacionSeleccionadaTexto = "${latLng.latitude}, ${latLng.longitude}"
                            googleMap.clear()
                            googleMap.addMarker(MarkerOptions().position(latLng))
                        }
                    }
                }
            },
            update = { mapView ->
                mapView.onResume()
            }
        )
    }
}
