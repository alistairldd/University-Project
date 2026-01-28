package Vue;

import Model.Ciel;
import Vue.Affichage;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Oiseaux {
    /*vitesse déplacement oiseau*/
    private final int DELAY = 150;
    private static ArrayList<Point> oiseaux;
    private Random generateur = new Random();
    private final int LARGEUR_MAX = Affichage.ZONE_LARG;



    public Oiseaux() {
        this.oiseaux = new ArrayList<Point>();
        for (int i = 0; i < 10; i++) {
            oiseaux.add(new Point(generateur.nextInt(LARGEUR_MAX), 50 + generateur.nextInt(100)));
        }
    }

    public static ArrayList<Point> getOiseaux() {
        return oiseaux;
    }


    public void actualiserOiseaux() {
        for (Point oisal : oiseaux) {
            oisal.x -= 3; // Vitesse de déplacement des nuages
            if (oisal.x < -300) { // Si le nuage sort de l'écran
                /*on réinitialise sa position X à droite de l'écran*/
                oisal.x = LARGEUR_MAX-1000 + generateur.nextInt(500);
                /*on réinitialise sa position Y aléatoirement*/
                oisal.y = 50 + generateur.nextInt(100);
            }
        }
    }


    /* thread qui fait défiler l'avancement*/
    public static class VentOiseaux extends Thread{
            private Oiseaux mesOiseaux;
            private final int DELAY = 100; // Vitesse de rafraîchissement du vent

            public VentOiseaux(Oiseaux ois) {
                mesOiseaux = ois;
                this.start();
            }

            @Override
            public void run() {
                while (true) {
                    mesOiseaux.actualiserOiseaux();
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
}
