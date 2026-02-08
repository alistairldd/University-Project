package Model;

import Model.Position;

public class Jump extends Thread {
    // nos attributs : la position
    private final Position maPosition;

    /*constructeur par défaut*/
    public Jump(Position p){
        maPosition = p;
        this.start();
    }

    /*redéfinition de run*/
    @Override
    public void run() {
        /*on vérifie si le jeu est en cours*/
        if (!Position.getJeuFini()) {
            // si le jeu n'est pas fini : on fait une animation de saut en augmentant progressivement la hauteur de l'ovale
            for (int i = 0; i < maPosition.getHauteurSaut(); i++) {
                maPosition.sauter();
                /*on attend un petit delai pour faire comme une animation de saut*/
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        /*si le jeu n'est plus en cours on interrompt le thread*/
        else Thread.currentThread().interrupt();
    }
}

