package com.example.tp1_restaurant

import android.app.Application

/**
 * Classe principale de l'application.
 *
 * @author Fabrice Sénécal et Mouhammad Wagane Diouf
 */
class RestaurantApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ProduitRepository.initialize(this)
    }
}