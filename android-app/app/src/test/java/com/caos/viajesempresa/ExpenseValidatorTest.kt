package com.caos.viajesempresa

import com.caos.viajesempresa.domain.model.Expense
import com.caos.viajesempresa.domain.usecase.ExpenseCalculator
import com.caos.viajesempresa.domain.usecase.FormValidation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpenseValidatorTest {

    private val validator = FormValidation()
    private val calculator = ExpenseCalculator()

    @Test
    fun `validar viaje con fechas correctas retorna exito`() {
        val result = validator.validateTrip("Europa 2026", "Italia", "2026-06-10", "2026-06-20")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `validar viaje con fecha fin anterior a inicio falla`() {
        val result = validator.validateTrip("Europa 2026", "Italia", "2026-06-20", "2026-06-10")
        assertTrue(result.isFailure)
    }

    @Test
    fun `validar gasto valido dentro del periodo retorna exito`() {
        val result = validator.validateExpense(
            description = "Transporte aeropuerto",
            amount = 35.0,
            categoryId = 1,
            expenseDate = "2026-06-12",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20"
        )
        assertTrue(result.isSuccess)
    }

    @Test
    fun `validar gasto con valor cero o negativo falla`() {
        val zeroResult = validator.validateExpense("Almuerzo", 0.0, 1, "2026-06-12", "2026-06-10", "2026-06-20")
        val negativeResult = validator.validateExpense("Almuerzo", -15.0, 1, "2026-06-12", "2026-06-10", "2026-06-20")
        assertTrue(zeroResult.isFailure)
        assertTrue(negativeResult.isFailure)
    }

    @Test
    fun `validar gasto fuera del rango de fechas del viaje falla`() {
        val beforeResult = validator.validateExpense("Hotel", 120.0, 1, "2026-06-05", "2026-06-10", "2026-06-20")
        val afterResult = validator.validateExpense("Hotel", 120.0, 1, "2026-06-25", "2026-06-10", "2026-06-20")
        assertTrue(beforeResult.isFailure)
        assertTrue(afterResult.isFailure)
    }

    @Test
    fun `calcular total suma solo los gastos del viaje seleccionado`() {
        val expenses = listOf(
            Expense("1", "trip-1", "Vuelo", 500.0, 1, "Transporte", "2026-06-10"),
            Expense("2", "trip-1", "Hotel", 300.0, 2, "Alojamiento", "2026-06-11"),
            Expense("3", "trip-2", "Taxi otro viaje", 90.0, 1, "Transporte", "2026-07-01")
        )
        val total = calculator.calculateTotal(expenses, "trip-1")
        assertEquals(800.0, total, 0.001)
    }
}