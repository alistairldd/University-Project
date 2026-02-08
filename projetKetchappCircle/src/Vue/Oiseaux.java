package Vue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Oiseaux {
    private static final ArrayList<Oiseau> oiseaux = new ArrayList<>();

    /*constructeur par défaut*/
    public Oiseaux() {
        this(5); // nombre par défaut, ajuster si besoin
    }

    /*constructeur avec nombre d'oiseaux*/
    public Oiseaux(int count) {
        for (int i = 0; i < count; i++) {
            Oiseau o = new Oiseau();
            oiseaux.add(o);
            o.start();
        }
    }

    /*renvoie les positions*/
    public static synchronized ArrayList<Point> getOiseaux() {
        ArrayList<Point> positions = new ArrayList<>();
        for (Oiseau o : oiseaux) {
            positions.add(new Point(o.getX(), o.getY()));
        }
        return positions;
    }

    /*arrête tous les oiseaux*/
    public synchronized void stopAll() {
        for (Oiseau o : oiseaux) {
            o.interrupt();
        }
    }

    /*ajoute un oiseau*/
    public void addOiseau() {
        Oiseau o = new Oiseau();
        oiseaux.add(o);
        o.start();
    }

}
/*classe représentant un oiseau qui se déplace de droite à gauche
* chaque oiseau a une position (x, y) et une vitesse de déplacement déterminée par un délai de rafraîchissement
* le thread de chaque oiseau met à jour sa position en fonction de sa vitesse et se réinitialise lorsqu'il sort de l'écran
* l'oiseau se déplace horizontalement de droite à gauche, et lorsqu'il atteint une position x inférieure à -300,
* il est réinitialisé à une position x aléatoire à droite de l'écran et une position y aléatoire entre 50 et 150*/
class Oiseau extends Thread {
    private final int delay; // Vitesse de rafraîchissement du vent
    private int x;
    private int y;
    private final int LARGEUR_MAX = Affichage.ZONE_LARG;
    private final Random generateur = new Random();

    /*constructeur*/
    public Oiseau() {
        this.x = LARGEUR_MAX + generateur.nextInt(50);
        this.y = 50 + generateur.nextInt(100);
        this.delay = generateur.nextInt(1000)/10 + 20; // entre 50ms et 150ms
    }

    /*renvoie la position X*/
    public int getX() {
        return x;
    }

    /*renvoie la position Y*/
    public int getY() {
        return y;
    }

    /*redéfinition de run*/
    @Override
    public void run() {
        while (!isInterrupted()) {
            x -= 3; // déplacement horizontal
            if (x < -300) {
                /*l'oiseau sort de l'écran, on le réinitialise à une position x aléatoire à droite de l'écran*/
                x = LARGEUR_MAX + generateur.nextInt(500);
                y = 50 + generateur.nextInt(100);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }

}
