package com.example.tp1_restaurant.produit

import android.provider.MediaStore.Images
import androidx.room.PrimaryKey
import java.util.UUID

data class Produit(
    @PrimaryKey val id: UUID,
    val nom: String,
    val typeProduit: TypeProduit,
    val paysOrigine: String,
    val producteur: String,
    val photoProduit: String? //utiliser les bitmap pour transformer l'image en string
){}
