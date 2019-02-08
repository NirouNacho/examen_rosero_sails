package com.example.examenapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_formulario_pokemon.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.content.ClipData
import android.os.Build



class FormularioPokemonActivity : AppCompatActivity() {

    var pathActualFoto = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_pokemon)
        val pokemonActualizar = intent.getParcelableExtra<Pokemon?>("pokemon_pasar")

        val entrenador = intent.getParcelableExtra<Entrenador?>("pokemon")


        if (pokemonActualizar != null) {
            Log.i("intent-editar", pokemonActualizar.nombrePokemon)
            Log.i("intent-editar", "${pokemonActualizar.poderEspecialUno}")
            mostrarDatos(pokemonActualizar)
        }

        button_guardarm.setOnClickListener {
            val pokemonFormulario = obtenerParametros()


            if (pokemonActualizar == null) {
                pokemonFormulario.crearPokemon(entrenador?.id)
                Alerter.create(this@FormularioPokemonActivity)
                    .setTitle("Pokemon creado")
                    .setText("Nombre:  ${pokemonFormulario.nombrePokemon}")
                    .setDuration(10000)
                    .setOnHideListener({
                        regresar(entrenador)
                    })
                    .show()
            } else if (pokemonActualizar != null) {


                pokemonFormulario.actualizar(pokemonActualizar.id)
                Alerter.create(this@FormularioPokemonActivity)
                    .setTitle("Pokemon actualizada")
                    .setText("Nombre:  ${pokemonFormulario.nombrePokemon}")
                    .setDuration(10000)
                    .setOnHideListener {
                        regresar(entrenador)
                    }
                    .show()


            }


        }

        button_foto.setOnClickListener {
            tomarFoto()

        }

    }
    fun regresar(entrenador: Entrenador?){
        val intent = Intent(this, ListarPokemonActivity::class.java)
        intent.putExtra("entrenador_pasar", entrenador)
        this.startActivity(intent)
        this.finish()
    }



    fun obtenerParametros(): PokemonHTTP {

        val numeroPokemon = editText_numeroPokemon.text.toString().toLong()
        val nombrePokemon = editText_nombre_pokemon.text.toString()
        val poderEspecialUno = editText_poder_uno.text.toString()
        val poderEspecialDos = editText_poder_dos.text.toString()
        val fechaCaptura = editText_fechac_pokemon.text.toString()
        val nivel = editText_nivel_pokemon.text.toString().toInt()


        return PokemonHTTP(numeroPokemon, nombrePokemon, poderEspecialUno, poderEspecialDos, nivel, fechaCaptura)
    }

    fun mostrarDatos(pokemon: Pokemon) {
        editText_numeroPokemon.setText(pokemon.numeroPokemon.toString())
        editText_nombre_pokemon.setText(pokemon.nombrePokemon)
        editText_fechac_pokemon.setText(pokemon.fechaCreacion)
        editText_poder_uno.setText(pokemon.poderEspecialUno.toString())
        editText_poder_dos.setText(pokemon.poderEspecialDos)
        editText_nivel_pokemon.setText(pokemon.nivel.toString())

    }
    fun tomarFoto() {
        val archivoImagen = crearArchivo(
            "JPEG_",
            Environment.DIRECTORY_PICTURES,
            ".jpg")

        pathActualFoto = archivoImagen.absolutePath
        Log.i("pathActualFoto", pathActualFoto)
        enviarIntentFoto(archivoImagen)

    }




    private fun crearArchivo(
        prefijo: String,
        directorio: String,
        extension: String): File {

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(Date())

        val prefijoImagen = prefijo + timeStamp + "_"

        val directorioAGuardarImagen = getExternalFilesDir(directorio)

        return File.createTempFile(
            prefijoImagen, /* prefijo */
            extension, /* fufijo */
            directorioAGuardarImagen /* directorio */
        )
    }

    private fun enviarIntentFoto(archivo: File) {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.examenapplication",
            archivo)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            takePictureIntent.clipData = ClipData.newRawUri("", photoURI)
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

        if (takePictureIntent.resolveActivity(packageManager) != null) {

            startActivityForResult(takePictureIntent, TOMAR_FOTO_REQUEST);

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TOMAR_FOTO_REQUEST -> {

                if (resultCode == Activity.RESULT_OK) {
                    obtenerInfoCodigoBarras(obtenerBitmapDeArchivo(pathActualFoto))
                   mostrarFotoImageView()
                    if (!respuestasBarCode.isEmpty()){
                        editText_numeroPokemon.setText(respuestasBarCode[0].toString())
                    }
                    for (barcode in respuestasBarCode){
                        Log.i("barcode", barcode)
                    }
                }
            }

        }
    }

    fun mostrarFotoImageView() {

        image_view_codigo_de_barras.setImageBitmap(obtenerBitmapDeArchivo(pathActualFoto))

    }


    fun obtenerBitmapDeArchivo(path: String): Bitmap {
        val archivoImagen = File(path)
        return BitmapFactory
            .decodeFile(
                archivoImagen.getAbsolutePath()
            )
    }

    fun obtenerInfoCodigoBarras(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
            .visionBarcodeDetector
        Log.i("info", "------- Entro a detectar")
        val result = detector.detectInImage(image)
            .addOnSuccessListener { barCodes ->
                Log.i("info", "------- tamano del barcode ${barCodes.size}")
                respuestasBarCode.add("Ejemplo")
                for (barcode in barCodes) {
                    val bounds = barcode.getBoundingBox()
                    val corners = barcode.getCornerPoints()

                    val rawValue = barcode.getRawValue()

                    Log.i("info", "------- $bounds")
                    Log.i("info", "------- $corners")
                    Log.i("info", "------- $rawValue")

                    respuestasBarCode.add(rawValue.toString())
                }
                editText_numeroPokemon.setText(respuestasBarCode[1])
            }
            .addOnFailureListener {
                Log.i("info", "------- No reconocio nada")
            }
    }



    companion object {
        val TOMAR_FOTO_REQUEST = 1
        var respuestasBarCode = ArrayList<String>()
    }

}