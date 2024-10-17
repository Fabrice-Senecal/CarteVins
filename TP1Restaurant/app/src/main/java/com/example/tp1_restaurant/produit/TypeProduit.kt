package com.example.tp1_restaurant.produit

/**
 * Enumération des types de cartes.
 *
 * @property VIN Type de produit qui représente les vins.
 * @property SPIRITUEUX Type de produit qui représente les spiritueux.
 * @property APERITIF Type de produit qui représente les apertifs.
 * @property BIERE Type de produit qui représente les bières.
 * @property AUTRE Type de produit qui représente autre chose.
 *
 * @constructor Crée un type de carte.
 *
 * @author Mouhammad Wagane Diouf et Fabrice Sénécal
 */
enum class TypeProduit {
    VIN,
    SPIRITUEUX,
    APERITIF,
    BIERE,
    AUTRE
}

/**
 * Retourne un type de produit aléatoire.
 *
 * @return un type de produit.
 */
 fun getRandomTypeProduit(): TypeProduit {
    return TypeProduit.values().random()
}