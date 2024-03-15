package test5;

//import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Ecouteur extends Thread {
    private Socket socClient;
    private ObjectInputStream in; // pour recevoir du client
    private int num;
    private Grille laGrille;

    public Ecouteur(Socket s, int n, Grille g) {
        super();
        socClient = s;
        num = n;
        laGrille = g;
    }

    public void run() {
        Message m;
        Object o;
        boolean continuer = true;
        int joueur = 1;
        Color[] couleurJoueur = {Color.white, Color.RED, Color.YELLOW}; 
        /*
         * try {
         * 
         * } catch (IOException e) {
         * System.out.
         * println("dans Ecouteur : Souci pour recup le canal de lecture du socket client  !!"
         * );
         * }
         * 
         * System.out.println("okkkkkkkkkkkkkkkkkkkkkkkk");
         */
        // DefaultListModel dlm = new DefaultListModel();

        // Ajouter l'initialisation des joueurs/envoi de qui commence

        do {
            try {
                in = new ObjectInputStream(socClient.getInputStream());
                o = in.readObject();
                Jeton j = ((Jeton) o);
                System.out.println("reception par ecouteur :" + j.toString());

                // Faire les tests server-side est mieux, au cas où un petit rigolo côté client s'amuse à trafiquer la mémoire
                if(j.getCouleur().equals(couleurJoueur[joueur]) && j.getNumColonne() <= 7){

                    final int LIGNE_CORRECTE = findRow(this.laGrille.getMatrice(), j.getNumColonne() - 1, 5); 
                    if (LIGNE_CORRECTE > -1) {
                        this.laGrille.getMatrice()[LIGNE_CORRECTE][j.getNumColonne() - 1] = joueur;
                        this.laGrille.repaint();
    
                        if (gagner(this.laGrille.getMatrice(), joueur)) {
                            System.out.println("Le joueur " + joueur + " a gagné la partie");
                            // Envoyer le message de victoire
                            continuer = false;
                        } else if (egalite(this.laGrille.getMatrice())) {
                            System.out.println("Egalité, fin de la partie");
                            // Envoyer le message d'égalité
                            continuer = false;
                        } else {
                            // Changer le joueur qui peut jouer
                            joueur = joueur % 2 + 1;
                        }
                    } else {
                        System.out.println("Colonne pleine.");
                    }
                }
                else{
                    System.out.println("Mauvais joueur ou mauvaise colonne.");
                }
            } catch (IOException e) {
                System.out.println("souci 1 dans run de Ecouteur " + num);
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("souci 2 dans run de Ecouteur " + num);
            }
        } while (continuer);
    }

    private int findRow(int tableau[][], int colonne, int ligne) {
        // On teste récursivement à partir du "bas" du tableau en remontant pour trouver
        // la première case (ligne dans la colonne) de libre.
        // Si il n'y en a aucune (on dépasse le tableau), cela signifie que la colonne
        // est pleine
        if (ligne < 0) {
            return ligne;
        } else {
            if (tableau[ligne][colonne] == 0)
                return ligne;
            else {
                return findRow(tableau, colonne, ligne - 1);
            }
        }
    }

    private boolean diagonale(int tableau[][], int joueur) {
        // Commence en bas à gauche jusqu'en haut à droite
        for (int ligne = 5; ligne > 2; --ligne) {
            for (int colonne = 0; colonne < 4; ++colonne) {
                if (diagonale_vers_haut(tableau, ligne, colonne, joueur)) {
                    return true;
                }
            }
        }
        // Commence en haut à gauche jusqu'en bas à droite
        for (int ligne = 0; ligne < 3; ++ligne) {
            for (int colonne = 0; colonne < 4; ++colonne) {
                if (diagonale_vers_bas(tableau, ligne, colonne, joueur)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean diagonale_vers_haut(int tableau[][], int ligne, int colonne, int joueur) {
        for (int i = 0; i < 4; ++i) {
            if (tableau[ligne - i][colonne + i] != joueur) {
                return false;
            }
        }
        return true;
    }

    private boolean diagonale_vers_bas(int tableau[][], int ligne, int colonne, int joueur) {
        for (int i = 0; i < 4; ++i) {
            if (tableau[ligne + i][colonne + i] != joueur) {
                return false;
            }
        }
        return true;
    }

    private boolean egalite(int tableau[][]) {
        for (int i = 0; i < 7; i++) {
            if (tableau[0][i] == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean verticale_bis(int tableau[][], int ligne, int colonne, int joueur) {
        for (int i = 0; i < 4; i++) {
            if (tableau[ligne - i][colonne] != joueur) {
                return false;
            }
        }
        return true;
    }

    private boolean verticale(int tableau[][], int joueur) {
        for (int ligne = 5; ligne > 2; ligne--) {
            for (int colonne = 0; colonne < 7; ++colonne) {
                if (verticale_bis(tableau, ligne, colonne, joueur)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean horizontale_bis(int tableau[][], int ligne, int colonne, int joueur) {
        for (int i = 0; i < 4; i++) {
            if (tableau[ligne][colonne + i] != joueur) {
                return false;
            }
        }
        return true;
    }

    private boolean horizontale(int tableau[][], int joueur) {
        for (int ligne = 0; ligne < 6; ligne++) {
            for (int colonne = 0; colonne < 4; colonne++) {
                if (horizontale_bis(tableau, ligne, colonne, joueur)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean gagner(int tableau[][], int joueur) {
        return (diagonale(tableau, joueur) || verticale(tableau, joueur) || horizontale(tableau, joueur));
    }
}