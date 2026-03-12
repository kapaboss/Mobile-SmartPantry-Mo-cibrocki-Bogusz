package data

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val UUID: String,
    val Name: String,
    val Quantity: Int,
    val Category: String
) : java.io.Serializable
