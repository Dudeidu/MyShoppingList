package com.example.shoppinglist

import android.R.attr.data
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import java.util.Collections


class ItemViewModel(private val repository: ItemRepository) : ViewModel() {
    private val _itemList = MutableLiveData<MutableList<Item>>().apply { value = mutableListOf() }
    val itemList: LiveData<MutableList<Item>> = _itemList

    init {
        // Load items when the ViewModel is created
        _itemList.value = repository.loadItems()
    }

    fun updateItems() {
        val list = _itemList.value?.toMutableList() ?: return
        _itemList.value = list

        repository.saveItems(list)
    }

    fun changeItem(
        position: Int,
        name: String? = null,
        amount: Float? = null,
        amountType: String? = null,
        checked: Boolean? = null,
        icon: Int? = null) {

        val list = _itemList.value?.toMutableList() ?: return
        val item = list[position]

        if (name != null) item.name = name
        if (amount != null) item.amount = amount
        if (amountType != null) item.amountType = amountType
        if (checked != null) item.done = checked
        if (icon != null) item.iconResource = icon

        // Update the LiveData with the new list
        _itemList.value = list

        repository.saveItems(list)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        Log.e("ITEM", "move from: $fromPosition to $toPosition")
        val currentList = _itemList.value ?: return
        if (fromPosition !in currentList.indices || toPosition !in currentList.indices) {
            Log.e("ItemViewModel", "Invalid positions for moveItem: fromPosition=$fromPosition, toPosition=$toPosition")
            return
        }

        // Create a mutable copy of the current list
        val list = currentList.toMutableList()

        // Move item within the list
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }

        // Update LiveData with the new list
        _itemList.value = list

        // Save the updated list to the repository
        repository.saveItems(list)
    }

    fun replaceItem(oldPosition: Int, newPosition: Int) {
        val list = _itemList.value?.toMutableList() ?: return

        if (oldPosition in list.indices) {
            val item = list[oldPosition]

            // Remove the item from the old position
            list.removeAt(oldPosition)

            // Adjust the new position if necessary
            val adjustedNewPosition = if (newPosition > oldPosition) newPosition - 1 else newPosition

            // Ensure the new position is within bounds
            if (adjustedNewPosition in list.indices) {
                // Replace the item if the new position is within bounds
                list[adjustedNewPosition] = item
            } else {
                // If the new position is out of bounds, handle it gracefully
                list.add(item) // Adding to the end of the list as a fallback
            }

            // Update LiveData with the new list
            _itemList.value = list

            repository.saveItems(list)
        } else {
            Log.e("RecyclerAdapter", "Invalid positions for replacement: oldPosition=$oldPosition, newPosition=$newPosition")
        }
    }

    fun addItem(name: String, position: Int = 0) {
        val list = _itemList.value ?: mutableListOf()
        val existingItemIndex = list.indexOfFirst { it.name == name }

        var item: Item? = null
        if (existingItemIndex == -1) {
            item = Item(name)
        } else {
            item = list[existingItemIndex]
            list.removeAt(existingItemIndex)
        }

        // Search Icon by name
        item.iconResource = IconSearcher().findIconByQuery(name)

        if (position != 0 && position in list.indices) {
            list.add(position, item)
        } else if (name == "") {
            list.add(0, item)
        } else {
            list.add(item)
        }
        _itemList.value = list

        repository.saveItems(list)
    }

    fun removeItem(item: Item) {
        val list = _itemList.value ?: mutableListOf()
        list.remove(item)
        _itemList.value = list

        repository.saveItems(list)
    }
}