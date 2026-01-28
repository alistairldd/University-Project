import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ControleurButton implements ActionListener {


    CModele modele;
    CVue vue;
    VueCommandes vueCommandes;

    public ControleurButton(CModele modele, CVue vue, VueCommandes vueCommandes) {
        this.modele = modele;
        this.vue = vue;
        this.vueCommandes = vueCommandes;

    }



    public void actionPerformed(ActionEvent e) {
        if (modele.getIsOver()) return;
        if (modele.getIsWin()) return;

        modele.getJoueurActuel().finito(); // si un joueur est sur l'heliport, on vérifie la condition de victoire

        JButton source = (JButton) e.getSource();

        if (source.getText().equals("Fin de tour")) { // fin de tour -> inonder + changer joueur + aléatoire : (clé + item)
            modele.inonderAlea(modele.getDifficulte());
            modele.resetAction();
            vue.setTexte("");

            modele.getJoueurActuel().aleaItem();
            modele.getJoueurActuel().resetPouvoir();
            modele.desactiverTout();


            if (modele.getJoueurActuel().cantMove()) {
                modele.setIsOver();
            }

            modele.changerJoueurActuel();
            modele.notifyObservers();

        } else if (source.getText().equals("Hélicoptère")) { // si on appuie sur le bouton hélico, on regarde si on peut se tp
            if (modele.getJoueurActuel().getItems().contains(Item.HELICO)) {
                vue.setTexte("Choisissez la case où vous déplacer");
                modele.setHelico(true);
            } else {
                vue.setTexte("Vous n'avez pas d'hélicoptère!");
            }
            modele.notifyObservers();

        } else if (source.getText().equals("Sac de sable")) { // si on appuie sur le bouton sac de sable, on regarde si on peut assecher
            if (modele.getJoueurActuel().getItems().contains(Item.SABLE)) {
                vue.setTexte("Choisissez la case à assécher");
                modele.setSable(true);

            } else {
                vue.setTexte("Vous n'avez pas de sable!");
            }
            modele.notifyObservers();

        } else if (modele.getAction() > 0) {

            if (source.getText().equals("Assécher")) {
                vue.setTexte("Sélectionnez une case à assécher");
                modele.desactiverTout();
                modele.activerModeAssechement();
                modele.notifyObservers();
            }

            if (source.getText().equals("Prendre un artefact")) { // prends l'artefact de la case actuelle si on a la bonne clé
                if (modele.getJoueurActuel().recupArtefact()) {
                    vue.setTexte("Vous avez réussi à prendre l'artefact");
                    modele.baisserAction();
                } else {
                    vue.setTexte("Vous n'avez pas pu prendre l'artefact");
                }
                modele.notifyObservers();
            }

            if (source.getText().equals("Pouvoir")) { // utilisation des povoirs
                modele.desactiverTout();
                if (modele.getJoueurActuel().getPouvoir()) {vue.setTexte("Pouvoir déjà utilisé");}
                else {
                    switch (modele.getJoueurActuel().getRole()) {
                        case "Pilote" -> {
                            vue.setTexte("Cliquez sur la zone où vous téléporter");
                            modele.activeTP();

                        }
                        case "Navigateur" -> {
                            vue.setTexte("Sélectionnez un joueur à déplacer");
                            modele.activePvrNavi();
                        }
                        case "Ingénieur", "Explorateur", "Plongeur", "Chômeur" -> vue.setTexte("Vous n'avez pas de pouvoir");
                        default -> System.out.println("b");
                    }
                }
                modele.notifyObservers();
            }

        } else { vue.setTexte("Vous n'avez plus d'actions disponible");
            modele.notifyObservers();}

        if (source.getText().equals("Facile") || source.getText().equals("Normal")) {
            if (source.getText().equals("Facile")) modele.setDifficulte(1);
            if (source.getText().equals("Normal")) modele.setDifficulte(3);
            modele.setHasStarted();
            //vue.addLayeredPane();

            vue.getVueCommande().initGame(this);
        }

        if (source.getText().equals("2") || source.getText().equals("3") || source.getText().equals("4") || source.getText().equals("5") || source.getText().equals("6")) {
            if (source.getText().equals("2")) modele.setNbJoueur(2);
            if (source.getText().equals("3")) modele.setNbJoueur(3);
            if (source.getText().equals("4")) modele.setNbJoueur(4);
            if (source.getText().equals("5")) modele.setNbJoueur(5);
            if (source.getText().equals("6")) modele.setNbJoueur(6);
            modele.setHasPartiallyStarted();
            vue.addLayeredPane();
            vue.getVueCommande().initPreGame(this);

        }


        vue.getFrame().requestFocusInWindow();
    }

}


