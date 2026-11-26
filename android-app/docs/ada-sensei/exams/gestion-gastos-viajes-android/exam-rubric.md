# Exam rubric

Rubric `unified-android:v2` is applied by the shared rubric engine from `006-evaluate`. ADA Sensei does not invent weights.

| dimension | weight | description |
|---|---:|---|
| `functional_requirements` | 40% | Exercise functional requirements |
| `tests` | 20% | Unit tests and requirement-to-test traceability |
| `code_quality` | 15% | Detekt, Android Lint and KtLint findings |
| `mvvm` | 15% | D4 arquitectura — Clean Architecture 50% + MVVM 50% |
| `android_practices` | 10% | Buenas prácticas Android |

### `tests` subcriteria

- `T1` — 50% of `tests` (10% of the exam): Requisitos con al menos una prueba unitaria asociada / total de requisitos funcionales
- `T2` — 35% of `tests` (7% of the exam): Pruebas aprobadas / (aprobadas + fallidas); errores de infraestructura se excluyen
- `T3` — 15% of `tests` (3% of the exam): Cada asociación debe apuntar a test real y requisito existente; se puntúa la proporción de asociaciones válidas

### `code_quality` subcriteria

- `detekt` — 40% of `code_quality` (6% of the exam): Complejidad, code smells, tamaño de métodos/clases y reglas Kotlin
- `android_lint` — 35% of `code_quality` (5% of the exam): Problemas estáticos Android, recursos, manifest, APIs, seguridad, accesibilidad y rendimiento
- `ktlint` — 25% of `code_quality` (4% of the exam): Convenciones y formato del código Kotlin

### `mvvm` subcriteria

- `C1` — 7% of `mvvm` (1% of the exam): Presentation, Domain and Data (or equivalent) responsibilities are separated by real code, not by folder names
- `C2` — 7% of `mvvm` (1% of the exam): Dependencies point toward the domain core; infrastructure does not leak into domain contracts
- `C3` — 7% of `mvvm` (1% of the exam): Domain code is independent of Android, Compose, Firebase and other frameworks
- `C4` — 7% of `mvvm` (1% of the exam): Repository contracts live in inner layers and implementations live in infrastructure
- `C5` — 7% of `mvvm` (1% of the exam): Use cases have justified responsibilities instead of artificial wrappers
- `C6` — 7% of `mvvm` (1% of the exam): Dependency inversion and injection are used where substitutability matters
- `C7` — 7% of `mvvm` (1% of the exam): Domain logic can be tested without Android or infrastructure
- `M1` — 8% of `mvvm` (1% of the exam): View/Composable renders state and emits events
- `M2` — 8% of `mvvm` (1% of the exam): ViewModel exposes observable state and coordinates actions
- `M3` — 8% of `mvvm` (1% of the exam): ViewModel delegates business rules and data access
- `M4` — 8% of `mvvm` (1% of the exam): ViewModel does not depend on Activity, View, UI Context or composables
- `M5` — 8% of `mvvm` (1% of the exam): State/event flow is unidirectional and internally consistent
- `M6` — 8% of `mvvm` (1% of the exam): Dependencies are substitutable and ViewModel tests exist

### `android_practices` subcriteria

- `A1` — 15% of `android_practices` (2% of the exam): Uso correcto de lifecycle; ViewModel asociado al ciclo de vida; ausencia de referencias a Activity/Context retenidas indebidamente; manejo de estado ante recreación
- `A2` — 20% of `android_practices` (2% of the exam): State hoisting; flujo unidireccional; estado observable; composables con responsabilidades acotadas; efectos (`LaunchedEffect`, `DisposableEffect`) usados con claves apropiadas
- `A3` — 10% of `android_practices` (1% of the exam): Strings/dimensiones/colores externalizados; manifest coherente; configuración por variantes; soporte de configuración/localización
- `A4` — 15% of `android_practices` (2% of the exam): Coroutines/Flow apropiados; `viewModelScope`/lifecycle-aware collection; cancelación; no bloquear main thread
- `A5` — 15% of `android_practices` (2% of the exam): Permisos mínimos; runtime permissions cuando aplican; no secretos hardcoded; almacenamiento y comunicaciones seguras; validación de entradas
- `A6` — 15% of `android_practices` (2% of the exam): Content descriptions; contraste; escalado de texto; targets táctiles; layouts adaptables; navegación accesible
- `A7` — 10% of `android_practices` (1% of the exam): minSdk/targetSdk coherentes; APIs disponibles para SDK objetivo; manejo de deprecated APIs; compatibilidad de dependencias

The raw score is computed as `50 × Σ(weight × score / 4) / Σ(weight of evaluated dimensions)`, and the suggested grade is `round(1.0 + (raw_score / 50) × 4.0, 1)`.

Dimensions without sufficient evidence are marked *not evaluated* and leave the denominator. They never count as a pass.

<!-- generated-by: ada-sensei -->
