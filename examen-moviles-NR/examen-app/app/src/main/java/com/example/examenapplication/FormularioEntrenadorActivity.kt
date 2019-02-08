package com.example.examenapplication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tapadoo.alerter.Alerter


import kotlinx.android.synthetic.main.activity_formulario_entrenador.*
//import android.support.test.espresso.matcher.ViewMatchers.isChecked


class FormularioEntrenadorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_entrenador)

        val entrenadorActualizar = intent.getParcelableExtra<Entrenador?>("entrenador_pasar")

        if (entrenadorActualizar != null) {
            Log.i("intent-editar", entrenadorActualizar.nombres)
            Log.i("intent-editar", "${entrenadorActualizar.numeroMedallas}")
            mostrarDatos(entrenadorActualizar)
        }

        intent = Intent(this, MainActivity::class.java)

        button_entrenador_cancelar.setOnClickListener {
            regresar()

        }

        button_entrenador_guardar.setOnClickListener {
            val entrenadorFormulario = obtenerParametros()


            if (entrenadorActualizar == null) {
                entrenadorFormulario.crearEntrenador()
                Alerter.create(this@FormularioEntrenadorActivity)
                    .setTitle("Entrenador creado")
                    .setText("Nombre:  ${entrenadorFormulario.nombres}")
                    .setDuration(10000)
                    .setOnHideListener({
                        regresar()
                    })
                    .show()
            } else if (entrenadorActualizar != null) {


                val entrenadorActualizado = obtenerParametros()

                devolverActualizar(entrenadorActualizar, entrenadorActualizado)


                /*
                estudianteFormulario.actualizar(estudianteActualizar.id)
                Alerter.create(this@FormularioEntrenadorActivity)
                    .setTitle("Entrenador actualizado")
                    .setText("Nombre:  ${estudianteFormulario.nombres}")
                    .setDuration(10000)
                    .setOnHideListener {
                        this.startActivity(Intent(this, ListarEntrenadorActivity::class.java))
                        this.finish()
                    }
                    .show()
                    */

            }


        }

    }

    fun devolverActualizar(entrenadorPorActualizar: Entrenador, entrenadorActual: EntrenadorHttp) {

        entrenadorPorActualizar.nombres = entrenadorActual.nombres
        entrenadorPorActualizar.numeroMedallas = entrenadorActual.numeroMedallas
        entrenadorPorActualizar.fechaNacimiento = entrenadorActual.fechaNacimiento
        entrenadorPorActualizar.numeroMedallas = entrenadorActual.numeroMedallas
        entrenadorPorActualizar.campeonActual = entrenadorActual.campeonActual

        val intentRespuesta = Intent()

        Log.i("entrenador_pasar", "${entrenadorPorActualizar.nombres}")
        intentRespuesta.putExtra("entrenador_pasar", entrenadorPorActualizar)

        this.setResult(
            Activity.RESULT_OK,
            intentRespuesta
        )

        this.finish()

    }
    fun regresar(){
        this.startActivity(intent)
        this.finish()
    }

    fun obtenerParametros(): EntrenadorHttp {

        val nombres = editText_entrenador_nombres.text.toString()
        val apellidos = editText_entrenador_apellidos.text.toString()
        val fechaNacimiento = editText_entrenador_fechaNacimiento.text.toString()
        val numeroMedallas = editText_entrenador_medallas.text.toString().toInt()
        val campeonActual = campeonActual.isChecked()

        return EntrenadorHttp(nombres, apellidos, fechaNacimiento, numeroMedallas, campeonActual)
    }

    fun mostrarDatos(entrenador: Entrenador) {
        editText_entrenador_nombres.setText(entrenador.nombres)
        editText_entrenador_apellidos.setText(entrenador.apellidos)
        editText_entrenador_fechaNacimiento.setText(entrenador.fechaNacimiento)
        editText_entrenador_medallas.setText(entrenador.numeroMedallas.toString())
        campeonActual.isChecked = entrenador.campeonActual
    }

}


