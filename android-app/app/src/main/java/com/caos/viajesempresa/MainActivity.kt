package com.caos.viajesempresa

import com.caos.viajesempresa.domain.repository.TripRepository
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caos.viajesempresa.data.RetrofitClient
import com.caos.viajesempresa.data.TripRepositoryImpl
import com.caos.viajesempresa.ui.theme.screens.CreateTripScreen
import com.caos.viajesempresa.ui.theme.screens.RegisterExpenseScreen
import com.caos.viajesempresa.ui.theme.screens.TripDetailScreen
import com.caos.viajesempresa.ui.theme.screens.TripListScreen
import com.caos.viajesempresa.ui.theme.viewmodel.ViajeViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = TripRepositoryImpl(RetrofitClient.apiService)

        setContent {
            val navController = rememberNavController()
            val viewModel: ViajeViewModel = viewModel(
                factory = ViajeViewModel.Factory(repository)
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            LaunchedEffect(uiState.errorMessage) {
                uiState.errorMessage?.let { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }

            NavHost(navController = navController, startDestination = "trip_list") {
                composable("trip_list") {
                    TripListScreen(
                        uiState = uiState,
                        onCreateTripClick = { navController.navigate("create_trip") },
                        onTripClick = { tripId ->
                            viewModel.loadTripDetail(tripId)
                            navController.navigate("trip_detail")
                        }
                    )
                }
                composable("create_trip") {
                    CreateTripScreen(
                        isLoading = uiState.isLoading,
                        onBack = { navController.popBackStack() },
                        onSave = { name, destination, startDate, endDate ->
                            viewModel.saveTrip(name, destination, startDate, endDate) {
                                navController.popBackStack()
                            }
                        }
                    )
                }
                composable("trip_detail") {
                    TripDetailScreen(
                        uiState = uiState,
                        onBack = { navController.popBackStack() },
                        onRegisterExpenseClick = { navController.navigate("register_expense") }
                    )
                }
                composable("register_expense") {
                    RegisterExpenseScreen(
                        categories = uiState.categories,
                        isLoading = uiState.isLoading,
                        onBack = { navController.popBackStack() },
                        onSave = { description, amount, categoryId, expenseDate ->
                            viewModel.saveExpense(description, amount, categoryId, expenseDate) {
                                navController.popBackStack()
                            }
                        }
                    )
                }
            }
        }
    }
}