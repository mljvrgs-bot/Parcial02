package com.caos.viajesempresa.domain.usecase

import com.caos.viajesempresa.domain.model.Category
import com.caos.viajesempresa.domain.model.Expense
import com.caos.viajesempresa.domain.model.Trip
import com.caos.viajesempresa.domain.repository.TripRepository
import java.time.LocalDate
import java.time.format.DateTimeParseException

class ExpenseCalculator {
    fun calculateTotal(expenses: List<Expense>, tripId: String): Double {
        return expenses
            .filter { it.tripId == tripId }
            .sumOf { it.amount }
    }
}

class FormValidation {
    fun validateTrip(
        name: String,
        destination: String,
        startDate: String,
        endDate: String
    ): Result<Unit> {
        if (name.isBlank() || destination.isBlank() || startDate.isBlank() || endDate.isBlank()) {
            return Result.failure(IllegalArgumentException("Todos los campos del viaje son obligatorios"))
        }
        return try {
            val start = LocalDate.parse(startDate.trim())
            val end = LocalDate.parse(endDate.trim())
            if (end.isBefore(start)) {
                Result.failure(IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio"))
            } else {
                Result.success(Unit)
            }
        } catch (e: DateTimeParseException) {
            Result.failure(IllegalArgumentException("Formato de fecha inválido. Usa YYYY-MM-DD"))
        }
    }

    fun validateExpense(
        description: String,
        amount: Double?,
        categoryId: Int?,
        expenseDate: String,
        tripStartDate: String,
        tripEndDate: String
    ): Result<Unit> {
        if (description.isBlank() || amount == null || categoryId == null || expenseDate.isBlank()) {
            return Result.failure(IllegalArgumentException("Todos los campos del gasto son obligatorios"))
        }
        if (amount <= 0.0) {
            return Result.failure(IllegalArgumentException("El valor del gasto debe ser mayor a cero"))
        }
        return try {
            val date = LocalDate.parse(expenseDate.trim())
            val start = LocalDate.parse(tripStartDate.trim())
            val end = LocalDate.parse(tripEndDate.trim())

            if (date.isBefore(start) || date.isAfter(end)) {
                Result.failure(
                    IllegalArgumentException("La fecha del gasto debe estar entre $tripStartDate y $tripEndDate")
                )
            } else {
                Result.success(Unit)
            }
        } catch (e: DateTimeParseException) {
            Result.failure(IllegalArgumentException("Formato de fecha inválido. Usa YYYY-MM-DD"))
        }
    }
}

class GetTripsUseCase(private val repository: TripRepository) {
    suspend operator fun invoke(): Result<List<Trip>> = repository.getTrips()
}

class CreateTripUseCase(
    private val repository: TripRepository,
    private val validator: FormValidation = FormValidation()
) {
    suspend operator fun invoke(
        name: String,
        destination: String,
        startDate: String,
        endDate: String
    ): Result<Trip> {
        validator.validateTrip(name, destination, startDate, endDate).onFailure { return Result.failure(it) }
        return repository.createTrip(name.trim(), destination.trim(), startDate.trim(), endDate.trim())
    }
}

class GetTripDetailUseCase(private val repository: TripRepository) {
    suspend operator fun invoke(tripId: String): Result<Trip> = repository.getTripDetail(tripId)
}

class GetCategoriesUseCase(private val repository: TripRepository) {
    suspend operator fun invoke(): Result<List<Category>> = repository.getCategories()
}

class SaveExpenseUseCase(
    private val repository: TripRepository,
    private val validator: FormValidation = FormValidation()
) {
    suspend operator fun invoke(
        trip: Trip,
        description: String,
        amount: Double?,
        categoryId: Int?,
        expenseDate: String
    ): Result<Expense> {
        validator.validateExpense(
            description = description,
            amount = amount,
            categoryId = categoryId,
            expenseDate = expenseDate,
            tripStartDate = trip.startDate,
            tripEndDate = trip.endDate
        ).onFailure { return Result.failure(it) }

        return repository.saveExpense(
            tripId = trip.id,
            description = description.trim(),
            amount = amount!!,
            categoryId = categoryId!!,
            expenseDate = expenseDate.trim()
        )
    }
}