package com.caos.viajesempresa.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ViajeApiService {

    @GET("api/trips")
    suspend fun getTrips(): Response<List<TripDto>>

    @POST("api/trips")
    suspend fun createTrip(@Body request: CreateTripRequest): Response<TripDto>

    @GET("api/trips/{id}")
    suspend fun getTripById(@Path("id") tripId: String): Response<TripDto>

    @GET("api/categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @POST("api/places")
    suspend fun createPlace(@Body request: CreatePlaceRequest): Response<PlaceDto>
}