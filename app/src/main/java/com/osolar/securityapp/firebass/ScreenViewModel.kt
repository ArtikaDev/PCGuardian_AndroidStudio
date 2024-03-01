package com.osolar.securityapp.firebass

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * ScreenViewModel.kt
 *
 * Esta clase tiene asociado un ViewModel que gestiona el estado y la lógica de negocio para la pantalla relacionada con la visualización
 * y ordenación de computadoras, así como la obtención de datos del usuario actual.
 *
 * Provee estados observables para listas de computadoras en diferentes criterios de ordenación y para los
 * datos del usuario actual.
 */

class ScreenViewModel: ViewModel() {
    // Estados para las listas de computadoras en diferentes criterios de ordenación
    val computersState =  mutableStateListOf<Computer>()
    val computerAscendentOrder = mutableStateListOf<Computer>()
    val computerDescendentOrder = mutableStateListOf<Computer>()
    val computerAlfaAscendentOrder = mutableStateListOf<Computer>()
    val computerAlfaDescendentOrder = mutableStateListOf<Computer>()

    // Estado para los datos del usuario actual
    val userState = mutableStateOf(User())

    // Inicializa la obtención de datos al crear el ViewModel
    init {
        getComputersData()
        getUserData()
    }

    /**
     * Inicia las corutinas para obtener datos de computadoras en distintos criterios de ordenación.
     */

    private fun getComputersData(){
        viewModelScope.launch {
            computersState.addAll(getComputersFromUser())
            computerAscendentOrder.addAll(getComputersAscendentOrder())
            computerDescendentOrder.addAll(getComputersDescendentOrder())
            computerAlfaAscendentOrder.addAll(getComputersAlfaAscendentOrder())
            computerAlfaDescendentOrder.addAll(getComputersAlfaDescendentOrder())
        }
    }

    /**
     * Inicia la corutina para obtener los datos del usuario actual.
     */

    private fun getUserData(){
        viewModelScope.launch {
            userState.value = getUser()
        }
    }

}

/**
 * Obtiene la información del usuario actual de Firestore y la retorna como un objeto User.
 *
 * @return Un objeto User con los datos del usuario actual.
 */

suspend fun getUser(): User {
    val db = FirebaseFirestore.getInstance()
    var userState = User()
    val auth = FirebaseAuth.getInstance()

    // Intenta obtener el ID del usuario actual y sus datos de Firestore

    try {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val docSnapshot = db.collection("users")
                    .whereEqualTo("user_id", userId)
                    .get().await()
                val userDocument = docSnapshot.documents.first()
                val displayName = userDocument.getString("display_name") ?: ""
                val phone = userDocument.getString("phone") ?: ""

                // Asegúrate de que tu clase User tenga un constructor que acepte ambos, displayName y phone.
                userState = User(displayName, phone)
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error al obtener el nombre de usuario", e)
            }
        } else {
            Log.e("FirestoreError", "Usuario no autenticado")
        }


    } catch (e: Exception) {
        Log.e("FirestoreError", "Error al obtener el nombre de usuario", e)
    }

    return userState
}

/**
 * Funciones `getComputersFromUser`, `getComputersAscendentOrder`, `getComputersDescendentOrder`,
 * `getComputersAlfaAscendentOrder`, y `getComputersAlfaDescendentOrder`:
 *
 * Estas funciones obtienen y ordenan la lista de computadoras asociadas al usuario actual desde Firestore.
 * La ordenación se realiza según el nombre y la fecha de inicio, tanto en orden ascendente como descendente.
 *
 * @return Una lista mutable de objetos Computer, cada uno representando una computadora.
 */

/**
 * Obtiene la información del usuario actual sobre los inicios de sesion que tiene de un ordenador
 *
 * @return Una lista de  objetos Comuputer() con los datos de los inicios de sesion.
 */

suspend fun getComputersFromUser(): MutableList<Computer> {
    val db = FirebaseFirestore.getInstance()
    val listComputers = mutableListOf<Computer>()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    try {
        // Encuentra el documento del usuario por el campo user_id
        val userQuerySnapshot = db.collection("users")
            .whereEqualTo("user_id", userId)
            .get().await()

        if (!userQuerySnapshot.isEmpty) {
            val userDocument = userQuerySnapshot.documents.first()

            // Accede a la subcolección ordenadores de ese usuario
            val computersQuerySnapshot = userDocument.reference.collection("ordenadores").get().await()

            for (document in computersQuerySnapshot.documents) {
                val name = document.getString("ordenador") // Asume que el campo se llama "nombre"
                val startTime = document.getTimestamp("hora de inicio")?.toDate() // Asume que el campo se llama "horaInicio"

                // Ajusta los nombres de los campos y la clase Computer según tu implementación
                val computer = Computer(name.orEmpty(), startTime.toString())
                listComputers.add(computer)
            }
        }
    } catch (e: Exception) {
        Log.e("FirestoreError", "Error al obtener los ordenadores del usuario", e)
    }

    return listComputers
}

/**
 * Obtiene la información del usuario actual sobre los inicios de sesion que tiene de un ordenador de forma descendente
 * sobre el @param "hora de inicio"
 *
 * @return Una lista de  objetos Comuputer() con los datos de los inicios de sesion.
 */

suspend fun getComputersDescendentOrder():MutableList<Computer>{
    val db = FirebaseFirestore.getInstance()
    val listComputers = mutableListOf<Computer>()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    try {
        val userQuerySnapshot = db.collection("users")
            .whereEqualTo("user_id", userId)
            .get().await()
        val userDocument = userQuerySnapshot.documents.first()
        val computersQuerySnapshot = userDocument.reference.collection("ordenadores").orderBy("hora de inicio", Query.Direction.DESCENDING).get().await()
        for (document in computersQuerySnapshot.documents) {
            val name = document.getString("ordenador")
            val date = document.getTimestamp("hora de inicio")

            val computer = Computer(name.orEmpty(), date?.toDate().toString())
            listComputers.add(computer)
        }
    }catch (e: FirebaseFirestoreException){
        Log.d("error", "getDataFromFireStore: $e")
    }
    return listComputers
}

/**
 * Obtiene la información del usuario actual sobre los inicios de sesion que tiene de un ordenador de forma ascendente
 * sobre el @param "hora de inicio"
 *
 * @return Una lista de  objetos Comuputer() con los datos de los inicios de sesion.
 */

suspend fun getComputersAscendentOrder():MutableList<Computer>{
    val db = FirebaseFirestore.getInstance()
    val listComputers = mutableListOf<Computer>()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    try {
        val userQuerySnapshot = db.collection("users")
            .whereEqualTo("user_id", userId)
            .get().await()
        val userDocument = userQuerySnapshot.documents.first()
        val computersQuerySnapshot = userDocument.reference.collection("ordenadores").orderBy("hora de inicio", Query.Direction.ASCENDING).get().await()
        for (document in computersQuerySnapshot.documents) {
            val name = document.getString("ordenador")
            val date = document.getTimestamp("hora de inicio")

            val computer = Computer(name.orEmpty(), date?.toDate().toString())
            listComputers.add(computer)
        }
    }catch (e: FirebaseFirestoreException){
        Log.d("error", "getDataFromFireStore: $e")
    }
    return listComputers
}

/**
 * Obtiene la información del usuario actual sobre los inicios de sesion que tiene de un ordenador de forma ascendente
 * sobre el @param "ordenador"
 *
 * @return Una lista de  objetos Comuputer() con los datos de los inicios de sesion.
 */

suspend fun getComputersAlfaAscendentOrder():MutableList<Computer>{
    val db = FirebaseFirestore.getInstance()
    val listComputers = mutableListOf<Computer>()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    try {
        val userQuerySnapshot = db.collection("users")
            .whereEqualTo("user_id", userId)
            .get().await()
        val userDocument = userQuerySnapshot.documents.first()
        val computersQuerySnapshot = userDocument.reference.collection("ordenadores").orderBy("ordenador", Query.Direction.ASCENDING).get().await()
        for (document in computersQuerySnapshot.documents) {
            val name = document.getString("ordenador")
            val date = document.getTimestamp("hora de inicio")

            val computer = Computer(name.orEmpty(), date?.toDate().toString())
            listComputers.add(computer)
        }
    }catch (e: FirebaseFirestoreException){
        Log.d("error", "getDataFromFireStore: $e")
    }
    return listComputers
}

/**
 * Obtiene la información del usuario actual sobre los inicios de sesion que tiene de un ordenador de forma descendente
 * sobre el @param "ordenador"
 *
 * @return Una lista de  objetos Comuputer() con los datos de los inicios de sesion.
 */

suspend fun getComputersAlfaDescendentOrder():MutableList<Computer>{
    val db = FirebaseFirestore.getInstance()
    val listComputers = mutableListOf<Computer>()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    try {
        val userQuerySnapshot = db.collection("users")
            .whereEqualTo("user_id", userId)
            .get().await()
        val userDocument = userQuerySnapshot.documents.first()
        val computersQuerySnapshot = userDocument.reference.collection("ordenadores").orderBy("ordenador", Query.Direction.DESCENDING).get().await()
        for (document in computersQuerySnapshot.documents) {
            val name = document.getString("ordenador")
            val date = document.getTimestamp("hora de inicio")

            val computer = Computer(name.orEmpty(), date?.toDate().toString())
            listComputers.add(computer)
        }
    }catch (e: FirebaseFirestoreException){
        Log.d("error", "getDataFromFireStore: $e")
    }
    return listComputers
}