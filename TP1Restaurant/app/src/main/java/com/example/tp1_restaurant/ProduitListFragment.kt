package com.example.tp1_restaurant

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp1_restaurant.databinding.FragmentProduitListBinding
import com.example.tp1_restaurant.produit.Produit
import com.example.tp1_restaurant.produit.TypeProduit
import com.example.tp1_restaurant.produit.getRandomTypeProduit
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "ProduitListFragment"

/**
 * La classe ProduitListFragment permet de gérer la liste des produits.
 *
 * @constructor Crée un ProduitListFragment.
 * @return Un ProduitListFragment.
 *
 * @author Mouhammad Wagane Diouf et Fabrice Sénécal
 */
class ProduitListFragment : Fragment() {
    private var _binding: FragmentProduitListBinding? = null
    private val binding 
        get() = checkNotNull(_binding) {
            "Binding est null."
        }

    private val produitListViewModels: ProduitListViewModel by viewModels()

    /**
     * Lorsque la vue est créée.
     *
     * @param inflater Le layout inflater.
     * @param container Le container.
     * @param savedInstanceState L'état sauvegardé.
     *
     * @return La vue.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProduitListBinding.inflate(inflater, container, false)
        binding.produitRecyclerView.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }

    /**
     * Lorsque la vue est affichée.
     *
     * @param view La vue.
     * @param savedInstanceState L'état sauvegardé.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitListViewModels.produits.collect { produits ->
                    binding.produitRecyclerView.adapter = ProduitHolder.ProduitsListAdapter(produits) { produitId ->
                        findNavController().navigate(ProduitListFragmentDirections.showProduitDetail(produitId))
                    }
                }
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_produits_list, menu)
            }

            /**
             * Lorsqu'un item du menu est sélectionné.
             *
             * @param menuItem L'item du menu.
             *
             * @return Un booléen.
             */
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.nouveau_produit -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val nouveauProduit = Produit(
                                UUID.randomUUID(),
                                "",
                                TypeProduit.AUTRE,
                                "",
                                "",
                                null,
                            )
                            produitListViewModels.addProduit(nouveauProduit)
                            findNavController().navigate(
                                ProduitListFragmentDirections.showProduitDetail(nouveauProduit.id)
                            )
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /**
     * Lorsque la vue est détruite.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}