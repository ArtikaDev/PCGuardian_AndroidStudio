/**
* Computer.kt
*
* Archivo  que contiene la clase Computer, representando los inicios de sesion de un ordenador
 */

package com.osolar.securityapp.firebass

/**
 * Esta clase sirve como modelo para representar un objeto de tipo Ordenador dentro de la aplicación.
 * Se utiliza principalmente para interactuar con Firebase al almacenar o recuperar información de inicios de sesion de un ordenador.
 *
 * @property name Nombre del ordenador.
 * @property date Fecha asociada al inicio de sesión del ordenador.
 * Estas propiedades son obligatorias para crear una instancia de un objeto Computer().
 */

data class Computer (
    val name:String = "",
    val date:String = ""
)

