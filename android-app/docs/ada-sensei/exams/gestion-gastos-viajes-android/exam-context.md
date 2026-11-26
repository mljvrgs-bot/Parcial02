# Exam context

- Exam id: `gestion-gastos-viajes-android`
- Version: `1.0`
- Rubric: `unified-android:v2`
- Statement source: `C:\Users\Maria\StudioProjects\Parcial02\gestion-gastos-viajes-parcial.examV2.json`
- Statement format: `json`

## Functional requirements

| id | requirement | weight | evidence expected |
|---|---|---:|---|
| `TRIP-LIST-001` | La aplicación muestra el listado de viajes registrados. Cada elemento presenta, como mínimo, el nombre y el destino del viaje. | 0.1 | TripListScreen, MyTripsScreen, TripViewModel, trip list, TripCard, TripItem, trip.name, trip.destination, TripRepository, TripDao, TripDataSource |
| `TRIP-CREATE-001` | La aplicación permite registrar un viaje mediante un formulario con nombre, destino, fecha de inicio y fecha de finalización. | 0.15 | CreateTripScreen, TripForm, name, destination, DestinationField, DatePicker, startDate, endDate, saveTrip, TripRepository, TripDao, CreateTripUseCase |
| `TRIP-DETAIL-001` | La aplicación permite seleccionar un viaje y consultar su información general junto con el resumen de los gastos registrados para ese viaje. | 0.1 | TripListScreen, navigation, TripDetailScreen, TripDetailScreen, trip.name, destination, startDate, endDate, totalExpenses, ExpenseSummary, TripDetailViewModel, tripId, ExpenseRepository, filterByTrip |
| `EXPENSE-LIST-001` | La aplicación permite consultar el listado de gastos asociados al viaje seleccionado. Cada gasto muestra categoría, descripción, fecha y valor. | 0.15 | ExpenseListScreen, ExpenseList, ExpenseRepository, tripId, ExpenseItem, description, amount, expenseDate, category, categoryName, tripId, filterByTrip, ExpenseDao |
| `EXPENSE-CREATE-001` | La aplicación permite registrar un gasto asociado a un viaje mediante un formulario que solicita descripción, valor, categoría y fecha del gasto. | 0.2 | RegisterExpenseScreen, ExpenseForm, tripId, description, DescriptionField, amount, value, AmountField, CategoryDropdown, CategorySelector, categoryId, expenseDate, DatePicker, saveExpense, ExpenseRepository, ExpenseDao, tripId |
| `CATEGORY-LIST-001` | La aplicación permite consultar las categorías disponibles para clasificar los gastos y ofrece esas categorías como opciones al registrar un gasto. | 0.05 | CategoryRepository, CategoryDao, CategoryViewModel, getCategories, CategoryDropdown, DropdownMenu, CategorySelector, categoryId, Expense, selectedCategory |
| `EXPENSE-TOTAL-001` | La aplicación calcula y muestra la suma de los valores de los gastos registrados para cada viaje. | 0.1 | calculateTotal, sumOf, ExpenseRepository, ExpenseCalculator, ExpenseSummary, totalExpenses, TripDetailScreen, Flow, observeExpenses, tripId, total recalculation |
| `EXPENSE-VALID-001` | La aplicación valida los campos obligatorios de viajes y gastos, exige que el valor del gasto sea positivo y verifica que la fecha del gasto se encuentre dentro del período del viaje. | 0.15 | validateTrip, FormValidation, required fields, validateExpense, FormValidation, required fields, amount > 0, validateAmount, ExpenseValidatorTest, expenseDate, startDate, date validation, expenseDate, endDate, date validation, startDate, endDate, date range validation, validation message, error state, saveExpense, saveTrip |
| `EXPENSE-MVVM-001` | La solución separa la interfaz, el estado de presentación y la lógica de negocio mediante responsabilidades compatibles con MVVM. El cálculo de totales y las validaciones no deben estar implementados exclusivamente dentro de los componentes visuales. | 0 | Screen, ViewModel, Model, Repository, UseCase, ViewModel, android.view, Activity, @Composable, domain, ExpenseCalculator, ExpenseValidator, UseCase |
| `EXPENSE-TEST-001` | El proyecto incluye pruebas unitarias ejecutables para verificar las operaciones de viajes y gastos, el cálculo de totales y las reglas de validación. | 0 | src/test, JUnit, ExpenseViewModelTest, ExpenseRepositoryTest, ExpenseValidatorTest, amount tests, date range tests, ExpenseCalculatorTest, calculateTotal, tripId, ./gradlew test, test reports, JUnit XML |

Original instructor statement: `C:\Users\Maria\StudioProjects\Parcial02\gestion-gastos-viajes-parcial.examV2.json`

<!-- generated-by: ada-sensei -->
