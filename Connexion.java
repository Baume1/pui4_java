package test5;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connexion extends Thread {

    private final Socket socketDuClient;
    private ObjectOutputStream out;// pour envoyer au client
    private ObjectInputStream in; // pour recevoir du client
    private int numeroDuClient;
    private Serveur leServeur;

    private Client client;

    public Connexion(Socket soc, int num, Serveur s, Client client) {
        this.socketDuClient = soc;
        this.numeroDuClient = num;
        leServeur = s;
        this.client = client;

        try {
            System.out.println("Tentative d'initialisation du flux d'entrée pour Connexion " + num);
            in = new ObjectInputStream(socketDuClient.getInputStream());
            System.out.println("Flux d'entrée initialisé avec succès pour Connexion " + num);
        } catch (Exception ex) {
            System.out.println("Erreur lors de l'initialisation du flux d'entrée pour Connexion " + num);
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("CONNEXION " + numeroDuClient + " lancée");
        try {
            in = new ObjectInputStream(this.socketDuClient.getInputStream());
        } catch (Exception ex) {
            System.out.println("Dans Connexion, souci pour mettre la main sur le canal de lecture du socket" + this.numeroDuClient);
        }

        Object o = null;
        do {
            System.out.println("\nConnexion " + this.numeroDuClient + "+ en attente de reception d'un objet ");
            try {
                o = in.readObject();
                System.out.println("Connexion " + this.numeroDuClient + " : reçu un truc ");

                if (o instanceof Jeton) { 
                    Jeton j = ((Jeton) o);
                    System.out.println("jeton reçu ::" + o.toString());

                    for (Connexion c : leServeur.getLaListeDesConnexions()) {
                        out = new ObjectOutputStream(c.socketDuClient.getOutputStream());
                        out.writeObject(j);
                        // out.close();
                    }
                    System.out.println("envoi à tous terminé");
                }else if (o instanceof Message) {
                    // Si l'objet reçu est un Message, vous pouvez effectuer le traitement correspondant
                    Message message = (Message) o;
                    // Traitement pour les objets de type Message
                    client.setMessage("Message reçu par Connexion " + numeroDuClient + ": " + message.getMessage());
                } 

            } catch (Exception e) {
                System.out.println("Error dans run connexion  " + this.numeroDuClient + ":" + e.toString());

            }

        } while (o != null);

    }

}
