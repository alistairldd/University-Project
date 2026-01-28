package Controleur;

import Model.Position;
import Vue.Affichage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ReactionClic implements MouseListener {

    /*le composoant d'affichage*/
    private Affichage monAffichage;

    /*le moèle pour gérer la position*/
    private Position maPosition;

    /*constructeur par défaut*/
    public ReactionClic(Affichage a, Position p){
        monAffichage = a;
        maPosition = p;
        a.addMouseListener(this);
    }

    /*redéfinition de la méthode mouseClicked*/
    @Override
    public void mouseClicked(MouseEvent e) {
        Position.Jump j = new Position.Jump(maPosition);
        Position.Descendre.resAccel();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}