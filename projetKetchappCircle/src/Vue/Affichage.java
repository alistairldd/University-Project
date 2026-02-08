package Vue;

import Model.Defilement;
import Model.Descendre;
import Model.Parcours;
import Model.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;

public class Affichage extends JPanel {

    /*constantes permettant de définir la taille de la fenetre*/
    public final static int RATIO_X = 7;
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
    private final int position_y = (Position.HAUTEUR_MAX - Position.POS_DEPART - Position.HAUTEUR_OVAL)*RATIO_Y;

    /*le modèle qui donne le parcours*/
    private final Parcours monParcours;

    /*le modèle qui done la position*/
    private final Position maPosition;

    // constante représentant le ciel pour les nuages
    private final Ciel ciel;

    // constante représentant l'image des oiseaux
    private final Image oiseauImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/birdNoBg.gif"))).getImage();


    // Bouton pour recommencer la partie, caché par défaut
    private final JButton boutonRecommencer = new JButton("Rejouer");
    // Bouton pour démarrer la partie, visible par défaut
    private final JButton boutonStart = new JButton("Start");

    // Getters pour les boutons (si besoin dans le contrôleur)
    public JButton getBoutonStart() { return boutonStart; }
    public JButton getBoutonReplay() { return boutonRecommencer; }


    /* constructeur de la classe Affichage, prend en paramètre les modèles nécessaires pour dessiner*/
    public Affichage(Position po, Parcours pa, Ciel c) {
        /*on définit la taille préférée du JPanel*/
        setPreferredSize(new Dimension(ZONE_LARG,ZONE_HAUT));
        maPosition = po;
        monParcours = pa;
        ciel = c;
        this.setBackground(new Color(135, 206, 235));

        // on veut placer le bouton manuellement
        this.setLayout(null);

        boutonStart.setBounds(ZONE_LARG/2 - 50, ZONE_HAUT/2 - 15, 100, 30);
        this.add(boutonStart);


        // On le centre
        boutonRecommencer.setBounds(ZONE_LARG/2 - 50, ZONE_HAUT/2, 100, 30);
        boutonRecommencer.setVisible(false);
        this.add(boutonRecommencer);
    }

    /*méthode pour dessiner le score
    * on le dessine simplement comme un texte
    * */
    public void DessinerScore(Graphics g) {
        Font font = g.getFont();
        Font nvFont = new Font("Monospaced", Font.BOLD, 20);
        g.setFont(nvFont);


        g.drawString("Score : " + Model.Score.getScore(), 10, 20);
        g.setFont(font);
    }


    /*méthode pour dessiner le parcours
    * on dessine une ligne brisée entre les points du parcours
    * on doit faire attention à inverser la position Y pour que le dessin corresponde au modèle
    * */
    public void DessinerParcours(Graphics g) {
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

    /*méthode pour dessiner les oiseaux
    * on récupère les positions des oiseaux depuis le modèle
    * et on dessine l'image à ces positions
     */
    public void DessineOiseau(Graphics2D g2d) {
        if (oiseauImage == null) return;
        List<Point> positions = Oiseaux.getOiseaux();
        for (Point p : positions) {

            g2d.drawImage(oiseauImage, p.x, p.y, null);
        }
    }

    /*méthode pour dessiner les nuages
    * on récupère les positions des nuages depuis le modèle
    * et on dessine des ovales blancs à ces positions
     */
    public void DessinerNuages(Graphics2D g2d) {
        g2d.setColor(Color.white);
        for (Point p : ciel.getNuages()) {
            g2d.fillOval(p.x, p.y, 60, 40);
            g2d.fillOval(p.x + 30, p.y - 10, 60, 50);
            g2d.fillOval(p.x + 60, p.y + 5, 50, 30);
        }
        g2d.setColor(Color.black);
    }

    /*méthode pour dessiner le sol
    * on dessine simplement un rectangle vert en bas de la fenêtre
     */
    public void DessinerSol(Graphics2D g2d) {
        /*couleur verte pour le sol*/
        g2d.setColor(new Color(34, 139, 34));
        /*on dessine un rectangle en bas de la fenêtre*/
        g2d.fillRect(0, ZONE_HAUT - 50, ZONE_LARG, 50);
        g2d.setColor(Color.black);
    }

    /*méthode pour dessiner l'oval représentant le personnage
    * on dessine un arc pour la partie gauche et un arc pour la partie droite
    * on applique un dégradé de couleur pour donner du relief à l'oval
    * on dessine la partie gauche de l'oval avant la ligne brisée pour qu'elle puisse passer derrière, et la partie droite après pour qu'elle puisse passer devant
    * */
    public void DessineOval(Graphics2D g2d, int y, boolean Gauche) {
        /*on dessine l'oval en deux parties pour qu'elle puisse passer derrière et devant la ligne brisée*/
        /*épaisseur de la partie de l'oval*/
        int epaisseur = 8;
        /* on sauvegarde le stroke d'origine */
        Stroke ancienStroke = g2d.getStroke();
        /* on définit un nouveau stroke avec l'épaisseur désirée */
        g2d.setStroke(new BasicStroke(epaisseur, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        /* On définit les couleurs du dégradé */
        Color rougeClair = new Color(200,50,250);
        Color rougeFonce = new Color(100, 50, 200);

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

    /*méthode pour dessiner le message de fin
    * on affiche simplement un texte au centre de la fenêtre
     */
    public void DessinerFin(Graphics g2d){
        Font font = g2d.getFont();
        Font nvFont = new Font("Monospaced", Font.BOLD, 20);
        g2d.setFont(nvFont);

        /*affichage du message de fin*/
        g2d.drawString("PERDIDO", ZONE_LARG/2 -40,ZONE_HAUT/2);
        g2d.setFont(font);
    }

    /*méthode pour dessiner la barre de vie
    * on dessine un rectangle dont la largeur dépend du nombre de vies restantes
    * la couleur du rectangle change du vert au rouge en fonction du nombre de vies restantes
     */
    public void DessinerVie(Graphics g2d) {
        /*affichage barre de vie*/
        /*entre 0 et 50*/
        int vie = Position.getNbVies();
        /*calcul du ratio de vie*/
        float ratio = vie / 50f;
        int rouge, vert;
        if (ratio > 0.5) {
            /* De 25 à 50 : Transition Jaune -> Vert
            * Le rouge diminue de 255 à 0, le vert reste à 255 */
            rouge = (int) (255 * (1 - ratio) * 2);
            vert = 255;
        } else {
            /* De 0 à 25 : Transition Rouge -> Jaune
            * Le rouge reste à 255, le vert augmente de 0 à 255 */
            rouge = 255;
            vert = (int) (255 * ratio * 2);
        }

        /*choix de la couleur pour la barre de vie*/
        g2d.setColor(new Color(rouge, vert, 0));

        /*dessin de la barre de vie*/
        g2d.fillRect(ZONE_LARG - 300, 20, vie * 5, 30);

        /*reset de la couleur*/
        g2d.setColor(Color.black);

    }

    /*méthode pour dessiner un rectangle rouge clignotant pour signaler un danger imminent
    * on utilise un dégradé radial pour créer l'effet de clignotement
    * la couleur devient plus rouge et plus opaque à mesure que le nombre de vies diminue
     */
    public void DessinerDanger(Graphics2D g2d) {

        int width = ZONE_LARG;
        int height = ZONE_HAUT;
        int nbVies = Position.getNbVies();
        /* calcul du ratio de vie pour ajuster les couleurs du dégradé
         * plus le nombre de vies est bas, plus les couleurs deviennent rouges et opaques */
        float nbVieFloat = nbVies/50f;

        float radius = width * 0.75f; // Rayon du cercle de clignotement
        Point2D center = new Point2D.Float(width / 2f, height / 2f); // Centre de la zone de clignotement

        /*plus le nombre de vies est bas, plus les couleurs deviennent rouges et opaques*/
        float[] dist = {0.1f, Math.min(0.35f+nbVieFloat,0.9f), Math.min(0.75f+nbVieFloat, 1.0f)}; // Répartition des couleurs dans le dégradé
        Color[] colors = {
            new Color(255, 0, 0, 0) ,   // Transparent
            new Color(255, 0, 0, 100),  // Rouge plus transparent
            new Color(255, 0, 0, 200) // Rouge semi-transparent
        };

        /*création du dégradé radial et application*/
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(p);
        g2d.fillRect(0, 0, width, height);
    }


    /*redéfinition de paintComponent pour dessiner tous les éléments du jeu
    * on dessine dans l'ordre : le sol, la partie gauche de l'oval,
    * les nuages, les oiseaux, le parcours, la partie droite de l'oval,
    * le rectangle de danger, la barre de vie, le score, et enfin le message de fin si le jeu est fini
     */
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


        /*on dessine le sol*/
        DessinerSol(g2d);

        /* on dessine la partie gauche de l'ovale pour qu'elle se retrouve derrière la ligne brisée*/
        DessineOval(g2d, y, true);

        /*on dessine les nuages*/
        DessinerNuages(g2d);

        /*on dessine un oiseau*/
        DessineOiseau(g2d);

        /*on dessine le parcours*/
        DessinerParcours(g2d);

        /* on dessine la partie droite de l'ovale pour qu'elle se retrouve devant la ligne brisée */
        DessineOval(g2d, y, false);


        /*on dessine un rectangle rouge clignotant pour signaler un danger imminent*/
        DessinerDanger(g2d);

        /*on dessine les vies */
        DessinerVie(g2d);
        /*on dessine le score*/
        DessinerScore(g2d);

        /*si le jeu est fini, on dessine le texte de fin*/
        if (Position.getJeuFini()){
            DessinerFin(g2d);
            if (!boutonRecommencer.isVisible()) {
                boutonRecommencer.setVisible(true);
            }
        }


    }


}