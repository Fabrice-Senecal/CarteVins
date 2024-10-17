package com.example.tp1_restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tp1_restaurant.produit.Produit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ProduitViewModel(produitId: UUID) : ViewModel() {
    private val produitRepository = ProduitRepository.get()

    private val _produit: MutableStateFlow<Produit?> = MutableStateFlow(null)

    val produit: StateFlow<Produit?> = _produit

    init {
        viewModelScope.launch {
            _produit.value = produitRepository.getProduit(produitId)
        }
    }

    /**
     * Met à jour le produit.
     *
     * @param onUpdate La fonction de mise à jour.
     */
    fun updateProduit(onUpdate: (Produit) -> Produit) {
        _produit.update { oldProduit ->
            oldProduit?.let {onUpdate(it)}
        }
    }

    /**
     * Supprime la carte
     *
     * @param onDelete la fonction de suppression
     */
    fun deleteProduit(onDelete: (Produit) -> Produit) {
        _produit.update { oldProduit ->
            oldProduit?.let {onDelete(it)}
        }
    }

    /**
     * Lorsque la vue est détruite.
     */
    override fun onCleared() {
        super.onCleared()
        produit.value?.let { produitRepository.updateProduit(it) }
    }

    /**
     * ProduitViewModelFactory permet de créer un ProduitViewModel.
     *
     * @param produitId L'identifiant du produit.
     * @constructor Crée un ProduitViewModelFactory.
     * @return Un ProduitViewModel.
     *
     * @author Mouhammad Wagane Diouf et Fabrice Sénécal
     */
    class ProduitViewModelFactory(private val produitId: UUID) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProduitViewModel(produitId) as T
        }
    }
}