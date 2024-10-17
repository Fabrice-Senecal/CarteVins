package com.example.tp1_restaurant

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp1_restaurant.databinding.FragmentProduitListBinding
import com.example.tp1_restaurant.produit.Produit
import com.example.tp1_restaurant.produit.getRandomTypeProduit
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "ProduitListFragment"

class ProduitListFragment : Fragment() {
    private var _binding: FragmentProduitListBinding? = null
    private val binding 
        get() = checkNotNull(_binding) {
            "Binding est null."
        }

    private val produitListViewModels: ProduitListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProduitListBinding.inflate(inflater, container, false)
        binding.produitRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitListViewModels.produits.collect { produits ->
                    binding.produitRecyclerView.adapter = ProduitHolder.ProduitsListAdapter(produits) {produitId ->
                        //Log.d(TAG, produitId.toString() + "sa marche")

                        findNavController().navigate(
                            ProduitslistFragmentDirections.showProduitDetails(produitId)
                        )
                    }
                }
            }
        }

        /**
         * Ajouter section menu ici
         * (menuHost et onMenuItemSelected)
         */
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_produits_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.nouveau_produit -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val nouveauTravail = Produit(
                                UUID.randomUUID(),
                                "",
                                getRandomTypeProduit(),
                                "",
                                "",
                                "",
                            )
                            produitListViewModels.addProduit(nouveauTravail)
                            findNavController().navigate(
                                TravauxListFragmentDirections.showTravailDetail(nouveauTravail.id)
                            )
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}