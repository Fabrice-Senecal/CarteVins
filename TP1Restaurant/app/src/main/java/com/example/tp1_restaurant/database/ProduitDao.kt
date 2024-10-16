package com.example.tp1_restaurant.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.tp1_restaurant.produit.Produit
import java.util.UUID

/**
 * Interface pour les opérations de base de données sur les produits.
 *
 * @author Fabrice Sénécal et Mouhammad Wagane Diouf
 */
interface ProduitDao {
    @Insert
    suspend fun insert(produit : Produit)

    @Query("SELECT * FROM produits ORDER BY id")
    suspend fun getAllProduits(): Flow<List<Produit>>

    @Query("SELECT * FROM produits WHERE id=(:id)")
    suspend fun getProduit(id: UUID): Produit

    @Update
    suspend fun update(produit : Produit)

    @Query("DELETE FROM produits WHERE id=(:id)")
    suspend fun delete(id : UUID)

}