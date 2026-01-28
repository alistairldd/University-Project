
## L'île Interdite - WANG Marc - POURCHOT Alistair

# Traité : 

On a traité l'ensemble du sujet, sauf pour le 8.2.4 où nous n'avons traité 
uniquement les différentes classes pour les personnages (sauf messager)
ainsi que les items : le sac de sable et helico.

# Architecture :

On a choisi de suivre le modèle MVC, ce qui nous permet de bien séparer la logique
, l'affichage et les interactions utilisateurs. 

Modele (CModele, Joueur, Tuile) : contient la logique du jeu, 
l'état de l'île, les règles, etc. 

Vue (CVue, VueCommandes, VueIle, VueJoueur) : affiche l'interface graphique du jeu 

Controleur (ControleurButton, ControleurClic, COntroleurMvt) : gère les actions
de l'utilisateurs (clic, déplacement) 


On utilise le patron Observateur, ce qui permet à la vue de se mettre automatiquement
à jour quand le modèle change, et cela évite de devoir gérer manuellement les 
rafraîchissement de l'affichage.

On a décidé de découper le code en plusieurs classes pour que chacune ait 
un rôle clair : 
- VueIle s'occupe de dessiner l'île, donc l'affichage de chaque tuiles,
    l'affichage de l'heliport, des coffres, etc.
- VueCommandes affiche les boutons d'actions du joueur, les possessions du joueur
  (clés, items, artéfacts) à gauche de l'île et également les premiers 
  boutons de choix de difficulté.
- VueJoueur affiche les icônes des joueurs sur l'île 

De leur côté, les contrôleurs ont également chacun une spécialisation
- ControleurMvt pour les déplacements du joueur
- ControleButton pour les différentes actions à effectuer en fonction du bouton sur 
lequel le joueur a appuyé
- ControleurClic pour gérer là ou clic le joueur

Ce découpage permet de faciliter les modifications, de tester plus facilement ou 
même juste de garder une structure propre. 


# Problemes persistants

Aucun

# Emprunt

Au long de ce projet, on aura utilisé différentes images trouvées sur internet.
Etant donné que ce projet est fait de manière à rester privé et non pas à devenir 'public',
on s'est alors autorisé à utiliser des images ne provenant pas de notre création.
A part cela, tout le code fourni viens de nous, il y a seulement une inspiration 
du fichier conway de notre archive, qui nous a été conseillé de regarder.
