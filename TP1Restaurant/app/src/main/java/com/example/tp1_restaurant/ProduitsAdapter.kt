package com.example.tp1_restaurant

import androidx.recyclerview.widget.RecyclerView
import com.example.tp1_restaurant.produit.Produit
import java.util.UUID

// A fix
class ProduitsAdapter(val binding: ProduitListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(produit: Produit, onProduitClicked: (Produit) -> Unit) {

            // logo ressources ici !!!
        }
}

class ProduitsListAdapter(
    private val produits: List<Produit>,
    private val onProduitClicked: (Produit: UUID) -> Unit
)
{

}