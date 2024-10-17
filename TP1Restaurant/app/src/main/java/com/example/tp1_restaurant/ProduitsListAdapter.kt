package com.example.tp1_restaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp1_restaurant.databinding.ProduitListItemBinding
import com.example.tp1_restaurant.produit.Produit
import java.util.UUID

// A fix
class ProduitHolder(private val binding: ProduitListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(produit: Produit, onProduitClicked: (produitId: UUID) -> Unit) {
            // logo ressources ici !!!
            binding.nomProduit.text = produit.nom
            binding.typeProduit.text = produit.typeProduit.toString()
            binding.paysOrigine.text = produit.paysOrigine
            binding.producteurProduit.text = produit.producteur
           // binding.produitPhoto. = produit.photoProduit

            binding.root.setOnClickListener {
                onProduitClicked(produit.id)
            }
        }


    class ProduitsListAdapter(
        private val produits: List<Produit>,
        private val onProduitClicked: (produitId: UUID) -> Unit
    ) : RecyclerView.Adapter<ProduitHolder>() {

        override fun onBindViewHolder(holder: ProduitHolder, position: Int) {
            val produit = produits[position]
            holder.bind(produit, onProduitClicked)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ProduitListItemBinding.inflate(inflater, parent, false)
            return ProduitHolder(binding)
        }

        override fun getItemCount(): Int {
            return produits.size
        }
    }
}


