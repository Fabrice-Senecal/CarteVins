package com.example.tp1_restaurant.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tp1_restaurant.produit.Produit

/**
 * Base de données pour les produits.
 *
 * @author Fabrice Sénécal et Mouhammad Wagane Diouf
 */
@Database(entities = [Produit::class], version = 1, exportSchema = false)
@TypeConverters(ProduitTypeConverters::class)
abstract class ProduitDatabase : RoomDatabase() {
    abstract fun produitDao(): ProduitDao
}

