package Controleur;

import Model.Descendre;
import Model.Jump;
import Model.Position;
import Vue.Affichage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ReactionClic implements MouseListener {

    /*le modèle pour gérer la position*/
    private final Position maPosition;

    /*constructeur par défaut*/
    public ReactionClic(Affichage a, Position p){
        /*le composoant d'affichage*/
        maPosition = p;
        a.addMouseListener(this);
    }

    // on veut pouvoir sauter en cliquant sur la souris
    @Override
    public void mouseClicked(MouseEvent e) {
        if (Position.getJeuCommence()){
            Jump j = new Jump(maPosition);
            Descendre.resAccel();
        }

    }

    // on ne fait rien quand la souris est pressée
    @Override
    public void mousePressed(MouseEvent e) {}

    // on ne fait rien quand la souris est relâchée
    @Override
    public void mouseReleased(MouseEvent e) {}

    // on ne fait rien quand la souris entre dans la zone de la fenêtre
    @Override
    public void mouseEntered(MouseEvent e) { }

    // on ne fait rien quand la souris sort de la zone de la fenêtre
    @Override
    public void mouseExited(MouseEvent e) {}
}