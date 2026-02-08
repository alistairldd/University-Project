package Vue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Ciel {
    /*liste position des nuages*/
    private ArrayList<Point> nuages;
    private final Random generateur = new Random();

    private final int LARGEUR_MAX = Affichage.ZONE_LARG;

    /*constructeur par défaut*/
    public Ciel() {
        nuages = new ArrayList<>();
        /*on initialise des positions aléatoires pour les nuages*/
        for (int i = 0; i < 3; i++) {
            nuages.add(new Point(LARGEUR_MAX + generateur.nextInt(LARGEUR_MAX), 50 + generateur.nextInt(100)));
        }
    }

    /*renvoie les positions des nuages*/
    public ArrayList<Point> getNuages() {return nuages;}

    /*actualise les positions des nuages*/
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

    /*thread qui fait défiler les nuages*/
    public static class Vent extends Thread{
        private Ciel monCiel;

        public Vent(Ciel c) {
            monCiel = c;
            this.start();
        }

        @Override
        public void run() {
            while (true) {
                monCiel.actualiserNuages();
                try {
                    // Vitesse de rafraîchissement du vent
                    int DELAY = 150;
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}