package com.tecsup.lab10.data

import com.google.gson.annotations.SerializedName

data class SerieModel(
    @SerializedName("id")           var id: Int,
    @SerializedName("nombre")       var name: String,
    @SerializedName("fecha_estreno") var release_date: String,
    @SerializedName("puntuacion")   var rating: Int,
    @SerializedName("categoria")    var category: String
)