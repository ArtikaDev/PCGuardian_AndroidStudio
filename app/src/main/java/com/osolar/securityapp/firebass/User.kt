package com.osolar.securityapp.firebass

/**
 * User.kt
 *
 * Clase de datos que representa a un usuario.
 * @property name El nombre del usuario.
 * @property phone El número de teléfono del usuario.
 * @property id El identificador único del usuario.
 */

data class User (
    val name:String = "",
    val phone:String = "",
    val id:String = ""
)


