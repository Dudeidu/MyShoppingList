package com.example.shoppinglist

import android.R.attr.data
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Collections


class ItemViewModel : ViewModel() {
    private val _itemList = MutableLiveData<MutableList<Item>>().apply { value = mutableListOf() }
    val itemList: LiveData<MutableList<Item>> = _itemList

    fun updateItems() {
        val list = _itemList.value?.toMutableList() ?: return
        _itemList.value = list
    }
    fun moveItem(fromPosition: Int, toPosition: Int) {
        val list = _itemList.value?.toMutableList() ?: return

        if (fromPosition in list.indices && toPosition in list.indices) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(list, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(list, i, i - 1)
                }
            }

            // Update the LiveData with the new list
            _itemList.value = list
        } else {
            Log.e("ItemViewModel", "Invalid positions for moveItem: fromPosition=$fromPosition, toPosition=$toPosition")
        }
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
    }

    fun removeItem(item: Item) {
        val list = _itemList.value ?: mutableListOf()
        list.remove(item)
        _itemList.value = list
    }
}