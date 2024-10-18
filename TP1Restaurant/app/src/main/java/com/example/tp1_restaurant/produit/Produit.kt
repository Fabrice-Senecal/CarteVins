package com.example.tp1_restaurant.produit

import android.graphics.Bitmap
import android.provider.MediaStore.Images
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Représente d'un produit.
 *
 * @param id L'indentifictaion du produit.
 * @param nom Le nom du produit.
 * @param typeProduit Le type de produit.
 * @param paysOrigine Le pays d'origine du produit.
 * @param producteur Le producteur du produit.
 * @param photoProduit La photo du produit.
 *
 * @constructor crée un produit.
 *
 * @author Mouhammad Wagane Diouf et Fabrice Sénécal
 */
@Entity(tableName = "produit-table")
data class Produit(
    @PrimaryKey val id: UUID,
    val nom: String,
    val typeProduit: TypeProduit,
    val paysOrigine: String,
    val producteur: String,
    val photoProduit: String? //utiliser les bitmaps pour transformer l'image en string
){}
