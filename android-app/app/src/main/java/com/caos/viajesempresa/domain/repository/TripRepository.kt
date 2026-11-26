package com.caos.viajesempresa.domain.repository

import com.caos.viajesempresa.domain.model.Category
import com.caos.viajesempresa.domain.model.Expense
import com.caos.viajesempresa.domain.model.Trip

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