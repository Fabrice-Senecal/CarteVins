package com.example.tp1_restaurant

import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.tp1_restaurant.databinding.FragmentProduitBinding
import com.example.tp1_restaurant.produit.Produit
import com.example.tp1_restaurant.produit.TypeProduit
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

/**
 * ProduitFragment affiche les détails d'un produit.
 *
 * @constructor Crée un ProduitFragment.
 *
 * @author Mouhammad Wagane Diouf et Fabrice Sénécal
 */
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
                    oldProduit.copy(photoProduit = photoFilename)
                }
            }
        }
    private var photoFilename: String? = null


    /**
     * Lorsque la vue est créée.
     *
     * @param inflater Le layout inflater.
     * @param container Le conteneur de la vue.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProduitBinding.inflate(inflater, container, false)
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

        binding.apply {
            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                TypeProduit.entries.toTypedArray()
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTypeProduit.adapter = spinnerAdapter

            nomProduit.doOnTextChanged { text, _, _, _ ->
                produitViewModel.updateProduit { oldProduit ->
                    oldProduit.copy(nom = text.toString())
                }
            }

            paysOrigine.doOnTextChanged { text, _, _, _ ->
                produitViewModel.updateProduit { oldProduit ->
                    oldProduit.copy(paysOrigine = text.toString())
                }
            }

            producteurProduit.doOnTextChanged { text, _, _, _ ->
                produitViewModel.updateProduit { oldProduit ->
                    oldProduit.copy(producteur = text.toString())
                }
            }

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

    /**
     * Met à jour l'interface utilisateur avec les données du produit.
     *
     * @param produit Le produit à afficher.
     */
    private fun updateUI(produit: Produit) {
        binding.apply {
            if (nomProduit.text.toString() != produit.nom)
                nomProduit.setText(produit.nom)

            if (paysOrigine.text.toString() != produit.paysOrigine)
                paysOrigine.setText(produit.paysOrigine)

            if (producteurProduit.text.toString() != produit.producteur)
                producteurProduit.setText(produit.producteur)

            val position = getPositionForProduitType(produit.typeProduit)
            if (spinnerTypeProduit.selectedItemPosition != position)
                spinnerTypeProduit.setSelection(position)

            produitCamera.setOnClickListener {
                photoFilename = "IMG_${Date()}.JPG"
                val photoFichier = File(requireContext().applicationContext.filesDir, photoFilename)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    // a changer
                    "com.example.fileprovider",
                    photoFichier
                )
                prendrePhoto.launch(photoUri)
            }
            updatePhoto(produit.photoProduit)
        }
    }

    /**
     * Permet d'obtenir la position du type de produit
     *
     * @param type Le type de produit.
     * @return La position du type de produit.
     */
    private fun getPositionForProduitType(type: TypeProduit): Int {
        val adapter = binding.spinnerTypeProduit.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i) == type) {
                return i
            }
        }
        return 0 // Default
    }


    /**
     * Vérifie si l'intent donné peut être résolu par une activité présente sur l'appareil.
     *
     * @param intent L'intent à vérifier.
     * @return true si l'intent peut être résolu, false sinon.
     */
    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        return intent.resolveActivity(packageManager) != null
    }


    /**
     * Fait une mise à jour de la photo du produit.
     *
     * @param photoFilename le nom du fichier de photo.
     */
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