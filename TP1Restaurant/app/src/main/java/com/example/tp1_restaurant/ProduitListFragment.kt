package com.example.tp1_restaurant

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp1_restaurant.databinding.FragmentProduitListBinding
import kotlinx.coroutines.launch

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
                        Log.d(TAG, produitId.toString() + "sa marche")
                        /*
                        findNavController().navigate(
                            ProduitsListFragmentDirections.showProduitDetails(produitId)
                        )
                         */
                    }
                }
            }
        }

        /**
         * Ajouter section menu ici
         * (menuHost et onMenuItemSelected)
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}