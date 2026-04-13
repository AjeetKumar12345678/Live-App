package com.example.stationery

data class StationeryItem(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val description: String,
    val imageUrl: String // We'll use placeholder colors/icons for now
)
