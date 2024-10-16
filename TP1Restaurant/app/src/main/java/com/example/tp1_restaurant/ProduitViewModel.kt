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

    fun updateProduit(onUpdate: (Produit) -> Produit) {
        _produit.update { oldProduit ->
            oldProduit?.let {onUpdate(it)}
        }
    }

    fun deleteProduit(onDelete: (Produit) -> Produit) {
        _produit.update { oldProduit ->
            oldProduit?.let {onDelete(it)}
        }
    }

    override fun onCleared() {
        super.onCleared()
        produit.value?.let { produitRepository.updateProduit(it) }
    }

    class ProduitViewModelFactory(private val produitId: UUID) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProduitViewModel(produitId) as T
        }
    }
}