package com.example.examenapplication

import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.*
import com.github.kittinunf.result.Result

class EntrenadorHttp(
    var nombres: String,
    var apellidos: String,
    var fechaNacimiento: String,
    var numeroMedallas: Int,
    var campeonActual: Boolean,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var id: Int? = null
) {

    constructor(): this ("", "", "", 0, false)

    val url = "http://192.168.0.8:1337/Entrenador"

    fun crearEntrenador() {

        val parametros = listOf(
            "nombres" to nombres,
            "apellidos" to apellidos,
            "fechaNacimiento" to fechaNacimiento,
            "numeroMedallas" to numeroMedallas,
            "campeonActual" to campeonActual

        )

        Log.i("httpres", parametros.toString())

        url
            .httpPost(parametros)
            .responseString { request, response, result ->

                when (result) {
                    is Result.Failure -> {
                        val exepcion = result.getException()
                        Log.i("httpres", "Error: ${exepcion}")
                        Log.i("httpres", "Error: ${response}")

                    }
                    is Result.Success -> {

                        val usuarioString = result.get()

                        val entrenadorClase: EntrenadorHttp? = Klaxon()
                            .parse<EntrenadorHttp>(usuarioString)

                        Log.i("httpres", "Datos: ${entrenadorClase?.nombres}")

                    }
                }
            }
    }

    fun obtenerTodos(){
        url.httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("httpres", "Error: ${ex}")
                    }
                    is Result.Success -> {
                        val data = result.get()
                        Log.i("httpres", "${data}")

                        val entrenadorArray = Klaxon().parseArray<EntrenadorHttp>(data)
                        Log.i("httpres", "Datos: ${entrenadorArray?.toString()}")
                        if (entrenadorArray != null) {
                            BDD.entrenadores.clear()
                            for ( entrenador in entrenadorArray.iterator()){
                                Log.i("httpres", "Entrenador: ${entrenador.nombres}")
                                Log.i("httpres", "Entrenador: ${entrenador}")

                                val estudianteInsert = EntrenadorHttp(entrenador.nombres, entrenador.apellidos,
                                    entrenador.fechaNacimiento, entrenador.numeroMedallas, entrenador.campeonActual,
                                    entrenador.createdAt, entrenador.updatedAt, entrenador.id)
                                BDD.entrenadores.add(estudianteInsert)
                            }
                        }

                    }
                }
            }
    }

    fun eliminar(id:Int?){
        var urlParam = url+'/'+id
        urlParam.httpDelete()
            .responseString { request, response, result ->

                when (result) {
                    is Result.Failure -> {
                        val exepcion = result.getException()
                        Log.i("httpres", "Error: ${exepcion}")
                        Log.i("httpres", "Error: ${response}")

                    }
                    is Result.Success -> {

                        val usuarioString = result.get()

                        val estudianteClase: EntrenadorHttp? = Klaxon()
                            .parse<EntrenadorHttp>(usuarioString)

                        Log.i("httpres", "Datos: ${estudianteClase?.nombres}")

                    }
                }
            }
    }

    fun actualizar(id:Int?){
        val urlParam = url+'/'+id

        val parametros = listOf(
            "nombres" to nombres,
            "apellidos" to apellidos,
            "fechaNacimiento" to fechaNacimiento,
            "numeroMedallas" to numeroMedallas,
            "campeonActual" to campeonActual

        )

        urlParam.httpPut(parametros)
            .responseString { request, response, result ->
    Log.i("http","parametros" + parametros)
                when (result) {
                    is Result.Failure -> {
                        val exepcion = result.getException()
                        Log.i("httpres", "Error: ${exepcion}")
                        Log.i("httpres", "Error: ${response}")

                    }
                    is Result.Success -> {

                        val usuarioString = result.get()

                        val estudianteClase: EntrenadorHttp? = Klaxon()
                            .parse<EntrenadorHttp>(usuarioString)

                        Log.i("httpres", "Datos: ${estudianteClase?.nombres}")

                    }
                }
            }
    }
}