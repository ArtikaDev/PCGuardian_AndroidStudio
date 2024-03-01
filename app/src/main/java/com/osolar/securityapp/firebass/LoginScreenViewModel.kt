package com.osolar.securityapp.firebass

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

/**
 * LoginScreenViewModel.kt
 *
 * Esta clase funciona con un ViewModel encargado de manejar la lógica de autenticación para la pantalla de login.
 * Provee funciones para iniciar sesión y registrar nuevos usuarios utilizando Firebase Auth,
 * y mantiene el estado de carga y errores para la interfaz de usuario.
 */

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth // Instancia de FirebaseAuth para autenticación.
    private val _loading = MutableLiveData(false) // Estado de carga durante operaciones de autenticación.

    private val _errorState = mutableStateOf<String?>(null) // Estado para manejar mensajes de error.
    val errorState: State<String?> = _errorState // Estado público para acceder a los mensajes de error desde la UI.

    /**
     * Intenta iniciar sesión con un email y contraseña proporcionados.
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     * @param home Callback que se llama con el resultado de la operación (true si es exitosa, false si hay error).
     */

    fun signInWithEmailAndPass(email: String, password:String, home: (Boolean) -> Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { _ ->
                    Log.d("SecurityApp", "signInWithEmailAndPassword logueado")
                    home(true)
                }.addOnFailureListener {e ->
                    Log.d("SecurityApp", "signInWithEmailAndPassword: ${e.message}")
                    _errorState.value = "Contraseña o usuario incorrecto"
                    home(false)
                }
        }catch (ex:Exception){
            Log.d("SecurityApp", "signInWithEmailAndPassword: ${ex.message}")
            _errorState.value = "Error al iniciar sesión"
            home(false)
        }
    }

    /**
     * Registra un nuevo usuario con email, contraseña y teléfono.
     * @param email Email para el nuevo usuario.
     * @param password Contraseña para el nuevo usuario.
     * @param phone Teléfono del nuevo usuario.
     * @param home Callback que se llama con el resultado de la operación.
     */

    fun createUserWithEmailAndPassword(
        email:String,
        password: String,
        phone: String,
        home: (Boolean) -> Unit
    ){
        try {
            if (_loading.value == false){
                _loading.value = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val displayName =
                                task.result.user?.email?.split("@")?.get(0)
                            creaeUser(displayName, phone)
                            home(true)
                        }else{
                            home(false)
                            Log.d("SecurityApp", "createUserWithEmailAndPassword: ${task.result}")
                            _errorState.value = "Algun campo es incorrecto"
                        }
                    }.addOnFailureListener { e ->
                        Log.d("SecurityApp", "errorCreateUserWithEmailAndPassword: ${e.message}")
                        _errorState.value = "Algun campo es incorrecto"
                        home(false)
                    }
            }
        }catch (ex:Exception){
            Log.d("SecurityApp", "createUserWithEmailAndPassword: ${ex.message}")
            _errorState.value = "Algun campo es incorrecto"
            home(false)
        }

    }

    /**
     * Crea un registro de usuario en Firestore con el nombre, teléfono y ID de usuario proporcionados.
     * @param displayName Nombre de usuario para mostrar.
     * @param phone Teléfono del usuario.
     */

    private fun creaeUser(displayName: String?, phone: String?) {
        val userId = auth.currentUser?.uid
        val user = mutableMapOf<String, Any>()

        user["user_id"] = userId.toString()
        user["display_name"] = displayName.toString()
        user["phone"] = phone.toString()
        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("SecurityApp", "Creado ${it.id}")
            }.addOnFailureListener {
                Log.d("SecurityApp", "Ocurrio un error $it")
            }
    }
}

