
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControleurMvt implements KeyListener {

    private final CModele modele;
    public ControleurMvt (CModele modele) { this.modele = modele;}

    public void keyPressed(KeyEvent e) {
        if (!modele.getHasStarted()) return;
        if (modele.getAction() > 0) {
            switch (e.getKeyCode()) {
                // HAUT BAS GAUCHE DROITE
                case KeyEvent.VK_NUMPAD8, KeyEvent.VK_UP:  modele.getJoueurActuel().allerHaut(); break;
                case KeyEvent.VK_NUMPAD2, KeyEvent.VK_DOWN:  modele.getJoueurActuel().allerBas(); break;
                case KeyEvent.VK_NUMPAD4, KeyEvent.VK_LEFT:  modele.getJoueurActuel().allerGauche(); break;
                case KeyEvent.VK_NUMPAD6, KeyEvent.VK_RIGHT:  modele.getJoueurActuel().allerDroite(); break;
                // DIAG HG HD BG BD
                case KeyEvent.VK_NUMPAD7:  modele.getJoueurActuel().allerHautGauche(); break;
                case KeyEvent.VK_NUMPAD9:  modele.getJoueurActuel().allerHautDroite(); break;
                case KeyEvent.VK_NUMPAD1:  modele.getJoueurActuel().allerBasGauche(); break;
                case KeyEvent.VK_NUMPAD3:  modele.getJoueurActuel().allerBasDroite(); break;
                default: break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
