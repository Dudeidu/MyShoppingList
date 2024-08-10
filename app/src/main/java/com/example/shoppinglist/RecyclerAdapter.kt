package com.example.shoppinglist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class RecyclerAdapter(
    private var context: Context,
    private var itemList: ArrayList<Item>,
    private val startDragListener: StartDragListener,
    private val recyclerView: RecyclerView,
    private val iconSearcher: IconSearcher
) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(), ItemTouchHelperAdapter {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cbDone: CheckBox = view.findViewById(R.id.cbDone)
        var actvName: AutoCompleteTextView = view.findViewById(R.id.actvItemName)
        var tvAmount: TextView = view.findViewById(R.id.tvAmountValue)
        var tvAmountType: TextView = view.findViewById(R.id.tvAmountType)
        var imgIcon: ImageView = view.findViewById(R.id.imgIcon)
        var handle: ImageView = view.findViewById(R.id.imgHandle)  // Handle view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.MyViewHolder {
        // Instantiate the contents of layout XML files into the view objects
        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.list_items, parent, false)

        return MyViewHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerAdapter.MyViewHolder, position: Int) {
        val item = itemList[position]

        // Read item suggestion text file from res/raw
        //val inputStream = context.resources.openRawResource(R.raw.item_suggestions)
        //val itemSuggestions = inputStream.bufferedReader().use { it.readText() }.split("\n").toTypedArray()
        val itemSuggestions = iconSearcher.makeWordList()

        // Create the ArrayAdapter with custom dropdown item layout
        val adapter : ArrayAdapter<String> = ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            itemSuggestions
        )

        // Set the adapter to the AutoCompleteTextView
        holder.actvName.setAdapter(adapter)
        // handle item selection
        holder.actvName.setOnItemClickListener { parent, view, position, id ->
            val text = parent.getItemAtPosition(position).toString()
            //holder.actvName.setText(text)
            item.iconResource = iconSearcher.findIconByQuery(text)
            item.name = text
            Log.d("ITEM", "text: $text , icon: $item.iconResource")
            holder.imgIcon.setImageResource(item.iconResource)
        }
        // handle focus loss
        holder.actvName.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // When focus is lost, confirm the text
                val text = holder.actvName.text.toString()
                item.iconResource = iconSearcher.findIconByQuery(text)
                item.name = text
                Log.d("ITEM", "text: $text , icon: $item.iconResource")
                holder.imgIcon.setImageResource(item.iconResource)
            }
        }

        // Set the OnCheckedChangeListener
        holder.cbDone.setOnCheckedChangeListener { buttonView, isChecked ->
            // Code to execute when the CheckBox is toggled
            if (isChecked) {
                holder.actvName.paintFlags = holder.actvName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                // Check if the strikethrough flag is enabled
                if (holder.actvName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG != 0) {
                    // If enabled, remove the strikethrough flag
                    holder.actvName.paintFlags = holder.actvName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
            item.done = isChecked
        }

        holder.actvName.setText(item.name)
        holder.tvAmount.text = if (item.amount == 0) "" else item.amount.toString()
        holder.tvAmountType.text = item.amountType
        holder.imgIcon.setImageResource(item.iconResource)

        // Set touch listener for handle view
        holder.handle.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                startDragListener.requestDrag(holder)
            }
            false
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition != toPosition) {
            val movedItem = itemList.removeAt(fromPosition)
            itemList.add(toPosition, movedItem)
            notifyItemMoved(fromPosition, toPosition)
        }
        return true
    }

    override fun onItemSwiped(position: Int) {
        val removedItem = itemList[position]

        // Remove the item from the data source
        itemList.removeAt(position)
        notifyItemRemoved(position)

        // Show the Snackbar with an undo option
        Snackbar.make(recyclerView, R.string.item_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                // Re-insert the item if the user clicks "Undo"
                itemList.add(position, removedItem)
                notifyItemInserted(position)
            }
            .show()
    }

}