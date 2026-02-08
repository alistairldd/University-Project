package Model;

import Model.Parcours;
import Model.Position;

import static Model.Position.toucheLigne;

public class Defilement extends Thread {

    // nos attributs : le delai de defilement, la position et le parcours
    private final static int DELAY = 100;
    private final Position monAvancement;
    private final Parcours monParcours;

    // constructeur par défaut
    public Defilement(Position p, Parcours pa){
        monAvancement = p;
        monParcours = pa;
        this.start();
    }

    /* on rajoute un thread pour faire défiler l'avancement*/
    @Override
    public void run() {
        while (true) {
            toucheLigne(monParcours.getPoints());
            /*on vérifie si on a plus de vie*/
            if (Position.getJeuFini()){
                Thread.currentThread().interrupt();
            }
            else{
                /*on augmente l'avancement*/
                if (Score.getScore() > 10) {
                    // Si le score est supérieur à 10, on accélère le défilement
                    monAvancement.setAvancement(Position.getAvancement() + 2);
                } else {
                    // vitesse de base
                    monAvancement.setAvancement(Position.getAvancement() + 1);
                }

            }

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}