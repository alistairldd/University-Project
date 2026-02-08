package Controleur;

import Model.Descendre;
import Model.Jump;
import Model.Position;
import Vue.Affichage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ReactionClavier implements KeyListener {

    /*le moèle pour gérer la position*/
    private final Position maPosition;

    /*constructeur par défaut*/
    public ReactionClavier(Affichage a, Position p){
        /*le composoant d'affichage*/
        maPosition = p;
        a.addKeyListener(this);
        a.setFocusable(true);
        a.requestFocus();
    }

    // on ne fait rien quand une touche est tapée
    @Override
    public void keyTyped(KeyEvent e) {}

    /*on veut pouvoir sauter en appuyant sur espace*/
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (Position.getJeuCommence()){
                Jump j = new Jump(maPosition);
                Descendre.resAccel();
            }

        }
    }

    // on ne fait rien quand la touche est relâchée
    @Override
    public void keyReleased(KeyEvent e) {}
}
