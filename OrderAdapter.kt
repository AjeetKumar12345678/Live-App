package com.example.stationery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId: TextView = view.findViewById(R.id.tvOrderId)
        val tvDate: TextView = view.findViewById(R.id.tvOrderDate)
        val tvItems: TextView = view.findViewById(R.id.tvOrderItems)
        val tvTotal: TextView = view.findViewById(R.id.tvOrderTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.tvId.text = "Order #${order.id}"
        holder.tvDate.text = order.date
        holder.tvTotal.text = "Total: ₹${order.totalAmount}"
        
        val itemsNames = order.items.joinToString(", ") { it.name }
        holder.tvItems.text = "Items: $itemsNames"
    }

    override fun getItemCount() = orders.size
}
