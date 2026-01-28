package Model;


import java.awt.*;
import java.util.ArrayList;

/* classe de la position : on trouve tout ce qui concerne la position de notre
ovale : baisser la position au fil du temps, ajouter si on saute etc
 */
public class Position extends Thread{

    private static boolean JeuEnCours = true;

    /* la hauteur de base de l'oval (qu'on multiplie par un facteur en fonction de la taille de la fenetre)*/
    public static final int HAUTEUR_OVAL = 10;
    public static final int LARG_OVAL = 6;

    /*constante représentant la hauteur d'un saut*/
    private final int hauteurSaut = 5;


    /*constante telle que si l'oval est à cette hauteur il arrete de descendre*/
    public static final int HAUTEUR_MIN = -20;

    /*constante telle que si l'oval est à cette hauteur on peut plus sauter*/
    public static final int HAUTEUR_MAX = 50;


    /*constante représentant l'horizon avant*/
    public static final int BEFORE = 50;

    /*constante représentant l'horizon après*/
    public static final int AFTER = 200;


    /* la position de départ de l'ovale*/
    public static final int POS_DEPART = 0;

    /* l'attribut de position*/
    private static int position = POS_DEPART;

    /* représente la position en X de l'ovale dans le modèle*/
    private static int avancement = 0;

    /* getter pour l'attribut avancement*/
    public static int getAvancement() {
        return avancement;
    }

    /* settter pour l'attribut avancement -> représente le facteur de déroulement*/
    public void setAvancement(int a) {
        avancement = a;
    }

    /*renvoie la hauteur actuelle*/
    public int getPosition(){return position;}

    /*renvoie la hauteur du saut*/
    public int getHauteurSaut(){return hauteurSaut;}

    /*effectue un saut, pour monter loval*/
    public void sauter(){
        /* on empeche de sauter si l'oval dépasse la hauteur de la fenetre*/
        /* on augmente progressivement jusqu'à atteindre hauteurSaut pour faire comme une animation de saut */
        position += 1;
        if (position>HAUTEUR_MAX){
            /*on remet la position au maximum autorisé*/
            position = HAUTEUR_MAX;
        }
    }

    /*méthode qui baisse la position de l'oval au fil du temps*/
    public int move(double d){
        position-=d;
        return position;
    }

    /*nombre de vies*/
    private static int nbVies = 1;
    /*méthode qui renvoie le nombre de vies*/
    public static int getNbVies() {
        return nbVies;
    }

    /*méthode qui enlève une vie*/
    public static void enleverVie() {
        nbVies--;
        if (nbVies <= 0){
            JeuEnCours = false;
        }
    }

    /*méthode qui vérifie si on touche la ligne du parcours*/
    public static void toucheLigne(ArrayList<Point> parcours) {
        /*la tolérance de collision*/
        int tolerance = 3;

        /* coordonnées de l'ovale */
        int ovalX = BEFORE;
        /*bas de l'oval*/
        int ovalY = position;

        /*on vérifie les 4 segments les plus proches de l'ovale*/
        for (int i = 0; i < parcours.size()-1; i++) {
            Point p1 = parcours.get(i);
            Point p2 = parcours.get(i + 1);

            /* On vérifie qu'on est bien au-dessus du segment horizontalement */
            if (ovalX >= p1.x && ovalX <= p2.x) {

                /*Calcul de la pente*/
                double pente = (double) (p2.y - p1.y) / (p2.x - p1.x);

                /*Calcul du Y de la ligne sous l'ovale*/
                double yLigne =  p1.y + (ovalX - p1.x) * pente;

                /* Vérification de la collision avec une tolérance*/
                /* On regarde la distance absolue entre le centre de l'ovale et la ligne*/
                if (yLigne >= ovalY || yLigne <= ovalY-HAUTEUR_OVAL) {
                    enleverVie();
                }
            }
        }
    }

/* thread qui fait défiler l'avancement*/
public static class Defilement extends Thread {
    private final static int DELAY = 100;
    private Position monAvancement;
    private Parcours monParcours;
    /*constructeur par défaut*/

    public Defilement(Position p, Parcours pa){
        monAvancement = p;
        monParcours = pa;
        this.start();
    }

    /* on rajoute un thread pour faire défiler l'avancement*/
    @Override
    public void run() {
        while (JeuEnCours) {
            toucheLigne(monParcours.getPoints());
            /*on vérifie si on a plus de vie*/
            if (Position.getNbVies() <= 0) {
                Thread.currentThread().interrupt();
            }
            /*on augmente l'avancement*/
            monAvancement.setAvancement(Position.getAvancement() + 1);
            /*on attend un certain delai*/
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

public static class Jump extends Thread {
    private Position maPosition;

    /*constructeur par défaut*/
    public Jump(Position p){
        maPosition = p;
        this.start();
    }

    /*redéfinition de run*/
    @Override
    public void run() {
        /*on effectue le saut*/
        for(int i=0; i<maPosition.getHauteurSaut(); i++){
            if (!JeuEnCours) {
                return;
            }
            maPosition.sauter();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}


public static class Descendre extends Thread {
        private int DELAY = 75;
        private Position maPosition;
        private static double accel = 0.5;

        /*constructeur â défaut*/
        public Descendre(Position p){
            maPosition = p;
            this.start();
        }

        /*remet l'accélération à sa valeur de départ lorsque l'on fait un saut*/
        public static void resAccel(){
            accel =0.5;
        }

        /*incrémente l'accélération*/
        public static void incrAccel(){
            accel +=0.1;
        }

        /*redefinition run*/
        @Override
        public void run() {
            while(JeuEnCours) {
                if (Position.getNbVies() <= 0) {
                    Thread.currentThread().interrupt();
                }
                /*si on mets pas ça, la boucle while ne s'execute plus une fois que la position est arrivée en dessous du minimum*/
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                /* si la position de l'ovale dépasse la taille de la fenêtre on empeche de descebndre*/
                if (maPosition.getPosition()>0) {
                    /*on descend progressivement*/
                    maPosition.move(accel);
                    incrAccel();
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}