public class jeu {

    public static void main(String[] args) {
        CModele modele = new CModele();

        CVue vue = new CVue(modele);
        modele.getJoueurActuel().setCpt();
    }
}
