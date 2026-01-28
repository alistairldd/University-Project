public class Tuile {

    private final String nom;
    private Etat etat;
    private boolean isHeliport;
    private Element coffre;
    private boolean contientArtefact;

    /** Constructeur */
    public Tuile(String nom) {
        this.nom = nom;
        this.etat = Etat.NORMALE;
        this.isHeliport = false;
        this.coffre = null;
        this.contientArtefact = false;
    }

    /** Getters */
    public String getNom() { return nom; }
    public Etat getEtat() { return etat; }
    public Element getCoffre() { return coffre; }
    public boolean getContientArtefact() { return contientArtefact; }

    public void inonder() {
        if (etat == Etat.NORMALE) etat = Etat.INONDEE;
        else if (etat == Etat.INONDEE) etat = Etat.SUBMERGEE;
    }


    public void assecher(){
        if (etat == Etat.INONDEE) etat = Etat.NORMALE;
    }

    public boolean isHeliport() { return isHeliport; }

    public String toString() {
        return nom + " (" + etat + ")";
    }

    /** setters */


    public void setHeliportTrue(){
        isHeliport = true;
    }

    public void setCoffreT(Element e){
        this.coffre = e;
        this.contientArtefact = true;
    }
    public void setCoffreF(){
        this.contientArtefact = false;
    }
}
