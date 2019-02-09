package com.example.examenapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EntrenadorHttp().obtenerTodos()

        button_crear_entrenador.setOnClickListener {
            this.irACrearEntrenador()
        }
        button_listar_estudiante.setOnClickListener {
            this.irAListarEntrenador()
        }


    }

    fun irACrearEntrenador(){
        intent = Intent(this, FormularioEntrenadorActivity::class.java)
        this.startActivity(intent)
    }

    fun irAListarEntrenador(){
        intent = Intent(this, ListarEntrenadorActivity::class.java)
        this.startActivity(intent)
    }
}
