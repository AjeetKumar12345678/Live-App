package com.example.stationery

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: StationeryAdapter
    private val allItems = mutableListOf<StationeryItem>()
    private var currentSearchQuery = ""
    private var currentCategory = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupData()
        setupRecyclerView()
        setupSearch()
        setupCategories()
        setupCartFab()
        
        // Add History Button to Toolbar
        findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar).apply {
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_history) {
                    startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
                    true
                } else false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateCartCount()
    }

    private fun setupData() {
        allItems.clear()
        allItems.addAll(listOf(
            StationeryItem(1, "Premium Fountain Pen", 1250.0, "Writing", "Elegant pen with smooth ink flow", "https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=500&q=80"),
            StationeryItem(2, "Leather Journal", 850.0, "Notebooks", "Handcrafted A5 journal", "https://images.unsplash.com/photo-1544816155-12df9643f363?w=500&q=80"),
            StationeryItem(3, "Watercolor Set (24)", 1500.0, "Art Supplies", "Professional grade pigments", "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=500&q=80"),
            StationeryItem(4, "Mechanical Pencil", 150.0, "Writing", "0.5mm lead with eraser", "https://images.unsplash.com/photo-1518674660708-0e2c0473e68e?w=500&q=80"),
            StationeryItem(5, "A4 Sketchbook", 450.0, "Art Supplies", "180 GSM acid-free paper", "https://images.unsplash.com/photo-1544816155-12df9643f363?w=500&q=80"),
            StationeryItem(6, "Highlighter Set", 300.0, "Writing", "Set of 6 neon colors", "https://images.unsplash.com/photo-1596272875729-ed2ff7d6d9c5?w=500&q=80"),
            StationeryItem(7, "Sticky Notes", 120.0, "Office", "Pack of 4 colors", "https://images.unsplash.com/photo-1603484477859-abe6a73f9366?w=500&q=80"),
            StationeryItem(8, "Geometric Compass Box", 550.0, "Math Tools", "All-in-one precision tools", "https://images.unsplash.com/photo-1516962080544-eac695c93791?w=500&q=80"),
            StationeryItem(9, "Hardcover Fiction Book", 499.0, "Books", "Bestselling mystery novel", "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500&q=80"),
            StationeryItem(10, "Premium Rollerball Pen", 950.0, "Writing", "Smooth signature pen", "https://images.unsplash.com/photo-1511556820780-d912e42b4980?w=500&q=80"),
            StationeryItem(11, "Ball Pen Set (Pack of 10)", 100.0, "Writing", "Blue and black mixed ink", "https://images.unsplash.com/photo-1562564055-71e051d33c19?w=500&q=80"),
            StationeryItem(12, "Executive Daily Diary", 650.0, "Notebooks", "2024 Planner with leather cover", "https://images.unsplash.com/photo-1531346878377-a5be20888e57?w=500&q=80"),
            StationeryItem(13, "Account Register (Large)", 250.0, "Office", "200 pages long register", "https://images.unsplash.com/photo-1517842645767-c639042777db?w=500&q=80"),
            StationeryItem(14, "Biology Disection Kit", 1200.0, "Lab Tools", "Professional 12-piece stainless steel kit", "https://images.unsplash.com/photo-1581093588401-fbb62a02f120?w=500&q=80")
        ))
    }

    private fun setupRecyclerView() {
        val rv = findViewById<RecyclerView>(R.id.rvStationery)
        adapter = StationeryAdapter(allItems) { item ->
            addToCart(item)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun setupSearch() {
        val etSearch = findViewById<EditText>(R.id.etSearch)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s.toString()
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupCategories() {
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupCategories)
        chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            currentCategory = when (checkedIds.firstOrNull()) {
                R.id.chipWriting -> "Writing"
                R.id.chipArt -> "Art Supplies"
                R.id.chipOffice -> "Office"
                else -> "All"
            }
            applyFilters()
        }
    }

    private fun applyFilters() {
        val filtered = allItems.filter { item ->
            val matchesSearch = item.name.contains(currentSearchQuery, ignoreCase = true) || 
                               item.category.contains(currentSearchQuery, ignoreCase = true)
            val matchesCategory = if (currentCategory == "All") true else item.category == currentCategory
            
            matchesSearch && matchesCategory
        }
        
        val tvEmptyState = findViewById<TextView>(R.id.tvEmptyState)
        tvEmptyState.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        
        adapter.updateList(filtered)
    }

    private fun setupCartFab() {
        val fab = findViewById<ExtendedFloatingActionButton>(R.id.fabCart)
        fab.setOnClickListener {
            if (CartManager.getCartCount() > 0) {
                startActivity(Intent(this, CartActivity::class.java))
            } else {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            }
        }
        updateCartCount()
    }

    private fun addToCart(item: StationeryItem) {
        CartManager.addItem(item)
        updateCartCount()
        Toast.makeText(this, "${item.name} added to cart", Toast.LENGTH_SHORT).show()
    }

    private fun updateCartCount() {
        val count = CartManager.getCartCount()
        findViewById<ExtendedFloatingActionButton>(R.id.fabCart).text = "Cart ($count)"
    }
}
