package com.example.stationery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class StationeryAdapter(
    private var items: List<StationeryItem>,
    private val onAddToCart: (StationeryItem) -> Unit
) : RecyclerView.Adapter<StationeryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivItemImage)
        val tvName: TextView = view.findViewById(R.id.tvItemName)
        val tvCategory: TextView = view.findViewById(R.id.tvItemCategory)
        val tvPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val btnAdd: Button = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stationery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvCategory.text = item.category
        holder.tvPrice.text = "₹${item.price}"
        
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .transform(RoundedCorners(16))
            .into(holder.ivImage)

        holder.btnAdd.setOnClickListener { onAddToCart(item) }
    }

    override fun getItemCount() = items.size

    fun updateList(newList: List<StationeryItem>) {
        items = newList
        notifyDataSetChanged()
    }
}
