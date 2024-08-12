package com.example.shoppinglist

import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ItemTouchHelperCallback(
    private val adapter: ItemTouchHelperAdapter
) : ItemTouchHelper.Callback() {

    private var isDragging = false
    private var dragStartPosition = RecyclerView.NO_POSITION
    private var fromPosition = RecyclerView.NO_POSITION
    private var toPosition = RecyclerView.NO_POSITION

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (source.adapterPosition != target.adapterPosition) {
            fromPosition = source.adapterPosition
            toPosition = target.adapterPosition
            adapter.onItemMove(fromPosition, toPosition)
        }
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            isDragging = true
            dragStartPosition = viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION
            // Optionally, perform additional actions such as hiding the keyboard
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (isDragging) {
            isDragging = false
            // Update the ViewModel with the final positions after dragging
            if (dragStartPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION) {
                //adapter.onItemMove(fromPosition, toPosition)
                // update the viewmodel
                (adapter as RecyclerAdapter).viewModel.moveItem(dragStartPosition, toPosition)
                adapter.notifyDataSetChanged()
                fromPosition = RecyclerView.NO_POSITION
                toPosition = RecyclerView.NO_POSITION
                dragStartPosition = RecyclerView.NO_POSITION
            }

            // Clear focus from AutoCompleteTextView widgets and hide keyboard
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
            layoutManager?.let {
                for (i in 0 until it.childCount) {
                    val view = it.getChildAt(i)
                    val actv = view?.findViewById<AutoCompleteTextView>(R.id.actvItemName)
                    actv?.clearFocus()
                }
            }
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Handle swipe-to-delete with undo
        Log.d("ItemTouchHelper", "Item at position ${viewHolder.adapterPosition} swiped")
        adapter.onItemSwiped(viewHolder.adapterPosition)
    }
}

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean
    fun onItemSwiped(position: Int)
}

interface StartDragListener {
    fun requestDrag(viewHolder: RecyclerView.ViewHolder)
}