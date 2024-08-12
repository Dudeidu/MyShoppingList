package com.example.shoppinglist

import android.content.Context

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class Item (
    var name: String,
    var done: Boolean = false,
    var amount: Float = 1.0f,
    var amountType: String = "",
    var iconResource: Int = R.drawable.icon_apple) {

        @kotlinx.serialization.Transient
        var namePrev: String = name
}

