package main;

import Controleur.ReactionClic;
import Model.Ciel;
import Model.Parcours;
import Model.Position;
import Vue.Affichage;
import Vue.Oiseaux;
import Vue.Redessine;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        /** méthode de lancement du programme **/
        JFrame maFenetre = new JFrame("Ketchapp Circle");
        maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*on cré&e le modèle*/
        Position po = new Position();
        Parcours pa = new Parcours();
        Ciel c = new Ciel();
        Oiseaux o = new Oiseaux();

        Affichage a = new Affichage(po, pa, c);
        maFenetre.add(a);
        maFenetre.pack();
        maFenetre.setVisible(true);


        /* création du controleur */
        ReactionClic r = new ReactionClic(a, po);

        /*on dessine et on fait descendre*/
        Redessine re = new Redessine(a);
        Position.Descendre de = new Position.Descendre(po);
        Position.Defilement def = new Position.Defilement(po, pa);

        /*on fait souffler le vent (on fait bouger les nuages quoi)*/
        Ciel.Vent v = new Ciel.Vent(c);
        Oiseaux.VentOiseaux vo = new Oiseaux.VentOiseaux(o);

    }
}