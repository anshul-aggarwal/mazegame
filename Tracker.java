import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Tracker implements ITracker {

    private ArrayList<IPlayer> players;

    public Tracker() {
        this.players = new ArrayList<>();
    }

    @Override
    public ArrayList<IPlayer> add(IPlayer player) {
        this.players.add(player);
        return this.players;
    }

    public static void main(String args[]) {

        try {

            /*
            Locate Registry and remove any Tracker bindings
             */
            Registry registry = LocateRegistry.getRegistry();
            try {
                registry.unbind("Tracker");
            } catch (java.rmi.NotBoundException nbe) {
            }

            /*
            Create a tracker object & a Stub Tracker out of it.
            Bind it to Registry
             */
            ITracker tracker = new Tracker();
            Integer port = Integer.parseInt(args[0]);
            ITracker stub = (ITracker) UnicastRemoteObject.exportObject(tracker, port);
            registry.bind("Tracker", stub);

            System.err.println("Tracker ready");
        } catch (Exception e) {
            System.err.println("Tracker exception: " + e.toString());
            e.printStackTrace();
        }


    }

}
