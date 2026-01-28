import javax.swing.*;
import java.awt.*;



public class CVue implements Observer {

    private final JFrame frame;
    private final JLabel narrateur;
    private String texte;
    private final VueIle vueIle;
    private VueCommandes vueCommandes;
    private final JLayeredPane layeredPane;



    public CVue(CModele modele) {

            frame = new JFrame(" Île interdite ");
            frame.setSize(new Dimension(1250,800));


            frame.setLayout(new GridLayout(1,2));


            this.narrateur = new JLabel("Bienvenue dans l'île interdite !");
            modele.addObserver(this);

            VueIle ile = new VueIle(modele);
            ile.setBounds(0, 0, 600, 600);
            this.vueIle = ile;

            VueJoueur joueur = new VueJoueur(modele);
            joueur.setBounds(0, 0, 600, 600);

            JLayeredPane layeredPane = new JLayeredPane();

            layeredPane.add(vueIle, Integer.valueOf(0));
            layeredPane.add(joueur, Integer.valueOf(1));

            this.layeredPane = layeredPane;


            ControleurClic ctrlClic = new ControleurClic(modele, this);
            ile.addMouseListener(ctrlClic);


            ControleurButton ctrlb  = new ControleurButton(modele, this, vueCommandes);
            this.vueCommandes = new VueCommandes(modele, ctrlb, frame);

            //vueCommandes.setBounds(0, 0, 200, 200);
            //commandes.setBackground(Color.RED);

            vueCommandes.add(narrateur);
            narrateur.setAlignmentX(Component.CENTER_ALIGNMENT);


            JPanel panelinfo = new JPanel ();
            //panelinfo.setPreferredSize(new Dimension(50, 50));
            vueCommandes.add(panelinfo);
            frame.add(vueCommandes);



            ControleurMvt ctrlm = new ControleurMvt(modele);
            frame.addKeyListener(ctrlm);
            frame.setFocusable(true);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);


    }

    public JFrame getFrame() { return frame; }

    public void setTexte(String txt){ this.texte = txt;}

    public void addLayeredPane() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(layeredPane);
        narrateur.setFont(new Font("Arial", Font.BOLD, 20));
        narrateur.setAlignmentX(Component.CENTER_ALIGNMENT);
        narrateur.setPreferredSize(new Dimension(600, 150));
        panel.add(narrateur);
        frame.add(panel);
    }


    public void update() {
        narrateur.setText(texte);
        narrateur.setHorizontalAlignment(JLabel.CENTER);
    }



    public VueIle getVueIle() {return this.vueIle;}

    public VueCommandes getVueCommande(){ return this.vueCommandes; }
}
