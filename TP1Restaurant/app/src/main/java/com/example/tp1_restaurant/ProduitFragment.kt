package com.example.tp1_restaurant

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.tp1_restaurant.databinding.FragmentProduitBinding
import com.example.tp1_restaurant.produit.Produit
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

class ProduitFragment : Fragment() {
    private var _binding: FragmentProduitBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding est null, la vue est-elle visible?"
        }

    private val args: ProduitFragmentArgs by navArgs()
    private val produitViewModel: ProduitViewModel by viewModels {
        ProduitViewModel.ProduitViewModelFactory(args.produitId)
    }

    private val prendrePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { photoPrise: Boolean ->
            if (photoPrise && photoFilename != null) {
                produitViewModel.updateProduit { oldProduit ->
                    //comprend pas pk y aune erreur
                    oldProduit.copy(photoProduit = photoFilename)
                }
            }
        }
    private var photoFilename: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProduitBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // sync le XML


            //...

            val cameraIntent = prendrePhoto.contract.createIntent(
                requireContext(),
                Uri.parse("")
            )
            // cameraIntent.addCategory(Intent.CATEGORY_APP_CALCULATOR) // Pour tester !
            produitCamera.isEnabled = canResolveIntent(cameraIntent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            produitViewModel.produit.collect { produit ->
               produit?.let {
                    updateUI(it)
                }
            }
        }
    }

    fun updateUI(produit: Produit) {
        binding.apply {

            // ca doit etre l'id du ui
            produitCamera.setOnClickListener {
                photoFilename = "IMG_${Date()}.JPG"
                val photoFichier = File(requireContext().applicationContext.filesDir, photoFilename)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    // a changer
                    "cstjean.mobile.fileprovider",
                    photoFichier
                )
                prendrePhoto.launch(photoUri)
            }
            updatePhoto(produit.photoProduit)
        }
    }



    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        return intent.resolveActivity(packageManager) != null
    }


    private fun updatePhoto(photoFilename: String?) {
        if (binding.produitPhoto.tag != photoFilename) {
            val photoFichier = photoFilename?.let {
                File(requireContext().applicationContext.filesDir, it)
            }
            if (photoFichier?.exists() == true) {
                binding.produitPhoto.doOnLayout { view ->
                    val scaledBitmap = getScaledBitmap(
                        photoFichier.path,
                        view.width,
                        view.height
                    )
                    binding.produitPhoto.setImageBitmap(scaledBitmap)
                    binding.produitPhoto.tag = photoFilename
                }
            } else {
                binding.produitPhoto.setImageBitmap(null)
                binding.produitPhoto.tag = null
            }
        }
    }

}