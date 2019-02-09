package com.example.examenapplication

import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.*
import com.github.kittinunf.result.Result

class PokemonHTTP(
    var numeroPokemon: Long,
    var nombrePokemon: String,
    var poderEspecialUno: String,
    var poderEspecialDos: String,
    var fechaCaptura: String,
    var nivel: Int,

    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var id: Int? = null
) {
    constructor(): this (0, "", "", "", "", 0)


    val url = "http://192.168.0.8:1337/Pokemon"

    fun crearPokemon(idEntrenador: Int?) {

        val parametros = listOf(
            "numeroPokemon" to numeroPokemon,
            "nombrePokemon" to nombrePokemon,
            "poderEspecialUno" to poderEspecialUno,
            "poderEspecialDos" to poderEspecialDos,
            "fechaCaptura" to fechaCaptura,
            "nivel" to nivel,
            "idEntrenador" to idEntrenador
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

                        val pokemonClase: PokemonHTTP? = Klaxon()
                            .parse<PokemonHTTP>(usuarioString)

                        Log.i("httpres", "Datos: ${pokemonClase?.nombrePokemon}")

                    }
                }
            }
    }



    fun eliminar(id:Int?){
        val urlParam = url+'/'+id
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

                        val pokemonClase: PokemonHTTP? = Klaxon()
                            .parse<PokemonHTTP>(usuarioString)

                        Log.i("httpres", "Datos: ${pokemonClase?.nombrePokemon}")

                    }
                }
            }
    }
    fun obtenerPorId(idEntrenador: Int?){
//        val urlParam = url+'/'+id
        val parametros = listOf(
            "idEntrenador" to idEntrenador

        )
        url.httpGet(parametros)
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("httpres", "Error: ${ex}")
                    }
                    is Result.Success -> {
                        val data = result.get()
                        Log.i("data", "Data: ${data}")

                        val pokemonArray = Klaxon().parseArray<PokemonHTTP>(data)
                        Log.i("httpres", "Datos: ${pokemonArray?.toString()}")
                        if (pokemonArray != null) {
                            BDD.pokemones.clear()
                            for ( pokemon in pokemonArray.iterator()){
                                Log.i("httpres", "Entrenador: ${pokemon.nombrePokemon}")
                                Log.i("httpres", "Entrenador: ${pokemon}")

                                val pokemonInsert = PokemonHTTP(
                                    pokemon.numeroPokemon,
                                    pokemon.nombrePokemon,
                                    pokemon.poderEspecialUno,
                                    pokemon.poderEspecialDos,
                                    pokemon.fechaCaptura,
                                    pokemon.nivel,
                                    pokemon.createdAt,
                                    pokemon.updatedAt,
                                    pokemon.id)
                                BDD.pokemones.add(pokemonInsert)
                            }
                        }

                    }
                }
            }
    }

    fun actualizar(id:Int?){
        val urlParam = url+'/'+id

        val parametros = listOf(
            "numeroPokemon" to numeroPokemon,
            "nombrePokemon" to nombrePokemon,
            "poderEspecialUno" to poderEspecialUno,
            "poderEspecialDos" to poderEspecialDos,
            "nivel" to nivel,
            "fechaCaptura" to fechaCaptura

//            "idEntrenador" to idEntrenador

        )

        urlParam.httpPut(parametros)
            .responseString { request, response, result ->

                when (result) {
                    is Result.Failure -> {
                        val exepcion = result.getException()
                        Log.i("httpres", "Error: ${exepcion}")
                        Log.i("httpres", "Error: ${response}")

                    }
                    is Result.Success -> {

                        val usuarioString = result.get()

                        val pokemonClase: PokemonHTTP? = Klaxon()
                            .parse<PokemonHTTP>(usuarioString)

                        Log.i("httpres", "Datos: ${pokemonClase?.nombrePokemon}")

                    }
                }
            }
    }


}