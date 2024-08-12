package com.example.shoppinglist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat


class RecyclerAdapter(
    private var context: Context,
    private var itemList: MutableList<Item>,
    private val startDragListener: StartDragListener,
    private val recyclerView: RecyclerView,
    private val iconSearcher: IconSearcher,
    val viewModel: ItemViewModel
) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(), ItemTouchHelperAdapter {

    private var originalItemList: List<Item> = itemList.toList()
    private var isUpdating = false

    // Create the ArrayAdapter with custom dropdown item layout
    private val arrayAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            iconSearcher.makeWordList()
        )
    }

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

        holder.actvName.setHorizontallyScrolling(false);
        holder.actvName.setMaxLines(3);

        // Set the adapter to the AutoCompleteTextView
        holder.actvName.setAdapter(arrayAdapter)
        // handle item selection
        holder.actvName.setOnItemClickListener { parent, v, clickPosition, id ->
            val text = parent.getItemAtPosition(clickPosition).toString()
            onNameChanged(holder, item, position, text)
        }
        // handle key press
        holder.actvName.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
        //holder.actvName.setOnKeyListener { v, keyCode, event ->
                //   if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                // Code to execute when Enter key is pressed

                val text = holder.actvName.text.toString()
                onNameChanged(holder, item, position, text)

                // hide the keyboard
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                holder.actvName.clearFocus()

                true // Return true to indicate the event was handled
            } else {
                false // Return false to indicate the event was not handled
            }
        }
        // handle focus loss
        holder.actvName.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // When focus is lost, confirm the text
                val text = holder.actvName.text.toString()
                onNameChanged(holder, item, position, text)
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
            viewModel.changeItem(position, checked = isChecked)
        }

        // amount edit dialog
        holder.tvAmount.setOnClickListener {
            showAmountDialog(context, item) { amount, type ->
                // Create a DecimalFormat instance
                val decimalFormat = DecimalFormat("#.##")
                // Format the amount to ensure no trailing decimal places if it's a whole number
                val formattedAmount = decimalFormat.format(amount)

                holder.tvAmount.text = formattedAmount
                holder.tvAmountType.text = type

                item.amount = amount
                item.amountType = type

                // Update the UI or ViewModel as needed
                viewModel.changeItem(position, null, item.amount, item.amountType)
            }
        }
        holder.tvAmountType.setOnClickListener {
            showAmountDialog(context, item) { amount, type ->
                // Create a DecimalFormat instance
                val decimalFormat = DecimalFormat("#.##")
                // Format the amount to ensure no trailing decimal places if it's a whole number
                val formattedAmount = decimalFormat.format(amount)

                holder.tvAmount.text = formattedAmount
                holder.tvAmountType.text = type

                item.amount = amount
                item.amountType = type

                // Update the UI or ViewModel as needed
                viewModel.changeItem(position, null, item.amount, item.amountType)
            }
        }

        holder.cbDone.isChecked = item.done
        holder.actvName.setText(item.name)

        // Create a DecimalFormat instance
        val decimalFormat = DecimalFormat("#.##")
        // Format the amount to ensure no trailing decimal places if it's a whole number
        val formattedAmount = decimalFormat.format(item.amount)
        holder.tvAmount.text = if (item.amount <= 0) "" else formattedAmount

        holder.tvAmountType.text = item.amountType
        holder.imgIcon.setImageResource(item.iconResource)


        // Set touch listener for handle view
        holder.handle.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                startDragListener.requestDrag(holder)
                return@setOnTouchListener true
            }
            false
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition != toPosition) {
            Log.d("ITEM", "DRAGGING")
            viewModel.moveItem(fromPosition, toPosition)
            //notifyItemMoved(fromPosition, toPosition)
            return true
        }
        return false
    }

    override fun onItemSwiped(position: Int) {
        if (position < 0 || position >= itemList.size) return

        val removedItem = itemList[position]

        // Remove the item from the data source
        viewModel.removeItem(removedItem)
        //notifyItemRemoved(position)

        // Show the Snackbar with an undo option
        Snackbar.make(recyclerView, R.string.item_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                // Re-insert the item if the user clicks "Undo"
                val validPosition = if (position in itemList.indices) position else itemList.size

                viewModel.addItem(removedItem.name, validPosition)
                //itemList.add(validPosition, removedItem)
                //notifyItemInserted(validPosition)
            }
            .show()
    }

    private fun onNameChanged(holder: MyViewHolder, item: Item, position: Int, text: String) {
        // Update the item details
        if (text == item.namePrev) return

        item.iconResource = iconSearcher.findIconByQuery(text)
        item.name = text
        holder.imgIcon.setImageResource(item.iconResource)

        item.namePrev = item.name

        // Find an existing item with the same name (but not the current item)
        val existingItemIndex = itemList.indexOfFirst { it.name == text && it !== item }

        if (existingItemIndex != -1) {
            // Handle replacement in a way that avoids index issues
            viewModel.replaceItem(existingItemIndex, position)
        } else {
            // Update suggestions if no replacement needed
            iconSearcher.updateAutofillWords(itemList)
            updateSuggestions(iconSearcher.totalWordsSet.toList())

            viewModel.changeItem(position, name = item.name, icon = item.iconResource)
        }
    }

    // Method to update the suggestions data from outside
    fun updateSuggestions(newSuggestions: List<String>) {
        arrayAdapter.clear()
        arrayAdapter.addAll(newSuggestions)
        arrayAdapter.notifyDataSetChanged()
        Log.d("ITEM", "suggestion list updated.")
    }

    fun submitList(newList: List<Item>) {
        val diffCallback = ItemDiffCallback(itemList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newList)

        // Apply the changes
        diffResult.dispatchUpdatesTo(this)

        // Update the original list reference
        originalItemList = newList
    }

    fun safeSubmitList(newList: List<Item>) {
        if (isUpdating) return
        isUpdating = true
        recyclerView.post {
            submitList(newList)
            isUpdating = false
        }
    }

    private fun showAmountDialog(context: Context, item: Item, onSave: (Float, String) -> Unit) {
        // Inflate the dialog layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_amount_type, null)

        // Set up the dialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Find the views in the dialog
        val etAmount = dialogView.findViewById<EditText>(R.id.etAmount)
        val dlType = dialogView.findViewById<Spinner>(R.id.dlType)
        val bSave = dialogView.findViewById<Button>(R.id.bSave)
        val bCancel = dialogView.findViewById<Button>(R.id.bCancel)

        // Populate dialog values with amount and type

        // Create a DecimalFormat instance
        val decimalFormat = DecimalFormat("#.##")
        // Format the amount to ensure no trailing decimal places if it's a whole number
        val formattedAmount = decimalFormat.format(item.amount)

        etAmount.setText(formattedAmount.toString())

        val type = item.amountType
        // Select drop list position depending on type
        val adapter = dlType.adapter
        if (adapter is ArrayAdapter<*>) {
            @Suppress("UNCHECKED_CAST")
            val spinnerPos = (adapter as ArrayAdapter<String>).getPosition(type)
            if (spinnerPos != -1) {
                dlType.setSelection(spinnerPos)
            } else {
                Log.e("Spinner", "Value not found: $type")
            }
        }

        // Show the dialog
        dialog.setOnShowListener {
            etAmount.requestFocus()
            etAmount.postDelayed({
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(etAmount, InputMethodManager.SHOW_IMPLICIT)
                etAmount.setSelection(0, etAmount.text.length)
            }, 200) // Adjust delay as necessary
        }

        dialog.setOnDismissListener {
            // Get the RecyclerView's layout manager
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
            layoutManager?.let {
                // Iterate over visible items
                for (i in 0 until it.childCount) {
                    val view = it.getChildAt(i)
                    val actv = view?.findViewById<AutoCompleteTextView>(R.id.actvItemName)
                    actv?.clearFocus()
                }
            }

            // Hide the keyboard if it's open
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialog.window?.decorView?.windowToken, 0)
        }

        // Handle the save button click
        bSave.setOnClickListener {
            val amount = etAmount.text.toString().toFloatOrNull() ?: 0f
            val selectedType = dlType.selectedItem.toString()

            onSave(amount, selectedType)

            dialog.dismiss()
        }

        bCancel.setOnClickListener {
            dialog.cancel()
        }

        // Show the dialog
        dialog.show()
    }
}