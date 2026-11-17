package com.example.kaijugrupomen.ui.components

import androidx.annotation.DrawableRes
import com.example.kaijugrupomen.R

@DrawableRes
fun imagenProducto(codigo: String): Int? = when (codigo) {
    "K-013" -> R.drawable.polera_rayquaza
    else -> null
}
