package com.caos.viajesempresa.domain.model

data class Trip(
    val id: String,
    val name: String,
    val destination: String,
    val startDate: String, // YYYY-MM-DD
    val endDate: String,   // YYYY-MM-DD
    val expenses: List<Expense> = emptyList()
) {
    val totalExpenses: Double
        get() = expenses.sumOf { it.amount }
}

data class Category(
    val id: Int,
    val name: String,
    val description: String
)

data class Expense(
    val id: String,
    val tripId: String,
    val description: String,
    val amount: Double,
    val categoryId: Int,
    val categoryName: String = "",
    val expenseDate: String // YYYY-MM-DD
)