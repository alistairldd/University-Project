package Model;

import java.awt.*;
import java.util.ArrayList;

public class Parcours {
    private static java.util.Random generateur = new java.util.Random();

    public static final int ECART_MIN_X = 10;
    public static final int ECART_MAX_X = 25;

    private ArrayList<Point> points;

    public Parcours() {
        points = new ArrayList<Point>();
        // Point de départ
        points.add(new Point(0, Position.POS_DEPART - Position.HAUTEUR_OVAL / 2));
        points.add(new Point(Position.BEFORE + 10, Position.POS_DEPART - Position.HAUTEUR_OVAL / 2));

        // On remplit l'écran initialement
        while (points.get(points.size() - 1).x < Position.AFTER) {
            points.add(genererPointSuivant(points.get(points.size() - 1)));

        }
    }

    /**
     * Cette méthode génère un point unique basé sur le point précédent.
     * Elle contient la logique mathématique (écarts, bornes Y) qui était dans le constructeur.
     */
    private Point genererPointSuivant(Point precedent) {
        int ecart_x = ECART_MIN_X + generateur.nextInt(ECART_MAX_X - ECART_MIN_X + 1);
        int nouveauX = precedent.x + ecart_x;

        int nouveauY = Position.HAUTEUR_MIN / 4 + generateur.nextInt(Position.HAUTEUR_MAX - 15);


        if (nouveauY < Position.HAUTEUR_MIN / 4) {
            nouveauY = Position.HAUTEUR_MIN / 4;
        } else if (nouveauY > Position.HAUTEUR_MAX - Position.HAUTEUR_OVAL / 2) {
            nouveauY = Position.HAUTEUR_MAX - Position.HAUTEUR_OVAL / 2;
        }

        return new Point(nouveauX, nouveauY);
    }

    /**
     * Logique de mise à jour du parcours infini
     */
    private synchronized void actualiserParcours() {
        /* ajout de point si nécessaire */
        Point dernierPoint = points.get(points.size() - 1);

        /* Si le dernier point est proche de l'horizon droit, on en génère un nouveau
         * et on incrémente le score */
        if (dernierPoint.x - Position.getAvancement() < Position.AFTER + 50) {
            points.add(genererPointSuivant(dernierPoint));
            Score.incrementerScore();
        }


        if (!points.isEmpty() && (points.get(0).x - Position.getAvancement()) < -100) {
            points.remove(0);
        }
    }

    /* Modification de getPoints pour inclure la mise à jour automatique*/
    public ArrayList<Point> getPoints() {
        // On met à jour la liste source avant de renvoyer la version décalée
        actualiserParcours();

        ArrayList<Point> nvxPoints = new ArrayList<Point>();
        for (Point pt : points) {
            nvxPoints.add(new Point(pt.x - Position.getAvancement(), pt.y));
        }
        return nvxPoints;
    }
}