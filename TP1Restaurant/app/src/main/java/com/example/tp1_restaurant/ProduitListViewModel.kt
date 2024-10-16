package com.example.tp1_restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1_restaurant.produit.Produit
import com.example.tp1_restaurant.produit.TypeProduit
import com.example.tp1_restaurant.produit.getRandomTypeProduit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class ProduitListViewModel : ViewModel() {

    private  val produitRepository = ProduitRepository.get()
    private val _produits: MutableStateFlow<List<Produit>> = MutableStateFlow(emptyList())
    val produits : StateFlow<List<Produit>> = _produits

    init {
        viewModelScope.launch {
            loadProduits();
            produitRepository.getProduits().collect { _produits.value = it }
        }
    }

    suspend fun loadProduits() {
        for (i in 0 until 10) {
            val produit = Produit(
                UUID.randomUUID(),
                "nom de commerce #$i",
                getRandomTypeProduit(),
                "Canada",
                "Quelqun",
                "photo #$i"
            )

            addProduit(produit)
        }
    }

    suspend fun addProduit(produit: Produit) {
        produitRepository.addProduit(produit)
    }


}