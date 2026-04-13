package com.example.stationery

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: StationeryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setupRecyclerView()
        updateTotal()

        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            if (CartManager.getCartCount() > 0) {
                startActivity(Intent(this, PaymentActivity::class.java))
            } else {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        val rv = findViewById<RecyclerView>(R.id.rvCartItems)
        adapter = StationeryAdapter(CartManager.getItems()) { _ ->
            // In a real app, maybe remove from cart?
            Toast.makeText(this, "Item already in cart", Toast.LENGTH_SHORT).show()
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun updateTotal() {
        val total = CartManager.getTotalPrice()
        findViewById<TextView>(R.id.tvTotalPrice).text = "₹$total"
    }
}
