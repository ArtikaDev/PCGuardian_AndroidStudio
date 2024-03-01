/**
 * VentanaPrincipal.kt
 *
 * Archivo que contiene funciones para el mostrado de la Pantalla principal. Muestra ordenadores listados
 * que contiene un usuario desde firebase.
 */

package com.osolar.securityapp.ventanas


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.osolar.securityapp.R
import com.osolar.securityapp.firebass.Computer
import com.osolar.securityapp.firebass.ScreenViewModel
import kotlinx.coroutines.launch

/**
 * Función principal de la pantalla principal.
 *
 * @param navController Controlador de navegación para manejar las transiciones entre pantallas.
 * @param dataViewModel ViewModel que contiene los datos necesarios para esta pantalla.
 */

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
fun Principal(navController: NavController, dataViewModel: ScreenViewModel = viewModel()){

    // Obtener el contexto actual de la aplicación
    val context = LocalContext.current

    // Estado mutable para mantener la lista de ordenadores
    val computersState = remember{mutableStateOf<List<Computer>>(dataViewModel.computersState)}

    // Estado mutable para mantener la lista de ordenadores filtrada
    val filteredComputersState = remember { mutableStateOf<List<Computer>>(dataViewModel.computersState) }

    // Estado mutable para almacenar el texto de búsqueda
    var searchText by remember { mutableStateOf("") }

    // Obtener el nombre del usuario actual
    val userName = dataViewModel.userState

    // Funciones para actualizar la lista de ordenadores en diferentes órdenes
    fun updateListAsc() {
        filteredComputersState.value = dataViewModel.computerAscendentOrder
    }

    fun updateListDesc() {
        filteredComputersState.value = dataViewModel.computerDescendentOrder
    }

    fun updateListAlfaAsc() {
        filteredComputersState.value = dataViewModel.computerAlfaAscendentOrder
    }

    fun updateListAlfaDesc() {
        filteredComputersState.value = dataViewModel.computerAlfaDescendentOrder
    }

    // Efecto lanzado cuando cambia el texto de búsqueda para actualizar la lista filtrada
    LaunchedEffect(searchText) {
        filteredComputersState.value = if (searchText.isEmpty()) {
            // Si searchText está vacío, usa la lista completa
            dataViewModel.computersState
        } else {
            // De lo contrario, aplica el filtro
            computersState.value.filter { it.name.contains(searchText, ignoreCase = true) }
        }
    }

    // Estado de la caja de navegación
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    // Construccion de la interfaz de usuario utilizando el componente Scaffold
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", modifier = Modifier.padding(16.dp))
                Divider()
                // Elemento de menú para navegar al perfil del usuario
                NavigationDrawerItem(
                    label = { Text(text = "Perfil") },
                    selected = false,
                    onClick = { navController.navigate("perfil") }
                )
                // Elemento de menú para cerrar sesión
                NavigationDrawerItem(
                    label = { Text(text = "Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        auth.signOut()
                        navController.navigate("bienvenida")
                        Toast.makeText(context, "Se ha cerrado su sesión", Toast.LENGTH_SHORT).show()
                    }
                )
                // Elemento de menú para navegar a la pantalla ubicación
                NavigationDrawerItem(
                    label = { Text(text = "Ubicación") },
                    selected = false,
                    onClick = {
                        navController.navigate("ubicacion")
                    }
                )
            }
        },
    ) {
        // Contenido principal de la pantalla
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color(0xff5c92a0),
                        titleContentColor = Color.White,
                    ),
                    title = {
                        Text("Inicios de Sesión ( "+ userName.value.name + " )")
                    },
                    navigationIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.pc_guardian_logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .width(90.dp)
                                .height(90.dp)
                                .padding(end = 10.dp))

                    },
                    actions = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color.White
                            )
                        }
                    },
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color(0xff5c92a0),
                    actions = {
                        IconButton(
                            onClick = {
                                updateListAsc()
                            }) {
                            Icon(
                                Icons.Filled.KeyboardArrowUp,
                                contentDescription = "TimeAscendentOrder",
                                tint = Color.White)
                        }
                        IconButton(
                            onClick = {
                                updateListDesc()

                            }) {
                            Icon(
                                Icons.Filled.KeyboardArrowDown,
                                contentDescription = "TimeDescendentOrder",
                                tint = Color.White
                            )
                        }
                        Button(
                            onClick = {
                                updateListAlfaAsc()
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xff5c92a0))
                        ) {
                            Text(text = "A", color = Color.White)
                        }
                        Button(
                            onClick = {
                                updateListAlfaDesc()
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xff5c92a0))
                        ) {
                            Text(text = "Z", color = Color.White)
                        }
                        // Barra de búsqueda
                        SearchBar(searchText = searchText, onSearchTextChanged = { searchText = it })
                    }
                )
            }
        ) {paddingValues ->
            // Columna desplazable de elementos
            PaddeableColumn(computers = filteredComputersState, paddingValues)}
    }
}

/**
 * Barra de búsqueda para filtrar los elementos.
 *
 * @param searchText Texto de búsqueda actual.
 * @param onSearchTextChanged Función de callback para manejar el cambio en el texto de búsqueda.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchText: String, onSearchTextChanged: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Buscar...", color = Color.White) },
            trailingIcon = {
                Icon(Icons.Filled.Search, "Buscar",
                    tint = Color.White)
            },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Cyan,
                unfocusedBorderColor = Color.White
            )
        )
    }
}

/**
 * Columna de elementos que contiene la lista de objetos computers.
 *
 * @param computers Estado mutable de la lista de ordenadores a mostrar.
 * @param paddingValues Valores de relleno para la columna.
 */

@Composable
fun PaddeableColumn(computers: MutableState<List<Computer>>, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        contentAlignment = Alignment.Center) {
        LazyColumn(contentPadding = paddingValues) {
            itemsIndexed(computers.value) { index, computer ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xff5c92a0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Ordenador ${index + 1}",
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                        Text(text = "Nombre: ${computer.name}", color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Fecha: ${computer.date}", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}