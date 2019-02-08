package com.example.examenapplication

import android.os.Parcel
import android.os.Parcelable

class Entrenador(var id:Int, var nombres: String, var apellidos: String,
                 var numeroMedallas: Int,
                 var fechaNacimiento: String,
                 var campeonActual: Boolean): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombres)
        parcel.writeString(apellidos)
        parcel.writeInt(numeroMedallas)
        parcel.writeString(fechaNacimiento)
        parcel.writeByte(if (campeonActual) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Entrenador> {
        override fun createFromParcel(parcel: Parcel): Entrenador {
            return Entrenador(parcel)
        }

        override fun newArray(size: Int): Array<Entrenador?> {
            return arrayOfNulls(size)
        }
    }
}

