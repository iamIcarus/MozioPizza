package com.mozio.moziopizza.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * pizza object
 * @property name Name of the pizza
 * @property price Pizza price
 */
@Entity(tableName = "pizza")
data class Pizza(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String? = null,
    val price: Double? = null,
)

/**
 * Object returned from the Pizza API call.
 * @property items the pizza items
 * @property dataHash a SHA256 hash from the raw data
 * @property errorMessage an error message if something went wrong
 */
data class PizzaResponse(val items: List<Pizza>, val dataHash: String, val errorMessage: String?)
