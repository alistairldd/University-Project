import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Objects;

public class VueCommandes extends JPanel implements Observer{

    private CModele modele;

    private JButton boutonFin;
    private JButton boutonAssecher;
    private JButton boutonArtefact;
    private JLabel item;
    private JButton boutonSable;
    private JButton boutonHelico;
    private JLabel joueurAction;
    private JButton boutonPouvoir;
    private JButton boutonCle;

    private JButton boutonFacile;
    private JButton boutonNormal;

    private JFrame frame;

    private final JPanel panelInfos = new JPanel();

    private final ImageIcon cleE = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/cleEau.png")));
    private final ImageIcon cleV = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/cleVent.png")));
    private final ImageIcon cleF = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/cleFeu.png")));
    private final ImageIcon cleT = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/cleTerre.png")));
    private final ImageIcon artE = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/artEau.png")));
    private final ImageIcon artV = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/artVent.png")));
    private final ImageIcon artF = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/artFeu.png")));
    private final ImageIcon artT = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/artTerre.png")));
    private final ImageIcon sac = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/sac.png")));
    private final ImageIcon helico = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/helico.png")));



    /** Constructeur */
    public VueCommandes(CModele modele, ControleurButton ctrl, JFrame frame)  {
        if (!modele.getIsOver() && !modele.getIsWin()) {

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


            this.modele = modele;
            this.boutonFin = new JButton("Fin de tour");
            this.boutonAssecher = new JButton("Assécher");
            this.boutonArtefact = new JButton("Prendre un artefact");
            this.item = new JLabel("Utiliser un objet spécial :");
            this.boutonSable = new JButton("Sac de sable");
            this.boutonHelico = new JButton("Hélicoptère");
            this.boutonPouvoir = new JButton("Pouvoir");
            this.boutonCle = new JButton("Echanger une clé");

            this.joueurAction = new JLabel("Joueur " + modele.getJoueurActuel().getId() + " : " + modele.getAction() + " actions restantes.");

            this.frame = frame;

            this.boutonFacile = new JButton("Facile");
            this.boutonNormal = new JButton("Normal");

            JButton boutonDeux = new JButton("2");
            JButton boutonTrois = new JButton("3");
            JButton boutonQuatre = new JButton("4");
            JButton boutonCinq = new JButton("5");
            JButton boutonSix = new JButton("6");

            if (!modele.getHasPartiallyStarted()) {

                JLabel nbJoueurs = new JLabel("Choisissez le nombre de joueurs");

                nbJoueurs.setAlignmentX(Component.CENTER_ALIGNMENT);
                boutonDeux.setAlignmentX(Component.CENTER_ALIGNMENT);
                boutonTrois.setAlignmentX(Component.CENTER_ALIGNMENT);
                boutonQuatre.setAlignmentX(Component.CENTER_ALIGNMENT);
                boutonCinq.setAlignmentX(Component.CENTER_ALIGNMENT);
                boutonSix.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(Box.createVerticalGlue());
                add(nbJoueurs);
                add(Box.createRigidArea(new Dimension(0, 10)));
                add(boutonDeux);
                add(Box.createRigidArea(new Dimension(0, 10)));
                add(boutonTrois);
                add(Box.createRigidArea(new Dimension(0, 10)));
                add(boutonQuatre);
                add(Box.createRigidArea(new Dimension(0, 10)));
                add(boutonCinq);
                add(Box.createRigidArea(new Dimension(0, 10)));
                add(boutonSix);
                add(Box.createVerticalGlue());

                boutonDeux.addActionListener(ctrl);
                boutonTrois.addActionListener(ctrl);
                boutonQuatre.addActionListener(ctrl);
                boutonCinq.addActionListener(ctrl);
                boutonSix.addActionListener(ctrl);
            }

            update();

        }

    }
    public void update() {
        if (modele.getIsOver() || modele.getIsWin()) {
            removeAll();
            JLabel fin;
            if (modele.getIsOver()) { fin = new JLabel("La partie est perdue.");}
            else {fin = new JLabel("Vous avez vaincu l'île maudite !!!");}

            fin.setAlignmentX(Component.CENTER_ALIGNMENT);
            fin.setForeground(Color.RED);

            JButton restart = new JButton("Recommencer");
            restart.setAlignmentX(Component.CENTER_ALIGNMENT);
            restart.addActionListener(e -> {
                System.out.println("Redémarrage du jeu");
                frame.dispose();
                jeu.main(new String[0]);
            });

            add(Box.createRigidArea(new Dimension(0, 20)));
            add(fin);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(restart);

            revalidate(); // remet à jour le layout
            repaint();    // redessine le panneau

        } else {
            if (modele.getHasStarted()) {
                joueurAction.setText(modele.getJoueurActuel().getRole() + " : " + modele.getAction() + " actions restantes.");
                panelInfos.removeAll();
                panelInfos.setBounds(1, 1, 1, 1);
                for (int i = 0; i < modele.nbJoueur; i++) {
                    Joueur joueur = modele.getJoueur()[i];

                    JPanel blocJoueur = new JPanel();
                    blocJoueur.setLayout(new BoxLayout(blocJoueur, BoxLayout.Y_AXIS));
                    blocJoueur.setAlignmentX(Component.CENTER_ALIGNMENT);
                    blocJoueur.setPreferredSize(new Dimension(200, 180));
                    //blocJoueur.setBorder(BorderFactory.createTitledBorder("Joueur " + joueur.getId()));

                    JPanel ligneC = new JPanel();
                    JPanel ligneA = new JPanel();
                    JPanel ligneI = new JPanel();

                    ligneC.setAlignmentX(Component.CENTER_ALIGNMENT);
                    ligneA.setAlignmentX(Component.CENTER_ALIGNMENT);
                    ligneI.setAlignmentX(Component.CENTER_ALIGNMENT);


                    for (Element e : joueur.getKey()) {
                        switch (e) {
                            case TERRE:
                                ligneC.add(new JLabel(new ImageIcon(cleT.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                                break;
                            case VENT:
                                ligneC.add(new JLabel(new ImageIcon(cleV.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                                break;
                            case FEU:
                                ligneC.add(new JLabel(new ImageIcon(cleF.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                                break;
                            case EAU:
                                ligneC.add(new JLabel(new ImageIcon(cleE.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                                break;
                        }
                    }

                    for (Element e : joueur.getArtefacts()) {
                        switch (e) {
                            case TERRE:
                                ligneA.add(new JLabel(new ImageIcon(artT.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                                break;
                            case VENT:
                                ligneA.add(new JLabel(new ImageIcon(artV.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                                break;
                            case FEU:
                                ligneA.add(new JLabel(new ImageIcon(artF.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                                break;
                            case EAU:
                                ligneA.add(new JLabel(new ImageIcon(artE.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                                break;
                        }
                    }


                    for (Item it : joueur.getItems()) {
                        switch (it) {
                            case SABLE ->
                                    ligneI.add(new JLabel(new ImageIcon(sac.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                            case HELICO ->
                                    ligneI.add(new JLabel(new ImageIcon(helico.getImage().getScaledInstance(30, 30, Image.SCALE_FAST))));
                        }
                    }


                    TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(joueur.getColor(), 2), joueur.getRole());
                    blocJoueur.setBorder(border);

                    blocJoueur.add(Box.createRigidArea(new Dimension(0, 5)));
                    blocJoueur.add(ligneC);

                    blocJoueur.add(Box.createRigidArea(new Dimension(0, 2)));
                    blocJoueur.add(ligneA);

                    blocJoueur.add(Box.createRigidArea(new Dimension(0, 2)));
                    blocJoueur.add(ligneI);

                    panelInfos.add(Box.createRigidArea(new Dimension(0, 5)));
                    panelInfos.add(blocJoueur);

                }
                panelInfos.revalidate();
                panelInfos.repaint();
            }
        }
        revalidate(); // remet à jour le layout
        repaint();    // redessine le panneau
    }

    public void initPreGame(ControleurButton ctrlb){
        removeAll();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel intro2 = new JLabel("Choisissez la difficulté :");
        intro2.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonFacile.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonNormal.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(intro2);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(boutonFacile);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(boutonNormal);
        add(Box.createVerticalGlue());

        boutonFacile.addActionListener(ctrlb);
        boutonNormal.addActionListener(ctrlb);


    }

    public void initGame(ControleurButton ctrlb){ // initialisation du jeu

        removeAll();


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        joueurAction.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonAssecher.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonFin.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonArtefact.setAlignmentX(Component.CENTER_ALIGNMENT);
        item.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonSable.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonHelico.setAlignmentX(Component.CENTER_ALIGNMENT);
        joueurAction.setForeground(Color.RED);
        boutonPouvoir.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonCle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(joueurAction);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(boutonAssecher);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(boutonPouvoir);

        //add(Box.createRigidArea(new Dimension(0, 5)));
        //add(boutonCle);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(boutonArtefact);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(item);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(boutonSable);
        add(boutonHelico);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(boutonFin);
        add(Box.createRigidArea(new Dimension(0, 5)));




        boutonFin.addActionListener(ctrlb);
        boutonAssecher.addActionListener(ctrlb);
        boutonArtefact.addActionListener(ctrlb);
        boutonSable.addActionListener(ctrlb);
        boutonHelico.addActionListener(ctrlb);
        boutonPouvoir.addActionListener(ctrlb);

        panelInfos.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(panelInfos);

        modele.addObserver(this);
        update();

        revalidate();
        repaint();
    }



    public void setJoueurAction(JLabel joueurAction) {this.joueurAction = joueurAction;}
}

