package com.farid.inventory.model

data class ProductResponse(
    val success: Boolean,
    val products: List<Product>
)
