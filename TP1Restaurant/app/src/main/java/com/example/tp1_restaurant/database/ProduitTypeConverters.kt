package com.example.tp1_restaurant.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.tp1_restaurant.produit.Produit
import com.example.tp1_restaurant.produit.TypeProduit
import java.io.ByteArrayOutputStream

/**
 * Convertisseurs pour les types de produits.
 *
 * @author Fabrice Sénécal et Mouhammad Wagane Diouf
 */
class ProduitTypeConverters {
    @TypeConverter
    fun fromTypeProduit(typeProduit: TypeProduit): String {
        return typeProduit.name
    }

    @TypeConverter
    fun toTypeProduit(typeProduit: String): TypeProduit {
        return  TypeProduit.valueOf(typeProduit)
    }

    @TypeConverter
    fun fromImageBitmap(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    @TypeConverter
    fun toImageBitmap(encodedString: String?): Bitmap? {
        if (encodedString == null) return null
        val bytes = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

}