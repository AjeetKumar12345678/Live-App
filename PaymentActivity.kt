package com.example.stationery

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PaymentActivity : AppCompatActivity() {

    private var selectedMethod = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val btnPay = findViewById<Button>(R.id.btnPayNow)
        val cvUpi = findViewById<MaterialCardView>(R.id.cvUpi)
        val cvCard = findViewById<MaterialCardView>(R.id.cvCard)
        val cvCod = findViewById<MaterialCardView>(R.id.cvCod)

        val total = CartManager.getTotalPrice()
        btnPay.text = "Pay ₹$total"

        cvUpi.setOnClickListener {
            selectMethod("UPI", cvUpi, cvCard, cvCod)
        }

        cvCard.setOnClickListener {
            selectMethod("Card", cvCard, cvUpi, cvCod)
        }

        cvCod.setOnClickListener {
            selectMethod("Cash on Delivery", cvCod, cvUpi, cvCard)
        }

        btnPay.setOnClickListener {
            if (selectedMethod.isEmpty()) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            } else {
                processPayment()
            }
        }
    }

    private fun selectMethod(method: String, selected: MaterialCardView, vararg others: MaterialCardView) {
        selectedMethod = method
        selected.strokeWidth = 5
        selected.setStrokeColor(android.graphics.Color.BLUE)
        
        for (other in others) {
            other.strokeWidth = 0
        }
        
        Toast.makeText(this, "$method selected", Toast.LENGTH_SHORT).show()
    }

    private fun processPayment() {
        val order = Order(
            id = UUID.randomUUID().toString().take(8).uppercase(),
            items = ArrayList(CartManager.getItems()),
            totalAmount = CartManager.getTotalPrice(),
            date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
        )

        CartManager.addOrder(order)
        CartManager.clearCart()

        Toast.makeText(this, "Payment Successful via $selectedMethod! Order ID: ${order.id}", Toast.LENGTH_LONG).show()
        
        val intent = android.content.Intent(this, MainActivity::class.java)
        intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
