package com.example.shoppinglist

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), StartDragListener {
    //private var itemList = arrayListOf<Item>()
    private var recyclerView: RecyclerView? = null
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var iconSearcher: IconSearcher = IconSearcher()

    private lateinit var viewModel: ItemViewModel
    private lateinit var itemRepository: ItemRepository
    private lateinit var recyclerAdapter: RecyclerAdapter
    private var fabAddItem: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        itemRepository = ItemRepository(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(itemRepository))[ItemViewModel::class.java]

        recyclerView = findViewById(R.id.recyclerView)

        fabAddItem = findViewById(R.id.fabAddItem)
        fabAddItem?.setOnClickListener { view ->
            viewModel.addItem("")

            recyclerView?.post {
                // Scroll the RecyclerView to the top
                recyclerView?.scrollToPosition(0)
            }

            // Use a Handler to delay the focus request
            Handler(Looper.getMainLooper()).postDelayed({
                // Get the ViewHolder at that position
                val viewHolder = recyclerView?.findViewHolderForAdapterPosition(0)

                // If the ViewHolder is found and not null
                if (viewHolder != null) {
                    // Assuming your ViewHolder has an EditText field, get the EditText reference
                    val actv = viewHolder.itemView.findViewById<AutoCompleteTextView>(R.id.actvItemName)
                    // Request focus on the EditText
                    actv.requestFocus()
                    // Show the keyboard
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(actv, InputMethodManager.SHOW_IMPLICIT)
                }
            }, 500) // Adjust the delay as needed
        }

        // Bind the adapter
        setAdapter()

        // Observe the item list from ViewModel
        viewModel.itemList.observe(this, Observer { itemList ->
            recyclerAdapter.safeSubmitList(itemList)
        })

    }

    private fun setAdapter() {
        recyclerAdapter = RecyclerAdapter(
            this,
            arrayListOf(),
            this,
            recyclerView!!,
            iconSearcher,
            viewModel)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)

        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = recyclerAdapter

        // Attach ItemTouchHelper (drag handle)
        val callback = ItemTouchHelperCallback(recyclerAdapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}