import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;

public class JoueurModeleTest {

    CModele modele = new CModele();

    @Test
    public void testDeplacementNormal() {
        Joueur joueur = new Joueur(modele, 2, 2, "Explorateur");

        Assert.assertEquals(2, joueur.getLig());
        Assert.assertEquals(2, joueur.getCol());
        joueur.allerHaut();
        Assert.assertEquals(1, joueur.getLig());
        Assert.assertEquals(2, joueur.getCol());
        joueur.allerBas();
        Assert.assertEquals(2, joueur.getLig());
        Assert.assertEquals(2, joueur.getCol());
        joueur.allerGauche();
        Assert.assertEquals(2, joueur.getLig());
        Assert.assertEquals(1, joueur.getCol());
        joueur.allerDroite();
        Assert.assertEquals(2, joueur.getLig());
        Assert.assertEquals(2, joueur.getCol());


    }

    @Test
    public void testAssechement() {
        Tuile tuile = new Tuile("Tuile1");
        Assert.assertEquals(Etat.NORMALE, tuile.getEtat());
        tuile.inonder();
        Assert.assertEquals(Etat.INONDEE, tuile.getEtat());
        tuile.assecher();
        Assert.assertEquals(Etat.NORMALE, tuile.getEtat());
        tuile.inonder();
        tuile.inonder();
        Assert.assertEquals(Etat.SUBMERGEE, tuile.getEtat());
    }

    @Test
    public void testArtefact() {
        Joueur joueur = new Joueur(modele, 2, 2, "Chomeur");

        joueur.addKey(Element.EAU);
        joueur.addKey(Element.VENT);
        ArrayList<Element> truc = new ArrayList<>();
        truc.add(Element.EAU);
        truc.add(Element.VENT);
        Assert.assertEquals(truc, joueur.getKey());
    }



    @Test
    public void testInitialisationGrille() {
        int cpt = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (modele.getTuile(i, j) != null) {
                    cpt++;
                }
            }
        }
        Assert.assertEquals(24, cpt);
    }

    @Test
    public void testNbJoueur() {
        Assert.assertEquals(6, modele.getNbJoueur());
        Assert.assertEquals(6, modele.getJoueur().length);
    }

    @Test
    public void testActionResetEtBaisser() {
        Assert.assertEquals(3, modele.getAction());
        modele.baisserAction();
        Assert.assertEquals(2, modele.getAction());
        modele.resetAction();
        Assert.assertEquals(3, modele.getAction());
    }

    @Test
    public void testChangerJoueurActuel() {
        Joueur joueurDebut = modele.getJoueurActuel();
        modele.changerJoueurActuel();
        Joueur joueurSuivant = modele.getJoueurActuel();
        Assert.assertNotEquals(joueurDebut, joueurSuivant);
    }

    @Test
    public void testTenterSable() {
        Joueur joueur = modele.getJoueurActuel();
        Tuile tuile = modele.getTuile(joueur.getLig(), joueur.getCol());

        // Simuler que le joueur a un sable
        joueur.addItem(Item.SABLE);

        tuile.inonder(); // Inonder la case
        Assert.assertEquals(Etat.INONDEE, tuile.getEtat());

        boolean resultat = modele.tenterSable(joueur.getLig(), joueur.getCol());
        Assert.assertTrue(resultat);
        Assert.assertEquals(Etat.NORMALE, tuile.getEtat());
    }

    @Test
    public void testTenterHelico() {
        Joueur joueur = modele.getJoueurActuel();
        Tuile tuile = modele.getTuile(joueur.getLig(), joueur.getCol());

        // Simuler que le joueur a un hélico
        joueur.addItem(Item.HELICO);

        modele.setHelico(true);
        boolean resultat = modele.tenterHelico(joueur.getLig(), joueur.getCol());
        Assert.assertTrue(resultat);
    }

    @Test
    public void testVictoireImpossibleAuDebut() {
        Assert.assertFalse(modele.getIsWin());
    }

    @Test
    public void testDefaiteImpossibleAuDebut() {
        Assert.assertFalse(modele.getIsOver());
    }
}


