package Model;

import Vue.Affichage;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Ciel {
    /*liste position des nuages*/
    private ArrayList<Point> nuages;
    private Random generateur = new Random();

    private final int LARGEUR_MAX = Affichage.ZONE_LARG;

    public Ciel() {
        nuages = new ArrayList<>();
        /*on initialise des positions aléatoires pour les nuages*/
        for (int i = 0; i < 3; i++) {
            nuages.add(new Point(generateur.nextInt(LARGEUR_MAX), 50 + generateur.nextInt(100)));
        }
    }

    public ArrayList<Point> getNuages() {
        return nuages;
    }

    public void actualiserNuages() {
        for (Point nuage : nuages) {
            nuage.x -= 3; // Vitesse de déplacement des nuages
            if (nuage.x < -100) { // Si le nuage sort de l'écran
                /*on réinitialise sa position X à droite de l'écran*/
                nuage.x = LARGEUR_MAX-1000 + generateur.nextInt(500);
                /*on réinitialise sa position Y aléatoirement*/
                nuage.y = 50 + generateur.nextInt(100);
            }
        }
    }

    public static class Vent extends Thread{
        private Ciel monCiel;
        private final int DELAY = 150; // Vitesse de rafraîchissement du vent

        public Vent(Ciel c) {
            monCiel = c;
            this.start();
        }

        @Override
        public void run() {
            while (true) {
                monCiel.actualiserNuages();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}