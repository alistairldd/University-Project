package Model;

import java.awt.*;
import java.util.ArrayList;

public class Parcours {
    // nos attributs : une liste de points, un générateur de nombres aléatoires et des constantes pour les écarts entre les points
    private static java.util.Random generateur = new java.util.Random();

    private ArrayList<Point> points;

    // représentent les écarts minimum et maximum en X entre les points du parcours, pour assurer une certaine variabilité dans la génération du parcours
    public static final int ECART_MIN_X = 20;
    public static final int ECART_MAX_X = 25;

    // constructeur par défaut
    public Parcours() {
        points = new ArrayList<Point>();
        // Point de départ à la main pour faire une ligne droite au début
        points.add(new Point(0, Position.POS_DEPART - Position.HAUTEUR_OVAL / 2));
        points.add(new Point(Position.BEFORE + 10, Position.POS_DEPART - Position.HAUTEUR_OVAL / 2));

        // On remplit l'écran initialement
        while (points.get(points.size() - 1).x < Position.AFTER) {
            points.add(genererPointSuivant(points.get(points.size() - 1)));

        }
    }

    /* Méthode pour générer un point suivant à partir d'un point précédent
     * Le nouveau point est situé à une distance aléatoire en X du point précédent,
     * et sa hauteur est également aléatoire dans une plage définie pour assurer une certaine variabilité dans le parcours
     */
    private Point genererPointSuivant(Point precedent) {
        int ecart_x = ECART_MIN_X + generateur.nextInt(ECART_MAX_X - ECART_MIN_X + 1);
        int nouveauX = precedent.x + ecart_x;
        int nouveauY = Position.HAUTEUR_MIN / 4 + generateur.nextInt(Position.HAUTEUR_MAX -20);
        return new Point(nouveauX, nouveauY);
    }

    /* Méthode qui met à jour la liste des points du parcours en fonction de l'avancement
     * Elle ajoute des points si nécessaire et supprime les points trop éloignés
     * synchronisée pour éviter les problèmes en cas d'accès concurrent
     */
    private synchronized void actualiserParcours() {
        /* ajout de point si nécessaire */
        Point dernierPoint = points.getLast();

        /* Si le dernier point est proche de l'horizon droit, on en génère un nouveau
         * et on incrémente le score */
        if (dernierPoint.x - Position.getAvancement() < Position.AFTER + 50) {
            points.add(genererPointSuivant(dernierPoint));
            if (Position.getJeuCommence()){
                Score.incrementerScore();
            }

        }
        /* suppression des points trop éloignés à gauche */
        if (!points.isEmpty() && (points.getFirst().x - Position.getAvancement()) < -100) {
            points.removeFirst();
        }
    }

    /* Méthode pour obtenir la liste des points du parcours, décalés en fonction de l'avancement
     * Elle met à jour la liste source avant de renvoyer une nouvelle liste de points décalés
     * synchronisée pour éviter les problèmes en cas d'accès concurrent
     */
    public ArrayList<Point> getPoints() {
        /* On met à jour la liste source avant de renvoyer la version décalée*/
        actualiserParcours();
        /* on crée une nouvelle liste de points décalés */
        ArrayList<Point> nvxPoints = new ArrayList<Point>();
        for (Point pt : points) {
            nvxPoints.add(new Point(pt.x - Position.getAvancement(), pt.y));
        }
        return nvxPoints;
    }

    /* Méthode pour réinitialiser le parcours, utilisée lors du redémarrage du jeu
     * Elle vide la liste des points et recrée les points de départ comme dans le constructeur
     */
    public void reset() {
        points.clear();
        // On recrée les points du début comme dans le constructeur
        points.add(new Point(0, Position.POS_DEPART - Position.HAUTEUR_OVAL / 2));
        points.add(new Point(Position.BEFORE + 10, Position.POS_DEPART - Position.HAUTEUR_OVAL / 2));
        while (points.get(points.size() - 1).x < Position.AFTER) {
            points.add(genererPointSuivant(points.get(points.size() - 1)));
        }
    }
}