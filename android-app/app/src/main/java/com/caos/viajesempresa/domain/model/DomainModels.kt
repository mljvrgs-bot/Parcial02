package com.caos.viajesempresa.domain.model

data class Trip(
    val id: String,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val places: List<Place> = emptyList()
)

data class Category(
    val id: Int,
    val name: String,
    val description: String
)

data class Place(
    val id: String,
    val tripId: String,
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int,
    val categoryName: String = "",
    val tentativeDate: String
)