import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Game {

    public static void main(String args[]) {

        /**
         * Read command line arguments
         */
        String registryHost;
        Integer port;
        String playerName;
        try {
            port = Integer.parseInt(args[0]);
            playerName = args[1];
            registryHost = args.length > 2 ? args[2] : "localhost";
        } catch (Exception e) {
            System.err.println("Wrong arguments. It should be: java Game [port-number] [player name] [tracker host, if not localhost]");
            return;
        }

        /**
         * Locate registry and get tracker stub
         */
        ITracker trackerStub;
        try {
            Registry registry = LocateRegistry.getRegistry(registryHost, Tracker.RMI_REGISTRY_PORT);
            trackerStub = (ITracker) registry.lookup(Tracker.TRACKER_STUB_REGISTRY_KEY);
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Unable to get Tracker Stub. Exiting Game." + e);
            return;
        }

        /**
         * Create player object & stub
         */
        IPlayer playerStub;
        try {
            IPlayer player = new Player(playerName, trackerStub);
            playerStub = (IPlayer) UnicastRemoteObject.exportObject(player, port);
        } catch (RemoteException e) {
            System.err.println("Unable to create player stub. Exiting." + e);
            return;
        }

        /**
         * Register the player
         */
        try {
            RegistrationUtil.register(playerStub);
        } catch (RemoteException e) {
            System.err.println("Unable to register new player. Exiting");
            return;
        }

        /**
         * Play the game
         */
        try {
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.print("Enter c to contact everyone or anything else to quit: ");
                String input = sc.next();
                if (input.equals("c")) {
                    System.out.println("SOME DUMMY CODE");
                } else {
                    break;
                }
            }
            sc.close();
            System.out.println("Game Over!");
        } catch (Exception e) {
            System.err.println("Game exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
