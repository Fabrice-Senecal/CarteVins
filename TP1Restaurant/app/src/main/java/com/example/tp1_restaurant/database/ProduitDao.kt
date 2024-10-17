package com.example.tp1_restaurant.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.tp1_restaurant.produit.Produit
import java.util.UUID

/**
 * Interface pour les opérations de base de données sur les produits.
 *
 * @author Fabrice Sénécal et Mouhammad Wagane Diouf
 */
@Dao
interface ProduitDao {
    @Insert
    suspend fun insert(produit : Produit)

    @Query("SELECT * FROM `produit-table` ORDER BY id")
    fun getAllProduits(): Flow<List<Produit>>

    @Query("SELECT * FROM `produit-table` WHERE id=(:id)")
    suspend fun getProduit(id: UUID): Produit

    @Update
    suspend fun update(produit : Produit)

    @Query("DELETE FROM `produit-table` WHERE id=(:id)")
    suspend fun delete(id : UUID)

}