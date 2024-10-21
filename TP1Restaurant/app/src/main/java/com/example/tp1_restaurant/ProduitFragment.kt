package com.example.tp1_restaurant

import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tp1_restaurant.databinding.FragmentProduitBinding
import com.example.tp1_restaurant.produit.Produit
import com.example.tp1_restaurant.produit.TypeProduit
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import android.Manifest


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

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(requireContext(), "La permission d'utiliser la caméra est requise pour prendre des photos.", Toast.LENGTH_SHORT).show()
        }
    }

    private val prendrePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { photoPrise: Boolean ->
            if (photoPrise && photoFilename != null) {
                Log.d("ProduitFragment", "Photo prise avec succès")
                produitViewModel.updateProduit { oldProduit ->
                    oldProduit.copy(photoProduit = photoFilename)
                }
            } else {
                Log.d("ProduitFragment", "Échec de la prise de photo")
            }
        }

    private var photoFilename: String? = null


    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun launchCamera() {
        photoFilename = "IMG_${Date()}.JPG"
        val photoFichier = File(requireContext().applicationContext.filesDir, photoFilename)
        val photoUri = FileProvider.getUriForFile(
            requireContext(),
            "com.example.tp1_restaurant.fileprovider",
            photoFichier
        )
        prendrePhoto.launch(photoUri)
    }

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
            spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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

            spinnerTypeProduit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = parent?.getItemAtPosition(position) as TypeProduit
                    produitViewModel.updateProduit { oldProduit ->
                        oldProduit.copy(typeProduit = selectedItem)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Ne rien faire si aucun élément n'est sélectionné
                }
            }

            boutonRetour.setOnClickListener {
                Log.d("ProduitFragment", "Le bouton de retour a été cliqué.")
                findNavController().popBackStack()
            }

            produitCamera.setOnClickListener {
                Log.d("ProduitFragment", "Le bouton de la caméra a été cliqué.")

                if (hasCameraPermission()) {
                    launchCamera()
                } else {
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitViewModel.produit.collect { produit ->
                    produit?.let {
                        updateUI(it)
                    }
                }
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(com.example.tp1_restaurant.R.menu.fragment_produit, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    com.example.tp1_restaurant.R.id.delete -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            produitViewModel.deleteProduit()
                            findNavController().popBackStack()
                        }
                        true
                    }
                    com.example.tp1_restaurant.R.id.share -> {
                        val produit = produitViewModel.produit.value
                        if (produit != null) {
                            shareProduit(produit)
                        } else {
                            Toast.makeText(requireContext(), "Produit indisponible", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /**
     * Permet de partager un produit.
     * Lorsque l'utilisateur clique sur le bouton de partage, un intent est créé pour partager les informations du produit.
     *
     * @param produit Le produit à partager.
     *
     */
    private fun shareProduit(produit: Produit) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"

        val shareText = """
        Découvrez ce produit :

        Nom : ${produit.nom}
        Type : ${produit.typeProduit}
        Pays d'origine : ${produit.paysOrigine}
        Producteur : ${produit.producteur}
        """.trimIndent()

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)

        if (produit.photoProduit != null) {
            val photoFile = File(requireContext().applicationContext.filesDir, produit.photoProduit)
            if (photoFile.exists()) {
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.tp1_restaurant.fileprovider",
                    photoFile
                )
                shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri)
                shareIntent.type = "image/*"
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        startActivity(Intent.createChooser(shareIntent, "Partager le produit"))
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

        }
        updatePhoto(produit.photoProduit)
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

    /**
     * Lorsque la vue est détruite.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}