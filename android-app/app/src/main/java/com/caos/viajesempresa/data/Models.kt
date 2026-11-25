package com.caos.viajesempresa.data

import com.caos.viajesempresa.domain.model.Category
import com.caos.viajesempresa.domain.model.Place
import com.caos.viajesempresa.domain.model.Trip

data class CategoryDto(
    val id: Int,
    val name: String,
    val description: String? = ""
) {
    fun toDomain() = Category(id = id, name = name, description = description ?: "")
}

data class PlaceDto(
    val id: String? = "",
    val tripId: String? = "",
    val name: String,
    val description: String? = "",
    val city: String,
    val categoryId: Int,
    val category: CategoryDto? = null,
    val tentativeDate: String
) {
    fun toDomain() = Place(
        id = id ?: "",
        tripId = tripId ?: "",
        name = name,
        description = description ?: "",
        city = city,
        categoryId = categoryId,
        categoryName = category?.name ?: "",
        tentativeDate = tentativeDate
    )
}

data class TripDto(
    val id: String? = "",
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val places: List<PlaceDto>? = emptyList()
) {
    fun toDomain() = Trip(
        id = id ?: "",
        name = name,
        destination = destination,
        startDate = startDate,
        endDate = endDate,
        places = places?.map { it.toDomain() } ?: emptyList()
    )
}

data class CreateTripRequest(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)

data class CreatePlaceRequest(
    val tripId: String,
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int,
    val tentativeDate: String
)