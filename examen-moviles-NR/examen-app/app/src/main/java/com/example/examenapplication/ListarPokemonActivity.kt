package com.example.examenapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_listar_pokemon.*

class ListarPokemonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_pokemon)

        val entrenadorMostrar = intent.getParcelableExtra<Entrenador?>("entrenador_pasar")
        PokemonHTTP().obtenerPorId(entrenadorMostrar!!.id)

        if (entrenadorMostrar != null) {
            Log.i("intent-mostar", entrenadorMostrar.nombres)
            Log.i("intent-mostrar", "${entrenadorMostrar.id}")
            mostrarDatos(entrenadorMostrar)
        }

        for (pokemon in BDD.pokemones) {
            Log.i("bdd-m", pokemon.nombrePokemon)
        }

        button_crear_pokemon.setOnClickListener {
            irACrearPokemon(entrenadorMostrar)
        }

        val layoutManager = LinearLayoutManager(this)
        val rv = rview_pokemones

        val adaptador = PokemonesAdaptador(BDD.pokemones, this, entrenadorMostrar)

        rview_pokemones.layoutManager = layoutManager
        rview_pokemones.itemAnimator = DefaultItemAnimator()
        rview_pokemones.adapter = adaptador

        adaptador.notifyDataSetChanged()

    }

    fun mostrarDatos(entrenador: Entrenador) {
        textView_nombresE.setText(entrenador.nombres)
        textView_apellidosE.setText(entrenador.apellidos)
        textView_fechaNacimiento.setText(entrenador.fechaNacimiento)
        textView_semestreE.setText(entrenador.numeroMedallas.toString())
    }

    fun irACrearPokemon(entrenador: Entrenador){
        val intent = Intent(this, FormularioPokemonActivity::class.java)
        intent.putExtra("entrenador", entrenador)
        this.startActivity(intent)
        this.finish()
    }
}

class PokemonesAdaptador(val listaPokemones: ArrayList<PokemonHTTP>, private val contexto: ListarPokemonActivity, val entrenador: Entrenador) :
    RecyclerView.Adapter<PokemonesAdaptador.MyViewHolder2>() {

    inner class MyViewHolder2(view: View) : RecyclerView.ViewHolder(view) {

        var nombreTextView: TextView
        var poderUnoTextView: TextView
        var boton_opciones: Button

        init {
            Log.i("debug", "Entro en Holder")
            nombreTextView = view.findViewById(R.id.textView_nombrePokemon) as TextView
            poderUnoTextView = view.findViewById(R.id.textView_poderEspecialUno) as TextView
            boton_opciones = view.findViewById(R.id.button_opcionesP) as Button

            val layout = view.findViewById(R.id.relative_layout_pokemones) as RelativeLayout


        }


    }

    // Definimos el layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder2 {
        Log.i("debug", "Entro en CreateH")

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.lista_pokemones_rvlayout,
                parent,
                false
            )

        return MyViewHolder2(itemView)
    }

    // Llenamos los datos del layout
    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        val pokemon = listaPokemones[position]
        Log.i("debug", "Entro en BindH")


        Log.i("pokemones", pokemon.nombrePokemon)
        Log.i("pokemones", pokemon.poderEspecialUno.toString())
        holder.nombreTextView.setText(pokemon.nombrePokemon)
        holder.poderUnoTextView.setText(pokemon.poderEspecialUno.toString())

        holder.boton_opciones.setOnClickListener {
            val popup = PopupMenu(contexto, holder.boton_opciones)
            //inflating menu from xml resource
            popup.inflate(R.menu.pokemones_menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.getItemId()) {
                    R.id.editar_pokemon -> {
                        val intentEditar = Intent(contexto, FormularioPokemonActivity::class.java)
                        Log.i(
                            "paso",
                            "${pokemon.nombrePokemon}, ${pokemon.poderEspecialUno}, ${pokemon.poderEspecialDos}, ${pokemon.id}"
                        )
//                        intentEditar.putExtra("id_usuario", persona.id)

                        val materiaActualizar = Pokemon(
                            pokemon.id!!,
                            pokemon.numeroPokemon,
                            pokemon.nombrePokemon,
                            pokemon.poderEspecialUno,
                            pokemon.poderEspecialDos,
                            pokemon.fechaCaptura,
                            pokemon.nivel
                        )

                        intentEditar.putExtra("pokemon_pasar", materiaActualizar)
                        intentEditar.putExtra("entrenador", entrenador)
                        //contexto.startActivityForResult(intentEditar, ListarEntrenadorActivity.requestCodeActualizar)
                        contexto.startActivity(intentEditar)
                        contexto.finish()
                        notifyDataSetChanged()
                        true
                    }
                    R.id.eliminar_pokemon -> {
                        val builder = AlertDialog.Builder(contexto)
                        builder
                            .setMessage("Estas seguro de eliminar el entrenador?")
                            .setPositiveButton(
                                "Si"
                            ) { dialog, which ->
                                PokemonHTTP().eliminar(pokemon.id)
                                Alerter.create(contexto)
                                    .setText("Pokemon ${pokemon.nombrePokemon} eliminada")
                                    .show()
                                listaPokemones.removeAt(position)
                                notifyDataSetChanged()

                            }
                            .setNegativeButton(
                                "No"
                            ) { dialog, which ->
                                Alerter.create(contexto)
                                    .setText("Selecciono que NO")
                                    .show()
                            }


                        val dialogo = builder.create()
                        dialogo.show()


                        true
                    }
                    R.id.compartir_pokemon -> {
                        val texto = pokemon.nombrePokemon

                        val intent = Intent(Intent.ACTION_SEND)

                        intent.type = "text/html"

                        intent.putExtra(Intent.EXTRA_TEXT, texto)

                        contexto.startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

    }
    override fun getItemCount(): Int {
        Log.i("debug", "Entro en Size")

        return listaPokemones.size
    }


}