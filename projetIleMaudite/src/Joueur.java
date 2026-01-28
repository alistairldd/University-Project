import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Joueur {

    public final int tailleJoueur = 32;

    private final CModele modele;
    private int lig;
    private int col;
    private final Color color;


    private boolean powerUsed;
    private final String role;

    private final int id;
    static int cpt = 1;

    private final List<Element> key;
    private final List<Element> artefacts;
    private final List<Item> items;

    public Joueur(CModele modele, int lig, int col, String role) {
        this.modele = modele;
        this.lig = lig;
        this.col = col;
        this.id = cpt;
        this.role = role;
        this.key = new ArrayList<>();
        this.artefacts = new ArrayList<>();
        this.items = new ArrayList<>();
        this.color =   switch (cpt) {
            case 1 -> Color.GREEN;
            case 2 -> Color.RED;
            case 3 -> Color.MAGENTA;
            case 4 -> Color.ORANGE;
            case 5 -> Color.BLUE;
            case 6 -> Color.CYAN;
            default -> Color.GRAY;
        };
        cpt++;


    }



    public int getLig() { return this.lig; }
    public int getCol() { return this.col; }
    public int getId() { return this.id; }

    public List<Element> getKey() {
        return this.key;
    }
    public List<Item> getItems() {return this.items; }

    public String getRole() { return role;}

    public String getKeytoString(){ // utilisée pour debuguée pour afficher les clés dans le terminal

        if (this.key.isEmpty()) return "";

        StringBuilder str = new StringBuilder();
        for ( Element k : this.key) {
            if (k == null) {
                return "";
            }
            switch (k) {
                case EAU:
                    str.append("Eau ");
                    break;
                case FEU:
                    str.append("Feu ");
                    break;
                case VENT:
                    str.append("Vent ");
                    break;
                case TERRE:
                    str.append("Terre ");
                    break;
            }
        }
        return str.toString().trim();

    }

    public List<Element> getArtefacts() {return artefacts;}

    public void setCpt(){cpt = 1;}
    public void addKey(Element key){this.key.add(key);}
    public void addItem(Item item){this.items.add(item);}
    public void rmItem(Item item){this.items.remove(item);}
    public void aleaItem(){ // pour récuperer de manière aléatoire une clé et ou un item
        double tmp;
        if (modele.getDifficulte() == 1) {tmp = 0.2;} // 4 chance sur 5 d'avoir une clé en mode facile
        else {tmp = 0.4;}  // 3 chance sur 5 d'avoir une clé en mode normal
        double alea = Math.random();
        if (this.key.size() < 4) {

            if (alea >= tmp) {

                List<Element> elemRestant = new ArrayList<>();

                for (Element k : Element.values()) { // on prends uniquement les clés qui ne sont pas déjà possédée par le joueur
                    if (!this.key.contains(k)) {
                        elemRestant.add(k);
                    }
                }

                if (elemRestant.isEmpty()) {
                    return;
                }

                int alea2 = (int) (Math.random() * elemRestant.size());
                Element elem = elemRestant.get(alea2);

                this.addKey(elem);
            }
        }
        if (this.items.size() < 4) {
            if (alea >= 0.6){
                if (alea <= 0.8){
                    this.addItem(Item.SABLE);
                }
                else {this.addItem(Item.HELICO);
                }
            }
        }

        modele.notifyObservers();
    }

    public boolean recupArtefact(){ // on récupere l'artefact du coffre et le coffre devient vide
        Tuile tuile = modele.getTuile(lig, col);
        if (tuile.getContientArtefact()) {
            for (Element k : this.getKey()) {
                if (k == tuile.getCoffre()) {
                    artefacts.add(tuile.getCoffre());
                    tuile.setCoffreF();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cantMove() { // on vérifie si le joueur est noyé
        boolean hautBloque = (lig - 1 < 0) || modele.getTuile(lig - 1, col) == null || modele.getTuile(lig - 1, col).getEtat() == Etat.SUBMERGEE;
        boolean basBloque = (lig + 1 >= 6) || modele.getTuile(lig + 1, col) == null || modele.getTuile(lig + 1, col).getEtat() == Etat.SUBMERGEE;
        boolean droiteBloque = (col + 1 >= 6) || modele.getTuile(lig, col + 1) == null || modele.getTuile(lig, col + 1).getEtat() == Etat.SUBMERGEE;
        boolean gaucheBloque = (col - 1 < 0) || modele.getTuile(lig, col - 1) == null || modele.getTuile(lig, col - 1).getEtat() == Etat.SUBMERGEE;
        boolean estNoye = (modele.getTuile(lig, col).getEtat() == Etat.SUBMERGEE);
        return hautBloque && basBloque && droiteBloque && gaucheBloque && estNoye;
    }

    public void setPos(int lig, int col) {
        this.lig = lig;
        this.col = col;
    }

    public void finito(){
        if (modele.getTuile(lig, col).isHeliport()){
            modele.setIsWin();
        }
    }

    public void allerHaut(){
        Tuile tuile = modele.getTuile(lig - 1, col);
        if (this.lig - 1 >= 0 && tuile != null){
            if (Objects.equals(role, "Plongeur") || tuile.getEtat() != Etat.SUBMERGEE) {
                this.lig--;
                modele.baisserAction();
                modele.notifyObservers();
            }
        }
    }

    public void allerBas(){
        Tuile tuile = modele.getTuile(lig + 1, col);
        if (this.lig + 1 >= 0 && tuile != null){
            if (Objects.equals(role, "Plongeur") || tuile.getEtat() != Etat.SUBMERGEE) {
                this.lig++;
                modele.baisserAction();
                modele.notifyObservers();
            }
        }
    }

    public void allerDroite() {
        Tuile tuile = modele.getTuile(lig, col + 1);
        if (this.col + 1 >= 0 && tuile != null){
            if (Objects.equals(role, "Plongeur") || tuile.getEtat() != Etat.SUBMERGEE) {
                this.col++;
                modele.baisserAction();
                modele.notifyObservers();
            }
        }
    }

    public void allerGauche() {
        Tuile tuile = modele.getTuile(lig, col - 1);
        if (this.col - 1 >= 0 && tuile != null){
            if (Objects.equals(role, "Plongeur") || tuile.getEtat() != Etat.SUBMERGEE) {
                this.col--;
                modele.baisserAction();
                modele.notifyObservers();
            }
        }
    }

    public void allerHautGauche() {
        if (!Objects.equals(this.getRole(), "Explorateur")) return;
        if (this.lig - 1 >= 0 && this.col - 1 >= 0 &&  modele.getTuile(lig -1 ,col -1) != null && modele.getTuile(lig -1 ,col -1).getEtat() != Etat.SUBMERGEE) {
            this.lig--;
            this.col--;
            modele.baisserAction();
            modele.notifyObservers();
        }
    }

    public void allerHautDroite() {
        if (!Objects.equals(this.getRole(), "Explorateur")) return;
        if (this.lig - 1 >= 0 && this.col + 1 >= 0 &&  modele.getTuile(lig -1 ,col +1) != null && modele.getTuile(lig -1 ,col +1).getEtat() != Etat.SUBMERGEE) {
            this.lig--;
            this.col++;
            modele.baisserAction();
            modele.notifyObservers();
        }
    }

    public void allerBasGauche() {
        if (!Objects.equals(this.getRole(), "Explorateur")) return;
        if (this.lig + 1 >= 0 && this.col - 1 >= 0 &&  modele.getTuile(lig +1 ,col -1) != null && modele.getTuile(lig +1 ,col -1).getEtat() != Etat.SUBMERGEE) {
            this.lig++;
            this.col--;
            modele.baisserAction();
            modele.notifyObservers();
        }
    }

    public void allerBasDroite() {
        if (!Objects.equals(this.getRole(), "Explorateur")) return;
        if (this.lig + 1 >= 0 && this.col + 1 >= 0 &&  modele.getTuile(lig +1 ,col +1) != null && modele.getTuile(lig +1 ,col +1).getEtat() != Etat.SUBMERGEE) {
            this.lig++;
            this.col++;
            modele.baisserAction();
            modele.notifyObservers();
        }
    }

    public Color getColor() { return this.color; }


    public void resetPouvoir() { this.powerUsed = false; }

    public void usePouvoir(){ this.powerUsed = true;}

    public boolean getPouvoir() { return powerUsed; }


}