# Gestión de Viajes y Gastos — Android App

Aplicación móvil nativa para Android desarrollada en **Kotlin** con **Jetpack Compose**, orientada al registro, administración y seguimiento de viajes y sus gastos asociados.

**Autora:** María Alejandra Vargas Acosta  
**GitHub:** [@mljvrgs-bot](https://github.com/mljvrgs-bot)

---

## Características principales

* **Gestión de viajes:** Creación, consulta y edición de viajes con fechas y presupuesto asociado.
* **Control de gastos:** Registro categorizado de gastos por viaje y cálculo automático de totales y saldos.
* **Interfaz moderna:** Pantallas construidas de forma declarativa con Jetpack Compose y Material Design 3.
* **Arquitectura limpia:** Separación de responsabilidades mediante patrón MVVM (`ViewModel` + `StateFlow`) y persistencia local.

---

## Tecnologías utilizadas

* **Lenguaje:** Kotlin
* **UI:** Jetpack Compose (Material 3)
* **Arquitectura:** MVVM (`ViewModel`, `StateFlow`)
* **Build System:** Gradle (Kotlin DSL)
* **Calidad y pruebas:** JUnit, Android Lint, Detekt, KtLint

---

## Estructura del proyecto

```text
android-app/
├── app/
│   └── src/
│       ├── main/        # Código fuente (UI Compose, ViewModels, modelos y repositorios)
│       └── test/        # Pruebas unitarias
└── docs/
    └── ada-sensei/
        └── exams/
            └── gestion-gastos-viajes-android/
                └── report.pdf   # Reporte de evaluación del parcial
