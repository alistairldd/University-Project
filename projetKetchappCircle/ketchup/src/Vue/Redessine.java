package Vue;

public class Redessine extends Thread {
    private Affichage monAffichage;
    public final static int DELAY = 50;

    /*constructeur*/
    public Redessine(Affichage a){
        monAffichage = a;
        this.start();
    }

    /*redéfinition de run*/
    @Override
    public void run() {
        while(true){
            monAffichage.repaint();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
