import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CModele extends Observable {

    public int tailleTuile = 100;
    public int nbJoueur;
    private final Tuile[][] grille;
    private final Joueur[] joueurs;
    private int action = 3;
    private int joueurActuel = 0;
    private int difficulte;

    private boolean isOver = false;
    private boolean isWin = false;
    private boolean hasStarted = false;
    private boolean hasPartiallyStarted = false;

    private boolean modeAssechement = false;
    private boolean sable = false;
    private boolean helico = false;
    private boolean teleportation = false; //pvrPilote
    private boolean pvrNavi = false;
    private boolean navi = false; // Intermédiaire à l'utilisation du pouvoir du navi

    public CModele() {
        // constructeur du modèle
        grille = new Tuile[6][6];
        nbJoueur = 6;
        joueurs = new Joueur[nbJoueur];
        difficulte = 3;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                grille[i][j] = new Tuile("Tuile" + (i * 6 + j + 1));
            }
        }
        //On enlève les coins
        grille[0][0] = null;
        grille[0][1] = null;
        grille[1][0] = null;
        grille[0][4] = null;
        grille[0][5] = null;
        grille[1][5] = null;
        grille[5][0] = null;
        grille[4][0] = null;
        grille[5][1] = null;
        grille[5][5] = null;
        grille[5][4] = null;
        grille[4][5] = null;




        int tmp = 0;

        // on initialise une case en héliport

        int xHel;
        int yHel;

        do {
            xHel = (int)(Math.random() * 6);
            yHel = (int)(Math.random() * 6);
            if (grille[xHel][yHel] != null) {
                grille[xHel][yHel].setHeliportTrue();
                tmp ++;
            }
        } while (tmp <1);

        tmp = 0;

        // on initialise 4 cases contenant les coffres des artéfacts

        do {
            int xc = (int)(Math.random() * 6);
            int yc = (int)(Math.random() * 6);
            if (grille[xc][yc] != null && !grille[xc][yc].isHeliport() && grille[xc][yc].getCoffre() == null) {

                Element elem = switch (tmp) {
                    case 0 -> Element.EAU;
                    case 1 -> Element.FEU;
                    case 2 -> Element.TERRE;
                    case 3 -> Element.VENT;
                    default -> null;
                };

                grille[xc][yc].setCoffreT(elem);
                tmp ++;
            }

        } while (tmp <=3);


        // on initialise les joueurs avec différentes classes
        // Pilote, Ingénieur, Explorateur, Navigateur, Plongeur
        ArrayList<String> roles =  new ArrayList<>(6);
        roles.add("Pilote");
        roles.add("Ingénieur");
        roles.add("Explorateur");
        roles.add("Navigateur");
        roles.add("Plongeur");
        roles.add("Chômeur");
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * (5-i));
            joueurs[i] = (new Joueur(this, xHel, yHel, roles.get(index)));
            roles.remove(index);
        }


    }

    /** Getters */
    public int getNbJoueur() {return nbJoueur;}

    public Joueur[] getJoueur() { return joueurs; }

    public Tuile getTuile(int lig, int col) { return grille[lig][col]; }

    public int getAction() { return action; }

    public Joueur getJoueurActuel() { return joueurs[joueurActuel] ; }

    public boolean getModeAssechement() { return modeAssechement ; }

    public boolean getPvrNavi() { return pvrNavi ; }

    public boolean getNavi() { return navi ; }

    public Tuile getHeliport(){
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (grille[i][j] != null && grille[i][j].isHeliport()) {
                    return grille[i][j];
                }
            }
        }
        return null;
    }

    public boolean getSable() {
        return sable;
    }

    public boolean getHelico() {return helico; }
    public boolean getTeleportation() { return teleportation; }
    public boolean getIsOver() { return isOver; }
    public boolean getIsWin() { return isWin; }
    public int getDifficulte() { return difficulte;}
    public boolean getHasStarted() { return hasStarted; }
    public boolean getHasPartiallyStarted() { return hasPartiallyStarted; }


    /** Setters */
    public void setNbJoueur(int nbJ) {this.nbJoueur = nbJ;}
    public void setDifficulte(int difficulte) { this.difficulte = difficulte; }
    public void setHasStarted() { this.hasStarted = true; }
    public void setHasPartiallyStarted() { this.hasPartiallyStarted = true; }
    public void setHelico(boolean helico) {this.helico = helico;}
    public void activeTP() { teleportation = true; }
    public void activePvrNavi() { pvrNavi = true; }
    public void desactivePvrNavi() { pvrNavi = false; }
    public void activeNavi() { navi = true; }
    public void desactiveNavi() { navi = false; }

    public void baisserAction() { action -- ; notifyObservers(); }

    public void resetAction() { action = 3; notifyObservers(); }

    public void changerJoueurActuel() { joueurActuel = (joueurActuel +1) % nbJoueur; }

    public void setSable(boolean sable) {
        this.sable = sable;
    }

    public void activerModeAssechement() { modeAssechement = true; }

    public void desactiverTout(){
         modeAssechement = false;
         sable = false;
         helico = false;
         teleportation = false;
         pvrNavi = false;
         navi = false;
    }





    public void inonderAlea(int nb) {// appelé à chaque fin de tour pour inonder 1 ou 3 cases selon la difficulté
        Set<Tuile> dejaInondees = new HashSet<>(); // pour mémoriser les tuiles déjà inondées

        int tmp = 0;
        do {
            int irandom = (int)(Math.random() * 6);
            int jrandom = (int)(Math.random() * 6);
            Tuile t = grille[irandom][jrandom];

            if (t != null && t.getEtat() != Etat.SUBMERGEE && !dejaInondees.contains(t)) {
                t.inonder();
                dejaInondees.add(t); // on l'ajoute aux cases déjà traitées
                tmp++;
            }
        } while (tmp != nb);

        this.checkIsOver();
        this.notifyObservers();
    }

    public boolean tenterAssechement(int lig, int col) {

        Joueur joueur = this.getJoueurActuel();
        int joueurLig = joueur.getLig();
        int joueurCol = joueur.getCol();


        if (((Math.abs(joueurLig - lig) <= 1 && joueurCol == col) || (Math.abs(joueurCol - col) <= 1 && joueurLig == lig)) ||
                (Objects.equals(joueur.getRole(), "Explorateur") && Math.abs(joueurLig - lig) == 1 && Math.abs(joueurCol - col) == 1 )) { // L'explorateur peut assécher en diagonale

            if (this.getTuile(lig, col) != null && this.getTuile(lig, col).getEtat() == Etat.INONDEE) {
                if (action > 0) {
                    this.assecher(lig, col);
                    this.modeAssechement = false;
                    if ((!Objects.equals(joueur.getRole(), "Ingénieur") || joueur.getPouvoir())) {
                        this.baisserAction();
                    } else {joueur.usePouvoir();}
                    return true;
                }
            }
        }
        this.modeAssechement = false;
        return false;
    }

    public boolean tenterSable(int lig, int col) { // pour assecher une case sur toute la carte
        this.setSable(false);
        if (this.getTuile(lig, col) != null && this.getTuile(lig, col).getEtat() == Etat.INONDEE) {
            this.assecher(lig, col);
            this.getJoueurActuel().rmItem(Item.SABLE);
            return true;
        } else return false;
    }

    public boolean tenterHelico(int lig, int col) { // pour se téléporter sur une case
        this.setHelico(false);
        if (this.getTuile(lig, col) != null && this.getTuile(lig, col).getEtat() != Etat.SUBMERGEE) {
            this.getJoueurActuel().setPos(lig, col);
            this.getJoueurActuel().rmItem(Item.HELICO);
            return true;
        }

        return false;
    }

    public boolean tenterTeleportation(int lig, int col) { // pouvoir de l'helico permettant de se téléporter sur une case
        teleportation = false;
        Tuile tuile  = this.getTuile(lig, col);
        if (tuile != null && tuile.getEtat() != Etat.SUBMERGEE) {
            this.getJoueurActuel().setPos(lig, col);
            this.getJoueurActuel().usePouvoir();
            this.baisserAction();
            return true;
        } else return false;
    }

    public boolean deplacerAutreJoueur(Joueur cible, int lig, int col) {
        Joueur joueurActuel = this.getJoueurActuel();

        // Vérifie que la cible est à portée (2 cases max adjacentes, pas diagonales)
        if (Math.abs(cible.getLig() - lig) + Math.abs(cible.getCol() - col) > 2) return false;

        // Que l'explorateur qui peut aller en diagonal
        if ((!Objects.equals(cible.getRole(), "Explorateur")) && Math.abs(cible.getLig() - lig) != 0 && Math.abs(cible.getCol() - col) != 0) return false;
        Tuile tuile = this.getTuile(lig, col);
        if (tuile != null &&  (Objects.equals(cible.getRole(), "Plongeur") || tuile.getEtat() != Etat.SUBMERGEE)) {
            cible.setPos(lig, col);
            joueurActuel.usePouvoir();
            this.baisserAction();
            this.notifyObservers();
            return true;
        }
        return false;
    }



    public void assecher(int lig, int col) {
        grille[lig][col].assecher();
        this.notifyObservers();
    }

    public void setIsOver() { this.isOver = true; }

    public void checkIsOver() { // vérifie si les conditions sont verifiées pour une perte de la partie

        int cpt = 0;
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
                if (grille[i][j] != null) {
                    if (grille[i][j].getEtat() == Etat.INONDEE) {
                        cpt++;
                    }
                }
            }
        } // conditions : héliport inondé ; un joueur est noyé
        this.isOver = (getHeliport().getEtat() == Etat.SUBMERGEE || cpt >= 23 || this.getJoueurActuel().cantMove()); // on met 23 pour que ça finisse si toutes les cases sont inondées sauf l'héliport
    }

    public void setIsWin() {
        Set<Element> list = new HashSet<>();
        for (Joueur j : joueurs) {
            list.addAll(j.getArtefacts());
        }

        // on vérifie que tous les artéfacts sont récupérés
        if (list.size() == 4 && list.contains(Element.FEU) && list.contains(Element.TERRE) && list.contains(Element.VENT) && list.contains(Element.EAU)) {
            for (Joueur j : joueurs) {

                if (!this.getTuile(j.getLig(), j.getCol()).isHeliport()) { // on vérifie que tous les joueurs sont à l'heliport
                    return;
                }
            }
            if (this.getJoueurActuel().getItems().contains(Item.HELICO)) { // on vérifie que le joueur actuel possède un hélico
                this.isWin = true;
            }

        }
    }

}
