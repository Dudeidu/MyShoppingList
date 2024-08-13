package com.example.shoppinglist

import android.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import java.util.Locale

class CustomArrayAdapter(
    context: Context,
    @LayoutRes resource: Int,
    internal var items: List<Any> = listOf()
)
    : ArrayAdapter<Any>(context, resource, items) {


    var tempItems: MutableList<Any> = mutableListOf()
    internal var suggestions: MutableList<Any> = mutableListOf()

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    private var filter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null && constraint.length >= 2) {
                val query = constraint.toString().lowercase()
                suggestions.clear()

                // Add items that starts with the query
                tempItems.forEach { item ->
                    val itemText = item.toString().lowercase()
                    if (itemText.startsWith(query)) {
                        // Add items that start with the query first
                        suggestions.add(item)
                    }
                }
                // Add items that contain the query but do not start with it
                tempItems.forEach { item ->
                    val itemText = item.toString().lowercase()
                    if (itemText.contains(query) && !itemText.startsWith(query)) {
                        suggestions.add(item)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            val filterList = results.values as? List<Any>
            if (results.count > 0) {
                clear()
                filterList?.forEach {
                    add(it)
                }.also {
                    notifyDataSetChanged()
                }
            }
        }
    }

    init {
        tempItems = items.toMutableList()
        suggestions = ArrayList()

    }

    override fun getFilter(): Filter {
        return filter
    }
}