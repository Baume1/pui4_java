package test5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
//import java.util.concurrent.CountDownLatch;

public class Grille extends JPanel {
    private int nbLignes = 6;

    private int nbColonnes = 7;
    private int DISTANCE = 45;
    private int matrice[][];
    private Color laBonneCouleur;
    private ObjectOutputStream out;// pour envoyer au serveur

    public Grille(Color c) {
        laBonneCouleur = c;
        matrice = new int[nbLignes][nbColonnes];
        for (int j = 0; j < nbColonnes; j++)
            for (int i = 0; i < nbLignes; i++)
                matrice[i][j] = 0; // 0 symbolise la couleur BLANCHE, 1 pour ROUGE et 2 pour JAUNE

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("X=" + e.getX() + " Y=" + e.getY());
                int ligne = (1 + e.getY() / DISTANCE);
                int colonne = (1 + e.getX() / DISTANCE);
                System.out.println("Vous avez cliqué le jeton de ligne " + ligne + " et de colonne " + colonne);

                Jeton j = new Jeton(ligne, colonne, laBonneCouleur);
                try {
                    out.writeObject(j);
                    System.out.println("Jeton bien  envoyé au serveur");
                } catch (IOException ex) {
                    System.out.println("Souci pour envoyer le message au serveur ");
                }

            }
        });
    }

    public void paint(Graphics g) {
        Color[] couleurJoueur = {Color.white, Color.RED, Color.YELLOW}; 
        for (int j = 1; j <= nbColonnes; j++)
            for (int i = 1; i <= nbLignes; i++) {
                g.setColor(couleurJoueur[matrice[i - 1][j - 1]]);

                g.fillOval((j - 1) * DISTANCE, (i - 1) * DISTANCE, DISTANCE, DISTANCE);
            }
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public int[][] getMatrice() {
        return matrice;
    }
}
