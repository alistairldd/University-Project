package Controleur;

import Model.Defilement;
import Model.Descendre;
import Model.Parcours;
import Model.Position;
import Vue.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Jeu {

    // On déclare tous les attributs nécessaires à notre jeu
    private JFrame maFenetre;
    private Affichage vue;
    private Position modelePosition;
    private Parcours modeleParcours;
    private Ciel modeleCiel;
    private Oiseaux modeleOiseaux;

    // Le constructeur du jeu, qui va initialiser tous les éléments du jeu
    public Jeu() {
        initialiserFenetre();
        initialiserModeles();
        initialiserVue();
        initialiserControleurs();
    }

    // Les méthodes d'initialisation sont séparées pour plus de clarté

    // On crée la fenêtre, on lui donne un titre, on définit son comportement à la fermeture et on empêche le redimensionnement
    private void initialiserFenetre() {
        maFenetre = new JFrame("Ketchapp Circle");
        maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maFenetre.setResizable(false);
    }

    // On crée les modèles, on lance les threads nécessaires pour faire bouger le ciel et les oiseaux
    private void initialiserModeles() {
        modeleCiel = new Ciel();
        modeleOiseaux = new Oiseaux(); // Lance les threads oiseaux
        new Ciel.Vent(modeleCiel);     // Lance le thread vent

        modelePosition = new Position();
        modeleParcours = new Parcours();
    }

    // On crée la vue, on l'ajoute à la fenêtre, on ajuste la taille de la fenêtre et on la rend visible, puis on lance le thread de redessin de la vue
    private void initialiserVue() {
        vue = new Affichage(modelePosition, modeleParcours, modeleCiel);

        // On ajoute la vue à la fenêtre, on ajuste la taille de la fenêtre et on la rend visible
        maFenetre.add(vue);
        maFenetre.pack();
        maFenetre.setVisible(true);

        new Redessine(vue);
    }

    // On crée les contrôleurs, on les associe à la vue et au modèle, puis on associe le contrôleur de boutons aux boutons de la vue
    private void initialiserControleurs() {
        // Les contrôleurs clavier/souris écoutent la vue mais modifient le modèle
        new ReactionClic(vue, modelePosition);
        new ReactionClavier(vue, modelePosition);

        // Le contrôleur de boutons écoute la vue et modifie le modèle
        ReactionBouton controleurBoutons = new ReactionBouton(vue, modelePosition, modeleParcours);

        // On dit aux boutons d'utiliser ce contrôleur
        vue.getBoutonStart().addActionListener(controleurBoutons);
        vue.getBoutonReplay().addActionListener(controleurBoutons);
    }
}