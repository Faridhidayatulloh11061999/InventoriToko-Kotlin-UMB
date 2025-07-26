package com.farid.inventory.model

import android.os.Parcelable
import com.farid.inventory.model.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val product: Product,
    var quantity: Int
) : Parcelable
