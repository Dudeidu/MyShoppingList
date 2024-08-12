package com.example.shoppinglist

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.ExperimentalSerializationApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ItemRepository(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    fun saveItems(items: MutableList<Item>) {
        val editor = sharedPreferences.edit()
        val jsonString = json.encodeToString(items)
        editor.putString("items", jsonString)
        editor.apply()
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun loadItems(): MutableList<Item>{
        val jsonString = sharedPreferences.getString("items", "[]") ?: "[]"
        return json.decodeFromString(jsonString)
    }
}