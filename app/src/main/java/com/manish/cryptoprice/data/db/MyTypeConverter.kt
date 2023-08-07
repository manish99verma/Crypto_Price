package com.manish.cryptoprice.data.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manish.cryptoprice.data.model.coinsList.Roi
import com.manish.cryptoprice.data.model.description.Description
import com.manish.cryptoprice.data.model.description.Image


@TypeConverters
class MyTypeConverter {
    @TypeConverter
    fun listOfListOfDoubleToStringConverter(list: List<List<Double>>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun listOfListOfDoubleFromStringConverter(json: String): List<List<Double>> {
        val listOfMyClassObject = object : TypeToken<List<List<Double?>?>?>() {}.type
        return gson.fromJson(json, listOfMyClassObject)
    }

    @TypeConverter
    fun roiToString(roi: Roi?): String {
        return gson.toJson(roi)
    }

    @TypeConverter
    fun roiFromString(json: String): Roi? {
        val type = object : TypeToken<Roi?>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun descriptionToString(desc: Description): String {
        return gson.toJson(desc)
    }

    @TypeConverter
    fun descriptionFromString(json: String): Description {
        val type = object : TypeToken<Description?>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun imageToString(image: Image): String {
        return gson.toJson(image)
    }

    @TypeConverter
    fun imageFromString(json: String): Image {
        val type = object : TypeToken<Image?>() {}.type
        return gson.fromJson(json, type)
    }

    companion object {
        private val gson = Gson()
    }
}