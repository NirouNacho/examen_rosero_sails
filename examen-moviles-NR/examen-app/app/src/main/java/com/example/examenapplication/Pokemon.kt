package com.example.examenapplication

import android.os.Parcel
import android.os.Parcelable


class Pokemon(var id:Int,
              var numeroPokemon: Long,
              var nombrePokemon: String,
              var poderEspecialUno: String,
              var poderEspecialDos: String,
              var fechaCreacion: String,
              var nivel: Int):Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(numeroPokemon)
        parcel.writeString(nombrePokemon)
        parcel.writeString(poderEspecialUno)
        parcel.writeString(poderEspecialDos)
        parcel.writeString(fechaCreacion)
        parcel.writeInt(nivel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pokemon> {
        override fun createFromParcel(parcel: Parcel): Pokemon {
            return Pokemon(parcel)
        }

        override fun newArray(size: Int): Array<Pokemon?> {
            return arrayOfNulls(size)
        }
    }
}