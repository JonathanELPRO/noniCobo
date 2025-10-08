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
