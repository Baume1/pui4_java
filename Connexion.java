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

    public Connexion(Socket soc, int num, Serveur s) {
        this.socketDuClient = soc;
        this.numeroDuClient = num;
        leServeur = s;
    }

    @Override
    public void run() {
        System.out.println("CONNEXION " + numeroDuClient + " lancée");
        try {
            in = new ObjectInputStream(this.socketDuClient.getInputStream());
        } catch (Exception ex) {
            System.out.println("Dans Connexion, souci pour mettre la main sur le canal de lecture du socket"
                    + this.numeroDuClient);

        }

        Object o = null;
        do {
            System.out.println("\nConnexion " + this.numeroDuClient + "+ en attente de reception d'un objet ");
            try {
                o = in.readObject();
                System.out.println("Connexion " + this.numeroDuClient + " : reçu un truc ");
                Jeton j = ((Jeton) o);
                System.out.println("jeton reçu ::" + o.toString());

                for (Connexion c : leServeur.getLaListeDesConnexions()) {
                    out = new ObjectOutputStream(c.socketDuClient.getOutputStream());
                    out.writeObject(j);
                    // out.close();
                }
                System.out.println("envoi à tous terminé");

            } catch (Exception e) {
                System.out.println("Error dans run connexion  " + this.numeroDuClient + ":" + e.toString());
            }

        } while (o != null);

    }

}