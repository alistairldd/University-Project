package Model;


import java.awt.*;
import java.util.ArrayList;

/* classe de la position : on trouve tout ce qui concerne la position de notre
ovale : baisser la position au fil du temps, ajouter si on saute etc
 */
public class Position extends Thread {

    // nos attributs :

    // deux booléens pour savoir si le jeu est fini ou s'il a commencé
    private static boolean JeuFini = false;
    private static boolean JeuCommence = false;

    // la hauteur de base de l'oval (qu'on multiplie par un facteur en fonction de la taille de la fenetre)
    public static final int HAUTEUR_OVAL = 10;
    public static final int LARG_OVAL = 6;

    // constante représentant la hauteur d'un saut
    private final int hauteurSaut = 6;

    // constante représentant la hauteur minimale de l'oval (pour éviter qu'il dépasse le bas de la fenêtre)
    public static final int HAUTEUR_MIN = -20;

    // constante représentant la hauteur maximale de l'oval (pour éviter qu'il dépasse le haut de la fenêtre)
    public static final int HAUTEUR_MAX = 50;

    // constante représentant l'horizon avant
    public static final int BEFORE = 50;

    // constante représentant l'horizon après (au delà duquel on ne dessine plus le parcours)
    public static final int AFTER = 200;

    /* la position de départ de l'ovale*/
    public static final int POS_DEPART = 0;

    /* l'attribut de position*/
    private static int position = POS_DEPART;

    // constante représentant l'avancement du parcours, qui permet de faire défiler le parcours et de faire comme si l'oval avançait
    private static int avancement = 0;

    // constante représentant le nombre de vies de départ
    private static int nbVies = 50;


    // --- GETTERS ET SETTERS ---

    /* getter pour l'attribut avancement*/
    public static int getAvancement() {
        return avancement;
    }

    /* settter pour l'attribut avancement -> représente le facteur de déroulement*/
    public void setAvancement(int a) {
        avancement = a;
    }

    /*renvoie la hauteur actuelle*/
    public int getPosition() {
        return position;
    }

    /*renvoie la hauteur du saut*/
    public int getHauteurSaut() {return hauteurSaut;}

    // méthode pour vérifier si le jeu est fini
    public static boolean getJeuFini() {
        return JeuFini;
    }

    public static void setJeuPasFini() {if (JeuFini) {JeuFini = false;}}

    // getter et setteur pour savoir si le jeu a commencé
    public static boolean getJeuCommence() {
        return JeuCommence;
    }

    public static void setJeuCommence() {if (!JeuCommence) {JeuCommence = true;}}


    // getter et setter pour le nombre de vies
    public static int getNbVies() {
        return nbVies;
    }

    // si on a plus de vies on arrête le jeu, sinon on enlève une vie
    public static void enleverVie() {if (nbVies > 0) {nbVies--;} else {JeuFini = true;}}

    // -- METHODES --

    /*méthode qui augmente la position de l'oval lorsqu'on saute
    * on empeche de sauter si l'oval dépasse la hauteur de la fenêtre
    * on augmente progressivement jsuqu'à atteindre hauteurSaut pour faire
    * comme une animation de saut
    * */
    public void sauter() {
        position += 1;
        if (position > HAUTEUR_MAX) {
            /*on remet la position au maximum autorisé*/
            position = HAUTEUR_MAX;
        }
    }

    /*méthode qui baisse la position de l'oval au fil du temps*/
    public void move(double d) {position -= d;}

    /* méthode qui vérifie si l'oval touche la ligne du parcours
    * on vérifie les du parcours pour voir s'il y a une collision
    * on regarde la position de l'ovale par rapport à la ligne du parcours pour voir s'il y a une collision
    * si la ligne est au-dessus de l'ovale ou en dessous de l'ovale, on enlève une vie
    * */
    public static void toucheLigne(ArrayList<Point> parcours) {
        /* coordonnées de l'ovale */
        int ovalX = BEFORE;
        /*bas de l'oval*/
        int ovalY = position;
        /*on vérifie les segments du parcours*/
        for (int i = 0; i < parcours.size() - 1; i++) {
            Point p1 = parcours.get(i);
            Point p2 = parcours.get(i + 1);

            /* On vérifie qu'on est bien au-dessus du segment horizontalement */
            if (ovalX >= p1.x && ovalX <= p2.x) {

                /*Calcul de la pente*/
                double pente = (double) (p2.y - p1.y) / (p2.x - p1.x);

                /*Calcul du Y de la ligne sous l'ovale*/
                double yLigne = p1.y + (ovalX - p1.x) * pente;

                /* Vérification de la collision avec une tolérance*/
                /* On regarde la distance absolue entre le centre de l'ovale et la ligne*/
                if (yLigne >= ovalY || yLigne <= ovalY - HAUTEUR_OVAL) {
                    enleverVie();
                }
            }
        }
    }

    /* méthode pour réinitialiser le jeu :
    *on remet la position de l'oval au départ, on remet les vies,
    *  on débloque le jeu, on remet le score à 0 et
    * on remet l'accélération à sa valeur de départ*/
    public static void reset() {
        position = POS_DEPART;
        avancement = 0;
        nbVies = 50; // On remet les vies
        JeuCommence = true; // On débloque le jeu
        Score.reset();
        Descendre.resAccel();
    }
}