package com.caos.viajesempresa.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.caos.viajesempresa.domain.model.Category
import com.caos.viajesempresa.domain.model.Trip
import com.caos.viajesempresa.domain.repository.TripRepository
import com.caos.viajesempresa.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TripUiState(
    val trips: List<Trip> = emptyList(),
    val selectedTrip: Trip? = null,
    val totalExpenses: Double = 0.0,
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ViajeViewModel(
    private val getTripsUseCase: GetTripsUseCase,
    private val createTripUseCase: CreateTripUseCase,
    private val getTripDetailUseCase: GetTripDetailUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val saveExpenseUseCase: SaveExpenseUseCase,
    private val expenseCalculator: ExpenseCalculator = ExpenseCalculator()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    init {
        loadTrips()
        loadCategories()
    }

    fun loadTrips() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getTripsUseCase()
                .onSuccess { list -> _uiState.update { it.copy(trips = list, isLoading = false) } }
                .onFailure { err -> _uiState.update { it.copy(isLoading = false, errorMessage = err.message) } }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .onSuccess { cats -> _uiState.update { it.copy(categories = cats) } }
        }
    }

    fun saveTrip(
        name: String,
        destination: String,
        startDate: String,
        endDate: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            createTripUseCase(name, destination, startDate, endDate)
                .onSuccess {
                    loadTrips()
                    onSuccess()
                }
                .onFailure { err ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = err.message) }
                }
        }
    }

    fun loadTripDetail(tripId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getTripDetailUseCase(tripId)
                .onSuccess { detail ->
                    val total = expenseCalculator.calculateTotal(detail.expenses, detail.id)
                    _uiState.update {
                        it.copy(
                            selectedTrip = detail,
                            totalExpenses = total,
                            isLoading = false
                        )
                    }
                }
                .onFailure { err ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = err.message) }
                }
        }
    }

    fun saveExpense(
        description: String,
        amountText: String,
        categoryId: Int?,
        expenseDate: String,
        onSuccess: () -> Unit
    ) {
        val currentTrip = _uiState.value.selectedTrip ?: return
        val amount = amountText.trim().toDoubleOrNull()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            saveExpenseUseCase(currentTrip, description, amount, categoryId, expenseDate)
                .onSuccess {
                    loadTripDetail(currentTrip.id)
                    onSuccess()
                }
                .onFailure { err ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = err.message) }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val repository: TripRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViajeViewModel(
                getTripsUseCase = GetTripsUseCase(repository),
                createTripUseCase = CreateTripUseCase(repository),
                getTripDetailUseCase = GetTripDetailUseCase(repository),
                getCategoriesUseCase = GetCategoriesUseCase(repository),
                saveExpenseUseCase = SaveExpenseUseCase(repository)
            ) as T
        }
    }
}