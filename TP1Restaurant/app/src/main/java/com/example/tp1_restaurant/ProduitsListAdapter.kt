package com.example.tp1_restaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp1_restaurant.databinding.ProduitListItemBinding
import com.example.tp1_restaurant.produit.Produit
import java.util.UUID

// A fix
/**
 * La classe ProduitHolder permet de lier les données du produit à la vue.
 *
 * @param binding La vue du produit.
 * @constructor Crée un ProduitHolder.
 *
 * @author Mouhammad Wagane Diouf et Fabrice Sénécal
 */
class ProduitHolder(private val binding: ProduitListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Lie les données du produit à la vue.
     *
     * @param produit Le produit.
     * @param onProduitClicked La fonction de clic sur le produit.
     */
        fun bind(produit: Produit, onProduitClicked: (produitId: UUID) -> Unit) {
            // logo ressources ici !!!
            binding.nomProduit.text = produit.nom
            binding.typeProduit.text = produit.typeProduit.toString()
            binding.paysOrigine.text = produit.paysOrigine
            binding.producteurProduit.text = produit.producteur

           // TODO
           // binding.produitPhoto. = produit.photoProduit

            binding.root.setOnClickListener {
                onProduitClicked(produit.id)
            }
        }


    /**
     * La classe ProduitListAdapter permet de lier les données de la liste des produits à la vue.
     *
     * @param produits La liste de produits.
     * @param onProduitClicked La fonction de clic sur le produit.
     * @constructor Crée un ProduitListAdapter.
     * @return Un ProduitListAdapter.
     *
     * @author Mouhammad Wagane Diouf et Fabrice Sénécal
     */
    class ProduitsListAdapter(
        private val produits: List<Produit>,
        private val onProduitClicked: (produitId: UUID) -> Unit
    ) : RecyclerView.Adapter<ProduitHolder>() {

        /**
         * Associe la vue du produit à un ViewHolder.
         *
         * @param holder Le ViewHolder.
         * @param position La position du produit.
         */
        override fun onBindViewHolder(holder: ProduitHolder, position: Int) {
            val produit = produits[position]
            holder.bind(produit, onProduitClicked)
        }

        /**
         * Crée un ViewHolder.
         *
         * @param parent Le parent de la vue.
         * @param p1 Le type de vue.
         * @return Un ProduitHolder.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ProduitListItemBinding.inflate(inflater, parent, false)
            return ProduitHolder(binding)
        }

        /**
         * Récupère le nombre de produits.
         *
         * @return retourne le nombre de produits.
         */
        override fun getItemCount(): Int {
            return produits.size
        }
    }
}


