package com.calyrsoft.ucbp1.features.lodging.data.database.entity


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomOption
import com.calyrsoft.ucbp1.features.lodging.domain.model.StayOption

class Converters {

    private val gson = Gson()

    // --- StayOptions ---
    @TypeConverter
    fun fromStayOptions(list: List<StayOption>?): String {
        return gson.toJson(list ?: emptyList<StayOption>())
    }

    @TypeConverter
    fun toStayOptions(json: String?): List<StayOption> {
        if (json.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<StayOption>>() {}.type
        //Cuando usamos Gson, normalmente puedes hacer esto:
        //
        //val persona = gson.fromJson(json, Persona::class.java)
        //Eso funciona porque Persona no es genérico.
        //
        //Pero… si tienes algo como esto 👇:
        //
        //val lista = gson.fromJson(json, List<Persona>::class.java)
        //
        //
        //Esto no funciona como esperas. Porque en tiempo de ejecución, Kotlin y Java pierden la
        // información del tipo genérico (Persona).
        //Esto se llama type erasure (borrado de tipos).
        // val type = object : TypeToken<List<StayOption>>() {}.type
        //Traducción: “crea una clase sin nombre y sin implementacion que hereda de TypeToken<List<StayOption>>
        // y con type le digo: al construirla, captura el parámetro genérico List<StayOption>”.


        return gson.fromJson(json, type)
    }

    // --- RoomOptions ---
    @TypeConverter
    fun fromRoomOptions(list: List<RoomOption>?): String {
        return gson.toJson(list ?: emptyList<RoomOption>())
    }

    @TypeConverter
    fun toRoomOptions(json: String?): List<RoomOption> {
        if (json.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<RoomOption>>() {}.type
        return gson.fromJson(json, type)
    }


}
