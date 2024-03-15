package test5;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
/*
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 */

public class Client extends JFrame {

    private ObjectOutputStream out;// pour envoyer au client
    private Ecouteur sonEcouteur;
    private int numero;
    private Grille laGrille;
    private JPanel Couleur;

    private JTextArea messageArea;

    public Client(int x) throws HeadlessException {
        super();
        numero = x;

        this.setLayout(new BorderLayout());

        Couleur = new JPanel();
        if (numero % 2 == 1) {
            Couleur.setBackground(Color.RED);
            laGrille = new Grille(Color.RED);

        } else {
            Couleur.setBackground(Color.yellow);
            laGrille = new Grille(Color.yellow);
        }
        this.add(laGrille, BorderLayout.CENTER);

         // Créer et ajouter la zone de texte pour afficher les messages
         messageArea = new JTextArea();
         messageArea.setEditable(false); // Empêcher l'édition du texte
         JScrollPane scrollPane = new JScrollPane(messageArea); // Ajouter une barre de défilement
         this.add(scrollPane, BorderLayout.SOUTH);

        this.add(Couleur, BorderLayout.SOUTH);
        this.setLocation((x - 1) * 400, 400);
        this.setSize(400, 400);
        this.setVisible(true);

        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println(ip);
            Socket soc = new Socket(ip, 49512);
            out = new ObjectOutputStream(soc.getOutputStream());
            laGrille.setOut(out);
            sonEcouteur = new Ecouteur(soc, x, laGrille);
            sonEcouteur.start();
        } catch (UnknownHostException uhe) {
            System.out.println("Souci de hostName  chez le client !!");
        } catch (IOException uhe) {
            System.out.println("Souci de connexion chez le client !!");
        }

    }

    public void setMessage(String message) {
        this.messageArea.setText(message + "\n"); // Définir le nouveau message
    }
}
