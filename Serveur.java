package test5;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.ArrayList;
import java.util.LinkedList;

public class Serveur extends Thread {

    private JFrame fenetreServeur;
    private JList listeDesIP;
    private DefaultListModel dlm;
    private final ServerSocket socketDuServeurQuiEcoute;
    private static int numeroClient;
    private LinkedList<Connexion> laListeDesConnexions;

    public Serveur() throws IOException {
        super();
        fenetreServeur = new JFrame();
        fenetreServeur.setSize(300, 300);
        fenetreServeur.setLocation(0, 0);
        fenetreServeur.setLayout(new BorderLayout());
        listeDesIP = new JList();
        fenetreServeur.add(listeDesIP, BorderLayout.CENTER);
        fenetreServeur.setVisible(true);
        dlm = new DefaultListModel();
        laListeDesConnexions = new LinkedList<>();

        this.socketDuServeurQuiEcoute = new ServerSocket(49512); // numero de port un peu au hasard

    }

    public void run() {
        Connexion connex;
        do {

            try {
                System.out.println("Serveur en attente ...");
                Socket socClient = socketDuServeurQuiEcoute.accept();

                dlm.addElement("-->l'ip " + socClient.getInetAddress() + " s'est connecte");
                listeDesIP.setModel(dlm);

                connex = new Connexion(socClient, ++numeroClient, this);

                laListeDesConnexions.add(connex);
                System.out.println("nombre de clients connectés " + laListeDesConnexions.size());
                connex.start();
            } catch (IOException e) {
                System.out.println("IOException coté serveur : ");
                e.printStackTrace();
            } catch (Exception ex) {
                System.out.println("Exception inconnue coté serveur : ");
                ex.printStackTrace();
            }
        } while (true);

    }

    public LinkedList<Connexion> getLaListeDesConnexions() {
        return laListeDesConnexions;
    }
}