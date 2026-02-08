package Controleur;

import Model.Defilement;
import Model.Descendre;
import Model.Parcours;
import Model.Position;
import Vue.Affichage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReactionBouton implements ActionListener {
    // Notre affichage
    Affichage affichage;
    // Notre position
    Position position;
    // Notre parcours
    Parcours parcours;

    // Le constructeur de la classe, qui prend en paramètre les éléments nécessaires pour gérer les actions des boutons
    ReactionBouton(Affichage affichage, Position position, Parcours parcours) {
        this.affichage = affichage;
        this.position = position;
        this.parcours = parcours;
    }

    // La méthode qui va être appelée à chaque fois qu'un bouton est cliqué, elle va vérifier quel bouton a été cliqué et agir en conséquence
    @Override
    public void actionPerformed(ActionEvent e) {
        //
        JButton source = (JButton) e.getSource();

        // Si le bouton "Start" est cliqué, on lance le jeu en créant les threads nécessaires pour faire descendre le personnage et faire défiler le parcours,
        // on cache le bouton et on lui redonne le focus pour que les contrôles clavier fonctionnent
        if (source.getText().equals("Start")) {
            new Descendre(position);
            new Defilement(position, parcours);
            Position.setJeuCommence();
            source.setVisible(false);
            source.requestFocusInWindow();

        // Si le bouton "Rejouer" est cliqué, on réinitialise la position et le parcours, on remet le jeu en état de ne pas fini,
        // on relance les threads pour faire descendre le personnage et faire défiler le parcours,
        } else if (source.getText().equals("Rejouer")) {
            Position.reset();
            parcours.reset();
            Position.setJeuPasFini();
            new Descendre(position);
            new Defilement(position, parcours);
            source.setVisible(false);
            source.requestFocusInWindow();
        }
    }
}
