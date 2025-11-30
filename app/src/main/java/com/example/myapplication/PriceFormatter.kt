package com.example.myapplication

import java.text.NumberFormat
import java.util.Locale

/**
 * Utilidad para formatear precios en pesos chilenos
 */
object PriceFormatter {
    /**
     * Formatea un precio en formato chileno: $9.999.999
     * @param price Precio en pesos chilenos (Long)
     * @return String formateado con el formato "$9.999.999"
     */
    fun formatPrice(price: Long): String {
        val locale = Locale.Builder().setLanguage("es").setRegion("CL").build()
        val formatter = NumberFormat.getNumberInstance(locale)
        return "$${formatter.format(price)}"
    }
}

