package com.example.tp1_restaurant

import android.content.Context
import androidx.room.Room
import com.example.tp1_restaurant.database.ProduitDatabase
import com.example.tp1_restaurant.produit.Produit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "produit-database"

/**
 * ProduitRepository permet de gérer les produits.
 *
 * @property context Le contexte.
 * @property coroutineScope La portée de coroutine.
 * @constructor Crée un ProduitRepository.
 *
 * @author Mouhammad Wagane Diouf et Fabrice Sénécal
 *
 */
class ProduitRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {
    private val database: ProduitDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            ProduitDatabase::class.java,
            DATABASE_NAME,
        )
            .createFromAsset(DATABASE_NAME)
            .build()

    /**
     * Récupère les produits.
     *
     * @return Un Flow de liste de produits.
     */
    suspend fun getProduits():Flow<List<Produit>> = database.produitDao().getAllProduits()

    /**
     * Récupère un produit.
     *
     * @param id L'identifiant du produit.
     * @return Le produit.
     */
    suspend fun getProduit(id: UUID): Produit = database.produitDao().getProduit(id)

    /**
     * Ajoute un produit.
     *
     * @param produit Le produit à ajouter.
     */
    suspend fun addProduit(produit: Produit) = database.produitDao().insert(produit)

    /**
     * Supprime un produit.
     *
     * @param id L'identifiant du produit.
     */
    suspend fun deleteProduit(id: UUID) = database.produitDao().delete(id)

    /**
     * Met à jour un produit.
     *
     * @param produit Le produit à mettre à jour.
     */
    fun updateProduit(produit: Produit) {
        coroutineScope.launch {
            database.produitDao().update(produit)
        }
    }

    companion object {
        private  var INSTANCE : ProduitRepository? = null

        /**
         * Initialise le ProduitRepository.
         *
         * @param context Le contexte.
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ProduitRepository(context)
            }
        }

        /**
         * Récupère le ProduitRepository.
         *
         * @return Le ProduitRepository.
         */
        fun get(): ProduitRepository = INSTANCE ?: throw IllegalStateException("ProduitRepository doit être initialisé")
    }
}