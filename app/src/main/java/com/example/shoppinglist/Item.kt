package com.example.shoppinglist

class Item (
    var name: String,
    var done: Boolean = false,
    var amount: Int = 0,
    var amountType: String = "",
    var iconResource: Int = R.drawable.icon_apple) {
}
