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

private const val DATABASE_NAME = "produit_database"

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



    suspend fun getProduits():Flow<List<Produit>> = database.produitDao().getAllProduits()

    suspend fun getProduit(id: UUID): Produit = database.produitDao().getProduit(id)

    suspend fun addProduit(produit: Produit) = database.produitDao().insert(produit)

    suspend fun  deleteProduit(id: UUID) = database.produitDao().delete(id)

    fun updateProduit(produit: Produit) {
        coroutineScope.launch {
            database.produitDao().update(produit)
        }
    }

    companion object {
        private  var INSTANCE : ProduitRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ProduitRepository(context)
            }
        }

        fun get(): ProduitRepository = INSTANCE ?: throw IllegalStateException("ProduitRepository doit être initialisé")
    }
}