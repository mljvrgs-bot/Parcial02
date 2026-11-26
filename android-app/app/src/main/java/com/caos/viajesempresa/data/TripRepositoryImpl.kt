package com.caos.viajesempresa.data

import com.caos.viajesempresa.domain.model.Category
import com.caos.viajesempresa.domain.model.Expense
import com.caos.viajesempresa.domain.model.Trip
import com.caos.viajesempresa.domain.repository.TripRepository

class TripRepositoryImpl(private val api: ViajeApiService) : TripRepository {

    override suspend fun getTrips(): Result<List<Trip>> = runCatching {
        val response = api.getTrips()
        if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else {
            throw Exception("Error ${response.code()} al consultar viajes")
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
            throw Exception("Error ${response.code()} al crear viaje")
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

    override suspend fun saveExpense(
        tripId: String,
        description: String,
        amount: Double,
        categoryId: Int,
        expenseDate: String
    ): Result<Expense> = runCatching {
        val request = CreatePlaceRequest(
            tripId = tripId,
            name = description,
            description = description,
            city = amount.toString(),
            categoryId = categoryId,
            tentativeDate = expenseDate
        )
        val response = api.createPlace(request)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toExpenseDomain()
        } else {
            throw Exception("Error ${response.code()} al registrar el gasto")
        }
    }
}