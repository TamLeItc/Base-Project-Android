package fxc.dev.core.data.source.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayListToString(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}