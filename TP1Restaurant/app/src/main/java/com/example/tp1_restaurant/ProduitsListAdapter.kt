package com.example.tp1_restaurant

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp1_restaurant.databinding.ProduitListItemBinding
import com.example.tp1_restaurant.produit.Produit
import com.example.tp1_restaurant.produit.TypeProduit
import java.util.UUID

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

        if (produit.photoProduit == null) {
            binding.produitPhoto.setImageResource(getDefaultImage(produit.typeProduit))
        } else {
            val bitmap = stringToBitmap(produit.photoProduit)
            if (bitmap != null) {
                binding.produitPhoto.setImageBitmap(bitmap)
            } else {
                binding.produitPhoto.setImageResource(getDefaultImage(produit.typeProduit))
            }
        }

        binding.root.setOnClickListener {
            onProduitClicked(produit.id)
        }
    }

    private fun getDefaultImage(typeProduit: TypeProduit): Int {
        return when (typeProduit) {
            TypeProduit.AUTRE -> R.drawable.ic_autre
            TypeProduit.BIERE -> R.drawable.ic_glass_mug
            TypeProduit.VIN -> R.drawable.ic_glass_wine
            TypeProduit.SPIRITUEUX -> R.drawable.ic_spiritueux
            TypeProduit.APERITIF -> R.drawable.ic_apero
        }
    }

    private fun stringToBitmap(encodedString: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
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


