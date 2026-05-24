package com.tecsup.lab10.data

import retrofit2.Response
import retrofit2.http.*

data class SeriesResponse(
    val total: Int,
    val series: ArrayList<SerieModel>
)

interface SerieApiService {

    @GET("serie")
    suspend fun selectSeries(): SeriesResponse

    @GET("serie/{id}")
    suspend fun selectSerie(@Path("id") id: String): Response<SerieModel>

    @Headers("Content-Type: application/json")
    @POST("serie")
    suspend fun insertSerie(@Body serie: SerieModel): Response<SerieModel>

    @PUT("serie/{id}")
    suspend fun updateSerie(@Path("id") id: String, @Body serie: SerieModel): Response<SerieModel>

    @DELETE("serie/{id}")
    suspend fun deleteSerie(@Path("id") id: String): Response<SerieModel>
}