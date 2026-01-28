
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


public class VueJoueur extends JPanel implements Observer {


    private final CModele modele;
    private final Joueur[] joueurs;
    private BufferedImage joueur1;
    private BufferedImage joueur2;
    private BufferedImage joueur3;
    private BufferedImage joueur4;
    private BufferedImage joueur5;
    private BufferedImage joueur6;





    public VueJoueur(CModele modele) {
        this.modele = modele;
        this.joueurs = modele.getJoueur();
        modele.addObserver(this);

        Dimension dim = new Dimension(50, 50);
        this.setPreferredSize(dim);
        this.setOpaque(false);


        try{
            joueur1 = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/joueur1.png")));
            joueur2 = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/joueur2.png")));
            joueur3 = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/joueur3.png")));
            joueur4 = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/joueur4.png")));
            joueur5 = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/joueur5.png")));
            joueur6 = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/joueur6.png")));


        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void update() { repaint(); }


    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (!modele.getIsOver() && ! modele.getIsWin()) {
            for (Joueur joueur : joueurs) {
                if (joueur.getId() <= modele.getNbJoueur()){
                    switch (joueur.getId()) {
                        case 1:
                            g.drawImage(joueur1, joueur.getCol() * modele.tailleTuile + 10, joueur.getLig() * modele.tailleTuile + 2, joueur.tailleJoueur, joueur.tailleJoueur, this);

                            if (joueur == modele.getJoueurActuel()) g.setColor(joueur.getColor());
                            else g.setColor(Color.black);
                            g.drawRect(joueur.getCol() * modele.tailleTuile + 10, joueur.getLig() * modele.tailleTuile + 2, joueur.tailleJoueur, joueur.tailleJoueur);
                            break;
                        case 2:

                            g.drawImage(joueur2, joueur.getCol() * modele.tailleTuile + 60, joueur.getLig() * modele.tailleTuile + 2, joueur.tailleJoueur, joueur.tailleJoueur, this);

                            if (joueur == modele.getJoueurActuel()) g.setColor(joueur.getColor());
                            else g.setColor(Color.black);
                            g.drawRect(joueur.getCol() * modele.tailleTuile + 60, joueur.getLig() * modele.tailleTuile + 2, joueur.tailleJoueur, joueur.tailleJoueur);
                            break;

                        case 3:
                            g.drawImage(joueur3, joueur.getCol() * modele.tailleTuile + 10, joueur.getLig() * modele.tailleTuile + 2 + joueur.tailleJoueur, joueur.tailleJoueur, joueur.tailleJoueur, this);

                            if (joueur == modele.getJoueurActuel()) g.setColor(joueur.getColor());
                            else g.setColor(Color.black);
                            g.drawRect(joueur.getCol() * modele.tailleTuile + 10, joueur.getLig() * modele.tailleTuile + 2 + joueur.tailleJoueur, joueur.tailleJoueur, joueur.tailleJoueur);
                            break;

                        case 4:
                            g.drawImage(joueur4, joueur.getCol() * modele.tailleTuile + 60, joueur.getLig() * modele.tailleTuile + 2 + joueur.tailleJoueur, joueur.tailleJoueur, joueur.tailleJoueur, this);

                            if (joueur == modele.getJoueurActuel()) g.setColor(joueur.getColor());
                            else g.setColor(Color.black);
                            g.drawRect(joueur.getCol() * modele.tailleTuile + 60, joueur.getLig() * modele.tailleTuile + 2 + joueur.tailleJoueur, joueur.tailleJoueur, joueur.tailleJoueur);
                            break;
                        case 5:
                            g.drawImage(joueur5, joueur.getCol() * modele.tailleTuile + 10, joueur.getLig() * modele.tailleTuile + 2 + 2 * joueur.tailleJoueur, joueur.tailleJoueur, joueur.tailleJoueur, this);

                            if (joueur == modele.getJoueurActuel()) g.setColor(joueur.getColor());
                            else g.setColor(Color.black);
                            g.drawRect(joueur.getCol() * modele.tailleTuile + 10, joueur.getLig() * modele.tailleTuile + 2 + 2 * joueur.tailleJoueur, joueur.tailleJoueur, joueur.tailleJoueur);
                            break;
                        case 6:
                            g.drawImage(joueur6, joueur.getCol() * modele.tailleTuile + 60, joueur.getLig() * modele.tailleTuile + 2 + 2 * joueur.tailleJoueur, joueur.tailleJoueur, joueur.tailleJoueur, this);

                            if (joueur == modele.getJoueurActuel()) g.setColor(joueur.getColor());
                            else g.setColor(Color.black);
                            g.drawRect(joueur.getCol() * modele.tailleTuile + 60, joueur.getLig() * modele.tailleTuile + 2 + 2 * joueur.tailleJoueur, joueur.tailleJoueur, joueur.tailleJoueur);
                            break;

                        default:
                            System.out.println("Erreur des joueurs");
                            break;
                    }
                }
            }
        }
    }

}
