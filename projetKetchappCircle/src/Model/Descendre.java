package Model;

import Model.Position;

public class Descendre extends Thread {
    // nos attributs : la position et l'accélération
    private final Position maPosition;
    private static double accel = 0.5;

    /*constructeur par défaut*/
    public Descendre(Position p){
        maPosition = p;
        this.start();
    }

    /*remet l'accélération à sa valeur de départ lorsque l'on fait un saut*/
    public static void resAccel(){
        accel = 0.5;
    }

    /*incrémente l'accélération*/
    public static void incrAccel(){
        accel +=0.03;
    }

    /*redefinition run*/
    @Override
    public void run() {
        while(true) {
            /*on vérifie si on a plus de vie*/
            if (Position.getJeuFini()) {
                /*on arrête le thread de défilement si on a plus de vie*/
                Thread.currentThread().interrupt();
            }
            /*on attend un certain delai*/
            int DELAY = 50;
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                return;
            }

            /* si la position de l'ovale dépasse la taille de la fenêtre on empeche de descendre*/
            if (maPosition.getPosition()>0) {
                /*on descend progressivement*/
                maPosition.move(accel);
                incrAccel();
                /*on attend un certain delai*/
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}