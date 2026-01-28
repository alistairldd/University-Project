package Vue;

import Model.Ciel;
import Model.Parcours;
import Model.Position;

import javax.swing.*;
import java.awt.*;

public class Affichage extends JPanel {

    /*constantes permettant de définir la taille de la fenetre*/
    public final static int RATIO_X = 10;
    public final static int RATIO_Y = 10;

    /* constantes représentant la zone de visibilié dans le mpdèle*/
    public final static int ZONE_LARG = (Position.BEFORE + Position.AFTER) * RATIO_X;
    public final static int ZONE_HAUT = (Position.HAUTEUR_MAX - Position.HAUTEUR_MIN) * RATIO_Y;

    /*constante représentant la largeur de l'oval dans notre fenetre*/
    public final  static int LARG_OVAL = Position.LARG_OVAL * RATIO_X;
    /*nouvelle hauteur de l'oval dans notre fenetre*/
    public final static int HAUTEUR_OVAL = Position.HAUTEUR_OVAL * RATIO_Y;

    /*constante représentant la position X fixe de l'oval*/
    public final static int POSITION_X = (Position.BEFORE*RATIO_X) - (LARG_OVAL/2);

    /*constante représentant la position Y non fixe de l'oval*/
    private int position_y = (Position.HAUTEUR_MAX - Position.POS_DEPART - Position.HAUTEUR_OVAL)*RATIO_Y;


    /*le modèle qui donne le parcours*/
    private Parcours monParcours;

    /*le modèle qui done la position*/
    private Position maPosition;

    private Ciel ciel;

    /*constructeur par défaut*/
    public Affichage(Position po, Parcours pa, Ciel c) {
        /*on définit la taille préférée du JPanel*/
        setPreferredSize(new Dimension(ZONE_LARG,ZONE_HAUT));
        maPosition = po;
        monParcours = pa;
        ciel = c;
        this.setBackground(new Color(135, 206, 235));
    }


    public void dessinerScore(Graphics g) {
        Font font = g.getFont();
        Font nvFont = new Font("Monospaced", Font.BOLD, 20);
        g.setFont(nvFont);


        g.drawString("Score : " + Model.Score.getScore(), 10, 20);
        g.setFont(font);
    }


    /*methode pour afficher la ligne du parcours*/
    public void dessinerParcours(Graphics g) {
        for (int i = 0; i < monParcours.getPoints().size() - 1; i++) {
            /*on récupère les deux points à relier*/
            Point p1 = monParcours.getPoints().get(i);
            Point p2 = monParcours.getPoints().get(i + 1);
            /*on calcule leurs positions dans la fenetre*/
            int x1 = p1.x * RATIO_X;
            int y1 = (Position.HAUTEUR_MAX - p1.y) * RATIO_Y;
            int x2 = p2.x * RATIO_X;
            int y2 = (Position.HAUTEUR_MAX - p2.y) * RATIO_Y;
            /*on dessine une ligne entre les deux points*/
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public void DessineOiseau(Graphics2D g2d) {
        /*on lit un giff animé et on le dessine à la position (x, y)*/
        for (Point p : Oiseaux.getOiseaux()){
            g2d.drawImage(new ImageIcon("src/Images/birdNoBg.gif").getImage(), p.x, p.y, null);
        }
    }

    public void DessinerNuages(Graphics2D g2d) {
        g2d.setColor(Color.white);
        for (Point p : ciel.getNuages()) {
            g2d.fillOval(p.x, p.y, 60, 40);
            g2d.fillOval(p.x + 30, p.y - 10, 60, 50);
            g2d.fillOval(p.x + 60, p.y + 5, 50, 30);
        }
    }

    public void DessinerSol(Graphics2D g2d) {
        /*couleur verte pour le sol*/
        g2d.setColor(new Color(34, 139, 34));
        /*on dessine un rectangle en bas de la fenêtre*/
        g2d.fillRect(0, ZONE_HAUT - 50, ZONE_LARG, 50);
        g2d.setColor(Color.black);
    }

    public void DessineOval(Graphics2D g2d, int y, boolean Gauche) {
        /*on dessine l'oval en deux parties pour qu'elle puisse passer derrière et devant la ligne brisée*/
        /*épaisseur de la partie de l'oval*/
        int epaisseur = 8;
        /* on sauvegarde le stroke d'origine */
        Stroke ancienStroke = g2d.getStroke();
        /* on définit un nouveau stroke avec l'épaisseur désirée */
        g2d.setStroke(new BasicStroke(epaisseur, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        /* On définit les couleurs du dégradé */
        Color rougeClair = new Color(250,0,0);
        Color rougeFonce = new Color(200, 0, 0);

        /* Création du dégradé vertical (du haut 'y' vers le bas 'y + HAUTEUR_OVAL')*/
        GradientPaint degrade = new GradientPaint(
            POSITION_X + LARG_OVAL, y, rougeClair,
                POSITION_X, y, rougeFonce
        );

        /* Applique le dégradé */
        g2d.setPaint(degrade);

        if (Gauche) {
            /*Dessine l'arc gauche avec le dégradé*/
            g2d.drawArc(POSITION_X, y , LARG_OVAL, HAUTEUR_OVAL, 90, 180);
        } else {
            /* Dessine l'arc droit avec le dégradé */
            g2d.drawArc(POSITION_X, y, LARG_OVAL, HAUTEUR_OVAL, -90, 180);
        }

        /* On remet la couleur noire et le stroke d'origine pour la suite des dessins*/
        g2d.setPaint(null);
        g2d.setColor(Color.black);
        g2d.setStroke(ancienStroke);
        g2d.setStroke(ancienStroke);

    }


    @Override
    public void paintComponent(Graphics g){
        /*on appelle la méthode parente pour effacer l'ancien dessin*/
        super.paintComponent(g);
        /*on active l'anti-aliasing pour un rendu plus lisse*/
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        /*on calcule la position y de l'oval en fonction de la position dans le modèle*/
        int y = (Position.HAUTEUR_MAX - maPosition.getPosition()) * RATIO_Y;


        /*on dessine les nuages*/
        DessinerNuages(g2d);

        /*on dessine le sol*/
        DessinerSol(g2d);

        /* on dessine la partie gauche de l'ovale pour qu'elle se retrouve derrière la ligne brisée*/
        DessineOval(g2d, y, true);

        /*on dessine le parcours*/
        dessinerParcours(g2d);

        /* on dessine la partie droite de l'ovale pour qu'elle se retrouve devant la ligne brisée */
        DessineOval(g2d, y, false);

        /*on dessine un oiseau*/
        DessineOiseau(g2d);

        /*on dessine le score*/
        dessinerScore(g2d);

        g2d.setColor(Color.black);

    }


}