import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class VueIle extends JPanel implements Observer {
    private final CModele modele;
    ControleurButton ctrlb;
    private Tuile tuileSurvolee = null;
    private Tuile ancienneTuileSurvolee = null; // permet d'eviter de faire des repaint() à chaque mouvement de souris

    private BufferedImage imageDeFond;
    private BufferedImage imageNormal;
    private BufferedImage imageFlaque;
    private BufferedImage imageHeliport;
    private BufferedImage imageOver;
    private BufferedImage imageWin;
    private BufferedImage imageCFeu;
    private BufferedImage imageCEau;
    private BufferedImage imageCTerre;
    private BufferedImage imageCVent;
    private BufferedImage imageCFeuOuvert;
    private BufferedImage imageCEauOuvert;
    private BufferedImage imageCTerreOuvert;
    private BufferedImage imageCVentOuvert;

    public VueIle(CModele modele) {
        this.modele = modele;
        modele.addObserver(this);

        try {
            imageDeFond = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/eau.jpeg")));
            imageNormal = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/herbe.png")));
            imageFlaque = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/flaque.png")));
            imageHeliport = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/heliport.png")));
            imageOver = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/over.png")));
            imageWin = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/win.png")));
            imageCFeu = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/coffreFeu.png")));
            imageCEau = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/coffreEau.png")));
            imageCTerre = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/coffreTerre.png")));
            imageCVent = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/coffreVent.png")));
            imageCFeuOuvert = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/cFeuOuvert.png")));
            imageCEauOuvert = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/cEauOuvert.png")));
            imageCTerreOuvert = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/cTerreOuvert.png")));
            imageCVentOuvert = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/cVentOuvert.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent e) {
                setTuileSurvolee(null);
            }
        });

    }

    public void update() { repaint(); }

    public void setTuileSurvolee(Tuile tuile) {
        this.tuileSurvolee = tuile;
        if (tuile != this.ancienneTuileSurvolee) {
            this.ancienneTuileSurvolee = tuile;
            repaint();
        }
    }




    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(imageDeFond, 0, 0, getWidth(), getHeight(), this);


        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Tuile tuile = modele.getTuile(i, j);
                if (tuile != null) {


                    switch (tuile.getEtat()) {
                        case NORMALE:
                            g.drawImage(imageNormal, j * modele.tailleTuile, i * modele.tailleTuile,  modele.tailleTuile, modele.tailleTuile, this);

                            if (tuile.isHeliport()) {
                                g.drawImage(imageHeliport, j * modele.tailleTuile, i * modele.tailleTuile,  modele.tailleTuile, modele.tailleTuile, this);
                            }
                            break;
                        case INONDEE:

                            g.drawImage(imageNormal, j * modele.tailleTuile, i * modele.tailleTuile,  modele.tailleTuile, modele.tailleTuile, this);

                            if (tuile.isHeliport()) {
                                g.drawImage(imageHeliport, j * modele.tailleTuile, i * modele.tailleTuile,  modele.tailleTuile, modele.tailleTuile, this);
                            }
                            g.drawImage(imageFlaque, j * modele.tailleTuile, i * modele.tailleTuile,  modele.tailleTuile, modele.tailleTuile, this);

                            break;
                        case SUBMERGEE:
                            //g.drawImage(imageDeFond, j * modele.tailleTuile, i * modele.tailleTuile,  modele.tailleTuile, modele.tailleTuile, this);
                            break;
                    }

                    if (tuile.getEtat() != Etat.SUBMERGEE){


                        Element elem = tuile.getCoffre();


                        switch (elem) {
                            case EAU:
                                if (tuile.getContientArtefact()) {
                                    g.drawImage(imageCEau, j * modele.tailleTuile, i * modele.tailleTuile, modele.tailleTuile - 40, modele.tailleTuile - 40, this);
                                } else {
                                    g.drawImage(imageCEauOuvert, j * modele.tailleTuile, i * modele.tailleTuile, modele.tailleTuile - 40, modele.tailleTuile - 40, this);
                                }
                                break;
                            case FEU:
                                if (tuile.getContientArtefact()) {
                                    g.drawImage(imageCFeu, j * modele.tailleTuile, i * modele.tailleTuile, modele.tailleTuile - 40, modele.tailleTuile - 40, this);
                                } else{
                                    g.drawImage(imageCFeuOuvert, j * modele.tailleTuile, i * modele.tailleTuile, modele.tailleTuile - 40, modele.tailleTuile - 40, this);
                                }
                                break;
                            case TERRE:
                                if (tuile.getContientArtefact()) {
                                    g.drawImage(imageCTerre, j * modele.tailleTuile, i * modele.tailleTuile,  modele.tailleTuile-40, modele.tailleTuile-40, this);
                                } else {
                                    g.drawImage(imageCTerreOuvert, j * modele.tailleTuile, i * modele.tailleTuile, modele.tailleTuile - 40, modele.tailleTuile - 40, this);
                                }
                                break;
                            case VENT:
                                if (tuile.getContientArtefact()) {
                                    g.drawImage(imageCVent, j * modele.tailleTuile, i * modele.tailleTuile,  modele.tailleTuile-40, modele.tailleTuile-40, this);
                                } else{
                                    g.drawImage(imageCVentOuvert, j * modele.tailleTuile, i * modele.tailleTuile, modele.tailleTuile - 40, modele.tailleTuile - 40, this);
                                }
                                break;
                            case null:
                                break;

                            default:
                                break;
                        }


                    }

                    g.setColor(Color.BLACK);

                    if (tuile == tuileSurvolee) {
                        g.setColor(Color.YELLOW);
                    }



                    g.drawRect(j * modele.tailleTuile +1, i * modele.tailleTuile +1,  modele.tailleTuile -2, modele.tailleTuile -2);


                    if (modele.getIsOver()) {
                        g.drawImage(imageOver, 0, 0, getWidth(), getHeight(), this);
                    }

                    if (modele.getIsWin()) {
                        g.drawImage(imageWin, 0, 0, getWidth(), getHeight(), this);
                    }

                }
            }
        }

    }



}
