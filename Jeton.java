package test5;

import java.awt.*;
import java.io.Serializable;

public class Jeton implements Serializable {

    private int numLigne, numColonne;
    private Color couleur;

    public Jeton(int numLigne, int numColonne, Color couleur) {
        this.numLigne = numLigne;
        this.numColonne = numColonne;
        this.couleur = couleur;
    }

    @Override
    public String toString() {
        return "Jeton{" +
                "numLigne=" + numLigne +
                ", numColonne=" + numColonne +
                ", couleur=" + couleur +
                '}';
    }

    public int getNumLigne() {
        return numLigne;
    }

    public int getNumColonne() {
        return numColonne;
    }

    public Color getCouleur() {
        return couleur;
    }
}
