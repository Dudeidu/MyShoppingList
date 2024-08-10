package com.example.shoppinglist

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity(), StartDragListener {
    private var itemList = arrayListOf<Item>()
    private var recyclerView: RecyclerView? = null
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var iconSearcher: IconSearcher = IconSearcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        // Example items

        addItem("Apples")
        addItem("Eggs")
        addItem("Lettuce")
        addItem("sdvsdv")

        // Bind the adapter
        setAdapter()

    }

    private fun setAdapter() {
        val adapter = RecyclerAdapter(
            this,
            itemList,
            this,
            recyclerView!!,
            iconSearcher)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)

        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter

        // Attach ItemTouchHelper (drag handle)
        val callback = ItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private fun addItem(name: String) {
        val item = Item(name)

        // Search Icon by name
        item.iconResource = iconSearcher.findIconByQuery(name)

        itemList.add(item)

    }



}