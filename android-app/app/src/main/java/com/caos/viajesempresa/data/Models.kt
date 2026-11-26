package com.caos.viajesempresa.data

import com.caos.viajesempresa.domain.model.Category
import com.caos.viajesempresa.domain.model.Expense
import com.caos.viajesempresa.domain.model.Trip

data class CategoryDto(
    val id: Int,
    val name: String,
    val description: String? = ""
) {
    fun toDomain() = Category(id = id, name = name, description = description ?: "")
}

data class PlaceDto(
    val id: String? = "",
    val tripId: String? = "",
    val name: String,
    val description: String? = "",
    val city: String? = "",
    val categoryId: Int,
    val category: CategoryDto? = null,
    val tentativeDate: String
) {
    fun toExpenseDomain(): Expense {
        val parsedAmount = city?.toDoubleOrNull()
            ?: name.replace("$", "").replace(",", "").trim().toDoubleOrNull()
            ?: 0.0
        return Expense(
            id = id ?: "",
            tripId = tripId ?: "",
            description = description?.ifBlank { name } ?: name,
            amount = parsedAmount,
            categoryId = categoryId,
            categoryName = category?.name ?: "",
            expenseDate = tentativeDate
        )
    }
}

data class TripDto(
    val id: String? = "",
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val places: List<PlaceDto>? = emptyList()
) {
    fun toDomain(): Trip {
        val idReal = id ?: ""
        val mappedExpenses = places?.map { it.toExpenseDomain().copy(tripId = idReal) } ?: emptyList()
        return Trip(
            id = idReal,
            name = name,
            destination = destination,
            startDate = startDate,
            endDate = endDate,
            expenses = mappedExpenses
        )
    }
}

data class CreateTripRequest(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)

data class CreatePlaceRequest(
    val tripId: String,
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int,
    val tentativeDate: String
)

interface TripRepository {
    suspend fun getTrips(): Result<List<Trip>>
    suspend fun createTrip(name: String, destination: String, startDate: String, endDate: String): Result<Trip>
    suspend fun getTripDetail(tripId: String): Result<Trip>
    suspend fun getCategories(): Result<List<Category>>
    suspend fun saveExpense(
        tripId: String,
        description: String,
        amount: Double,
        categoryId: Int,
        expenseDate: String
    ): Result<Expense>
}