package com.caos.viajesempresa.data

import com.caos.viajesempresa.domain.model.Category
import com.caos.viajesempresa.domain.model.Place
import com.caos.viajesempresa.domain.model.Trip
import com.caos.viajesempresa.domain.repository.ViajesRepository

class ViajesRepository(private val api: ViajeApiService) : ViajesRepository {

    override suspend fun getTrips(): Result<List<Trip>> = runCatching {
        val response = api.getTrips()
        if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else {
            throw Exception("Error ${response.code()} al cargar viajes")
        }
    }

    override suspend fun createTrip(
        name: String,
        destination: String,
        startDate: String,
        endDate: String
    ): Result<Trip> = runCatching {
        val response = api.createTrip(CreateTripRequest(name, destination, startDate, endDate))
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            throw Exception("Error ${response.code()} al guardar viaje")
        }
    }

    override suspend fun getTripDetail(tripId: String): Result<Trip> = runCatching {
        val response = api.getTripById(tripId)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            throw Exception("No se encontró el detalle del viaje")
        }
    }

    override suspend fun getCategories(): Result<List<Category>> = runCatching {
        val response = api.getCategories()
        if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else {
            throw Exception("Error ${response.code()} al cargar categorías")
        }
    }

    override suspend fun addPlace(
        tripId: String,
        name: String,
        description: String,
        city: String,
        categoryId: Int,
        tentativeDate: String
    ): Result<Place> = runCatching {
        val response = api.createPlace(
            CreatePlaceRequest(tripId, name, description, city, categoryId, tentativeDate)
        )
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            throw Exception("Error ${response.code()} al registrar lugar")
        }
    }
}