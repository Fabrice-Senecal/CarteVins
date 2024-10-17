package com.example.tp1_restaurant

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.roundToInt

/**
 * Obtenir un bitmap redimensionné proportionnellement à partir d'un chemin de fichier.
 *
 * @param path Le chemin vers l'image à redimensionner.
 * @param destWidth La largeur cible à laquelle l'image doit être redimensionnée.
 * @param destHeight La hauteur cible à laquelle l'image doit être redimensionnée.
 *
 * @return Un objet Bitmap redimensionné en fonction des dimensions spécifiées.
 *
 * @author Mouhammad Wagane Diouf et Fabrice Sénécal
 */
fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()
    val sampleSize = if (srcHeight <= destHeight && srcWidth <= destWidth) {
        1
    } else {
        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWidth

        minOf(heightScale, widthScale).roundToInt()
    }
    return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply {
        inSampleSize = sampleSize
    })
}