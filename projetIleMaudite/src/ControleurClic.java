import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ControleurClic implements MouseListener, MouseMotionListener {

    private final CModele modele;
    private final CVue vue;

    private Joueur cible;

    public ControleurClic(CModele modele, CVue cvue) {
        this.modele = modele;
        this.vue = cvue;
        vue.getVueIle().addMouseMotionListener(this);
    }


    public void mouseClicked(MouseEvent e) {
        int col = e.getX() / modele.tailleTuile;
        int lig = e.getY() / modele.tailleTuile;


        if (modele.getIsOver()) return;
        if (modele.getIsWin()) return;
        if (modele.getModeAssechement()) { // si on est en mode assechement la case sur laquelle on clique s'asseche (si innondée et si distance(joueur, case) <=1)
            boolean res = modele.tenterAssechement(lig, col);
            if (res) vue.setTexte("Assèchement réussi");
            else vue.setTexte("Assèchement impossible");
            modele.notifyObservers();
        }

        if (modele.getSable()){ // pareil mais n'importe quelle case innondée
            boolean res = modele.tenterSable(lig, col);
            if (res) vue.setTexte("Sable réussi");
            else vue.setTexte("Le sable a échoué");
            modele.notifyObservers();
        }

        if (modele.getHelico()){ // on se déplace sur n'importe quelle case si pas submergée
            boolean res = modele.tenterHelico(lig, col);
            if (res) vue.setTexte("Hélicoptère réussi");
            else vue.setTexte("Hélicoptère a échoué");
            modele.notifyObservers();
        }

        if(modele.getTeleportation()){ // teleportation (pouvoir) sur n'importe quelle case si pas submergée
            boolean res = modele.tenterTeleportation(lig, col);
            if (res) vue.setTexte("Téléportation réussie !");
            else vue.setTexte("Téléportation échouée");
            modele.notifyObservers();
        }

        if (modele.getPvrNavi()) { //pouvoir du navigateur pour déplacer quelqu'un
            this.cible = trouveJoueur(e.getX(), e.getY());
            if (cible != null) {
                vue.setTexte("<html>Sélectionnez une case (adjacente de 1 ou 2) <br>  où déplacer le joueur " + cible.getRole() +"</html>");
                modele.activeNavi();
                modele.desactivePvrNavi();
                modele.notifyObservers();
            }

        } else if (modele.getNavi()) {
            boolean res = modele.deplacerAutreJoueur(cible, lig, col);
            if (res) {vue.setTexte("Déplacement réussie !"); modele.desactiveNavi();}
            else vue.setTexte("<html>Déplacement échouée, sélectionnez une ou deux case(s) <br> adjacente(s) au joueur, pas en diagonal sauf pour l'explorateur</html>");
            modele.notifyObservers();
        }


    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) { // pour illuminer les tuiles sur lesquelles, on passe la souris
        if (modele.getIsOver()) return;
        if (modele.getIsWin()) return;
        int x = e.getX() / modele.tailleTuile;
        int y = e.getY() / modele.tailleTuile;


        if (x < 6 && y < 6 && x >= 0 && y >= 0) {
            Tuile tuilesurvolee = modele.getTuile(y, x);
            if (tuilesurvolee != null) {
                vue.getVueIle().setTuileSurvolee(tuilesurvolee);
            }
        }
    }

    public Joueur trouveJoueur(int x, int y) { // Trouve le joueur à l'aide de sa position en pixel
        for (Joueur joueur : modele.getJoueur()) {
            int posPixelX; int posPixelY;
            if (joueur.getId() % 2 == 1) posPixelX = joueur.getCol() * modele.tailleTuile + 10;
            else posPixelX = joueur.getCol() * modele.tailleTuile + 60;

            posPixelY = joueur.getLig() * modele.tailleTuile + 2 + ((joueur.getId()-1) / 2) * joueur.tailleJoueur;

            if (x >= posPixelX && x <= posPixelX + joueur.tailleJoueur &&
                    y >= posPixelY && y <= posPixelY + joueur.tailleJoueur) return joueur;
        }
        return null; //Aucun joueur cliqué
    }

}
