package com.example.tp1_restaurant.produit

enum class TypeProduit {
    VIN,
    SPIRITUEUX,
    APERITIF,
    BIERE,
    AUTRE
}

public fun getRandomTypeProduit(): TypeProduit {
    return TypeProduit.values().random()
}