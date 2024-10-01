[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/w0wfVsyL)
# TP1

## Mise en contexte
Un restaurateur vous approche pour créer une application mobile qui sera utilisée par les sommeliers
et sommelières de son restaurant pour gérer et présenter les différentes boissons offertes à leurs
clients.

## Consignes
Vous devez créer une application Android complète qui permet de gérer une liste de produits.

Vous devez programmer l’application en Kotlin.

L’application sera testée sur un Pixel 7 Pro API 34. Toutefois, vous devez utiliser les bonnes pratiques pour que l’application fonctionne sur un maximum d’appareils mobiles différents.

L’application doit fonctionner en mode paysage et en mode portrait.

Les données doivent être sauvegardées sur l’appareil avec la librairie Room.

L’application aura 2 écrans principaux et chaque écran aura des composants à implanter.

Vous devez créer votre propre thème pour l’application. Il vous faudra donc choisir des couleurs et un style visuel propre.

### **Écran 1**
#### Barre de l’application
En plus du nom de l’application, vous devez faire afficher l’option pour ajouter un produit.

L’ajout créera un nouveau produit et nous amènera à l’écran 2 pour la modification.

#### Contenu principal
On montrera les boissons présentes dans l’inventaire.
Vous devez utiliser un autre « widget » que le « RecyclerView » avec le « LinearLayoutManager ». Le « RecyclerView » peut être utilisé si vous le souhaitez mais il doit être composé d’un « layout » différent de nos projets habituels.

Pour chaque boisson, vous devez faire afficher un aperçu de la photo, le type de produit, le nom, le pays d’origine et le producteur. Si aucune image n’est présente pour le produit, vous devez utiliser une image par défaut.

Une icône adaptée doit accompagner le type de produit.

De plus, on doit pouvoir filtrer les produits par nom. Un champ texte dans le haut de l’écran nous permettra d’entrer une partie du nom et la liste devra s’ajuster en temps réel. Si on va à l’écran 2 et qu’on revient, le filtre doit rester actif.

Finalement, lorsqu’on pèse sur un produit, on tombe sur l’écran 2.

### **Écran 2**
#### Barre de l’application
Vous devez faire afficher 2 options :

 1. Partager le produit
 2. Supprimer le produit

Le bouton de partage permettra aux sommeliers de partager l’image de la boisson.

Assurez-vous que l’envoi fonctionne par courriel et sur Instagram. Vous aurez à trouver comment partager une image sur Instagram.

Si aucune application ne peut gérer l’envoi ou qu’aucune image n’est encore ajoutée, vous devez cacher le bouton.

Le deuxième bouton permettra de supprimer le produit. Vous devez toutefois demander une confirmation à l’usager via une boîte de dialogue. Assurez-vous que la photo et les données sont supprimées du téléphone.

#### Contenu principal
Ce sera l’écran de détails pour un produit. Tous les champs sont obligatoires sauf la photo. On pourra
effectuer des modifications sur tous les champs :
 - Nom (champ texte)
 -  Type de produit (sélection unique) : Vin, spiritueux, apéritif, bière ou autre.
 -  Pays d’origine (champ texte)
 -  Producteur (champ texte)
 -  Photo (il peut y avoir qu’une seule photo)

Pour la photo, vous devez faire afficher un aperçu et un bouton qui nous permettra de prendre une nouvelle photo.



Attention au plagiat et à la présentation de votre code. Ne pas oublier les commentaires et le Lint.  
Vous êtes autorisé d'utiliser le code partagé en classe.**  

## Remise
- Le travail doit se faire **en équipe de 2**, sauf sur approbation préalable du professeur.  
- La remise doit être sur Léa ou sur Github Classroom l’endroit approprié.  
- La remise doit se faire avant le 17 octobre 2024 à 23:59.  


#### Auteurs
Gabriel T. St-Hilaire  
Ajusté par Vincent Beauregard
