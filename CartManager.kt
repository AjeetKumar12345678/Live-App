package com.example.stationery

object CartManager {
    private val cartItems = mutableListOf<StationeryItem>()
    private val orderHistory = mutableListOf<Order>()

    fun addItem(item: StationeryItem) {
        cartItems.add(item)
    }

    fun getItems(): List<StationeryItem> = cartItems

    fun clearCart() {
        cartItems.clear()
    }

    fun getTotalPrice(): Double = cartItems.sumOf { it.price }

    fun getCartCount(): Int = cartItems.size

    fun addOrder(order: Order) {
        orderHistory.add(order)
    }

    fun getOrderHistory(): List<Order> = orderHistory
}

data class Order(
    val id: String,
    val items: List<StationeryItem>,
    val totalAmount: Double,
    val date: String
)
