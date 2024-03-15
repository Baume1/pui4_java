package test5;

/* 
import java.net.InetAddress;
import java.net.Socket;
*/

public class Main {

    public static void main(String[] args) {
        Serveur leServeur = null;
        try {
            leServeur = new Serveur();
            leServeur.start();// appel de run()
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            Client c1 = new Client(1);
            Client c2 = new Client(2);

            // Après avoir instancié les clients, les ajouter au serveur
            leServeur.addClient(c1);
            leServeur.addClient(c2);


            // Client c3 = new Client(3);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
