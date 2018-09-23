import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Tracker implements ITracker {

    private final MazeDimensions mazeDimensions;
    private Set<IPlayer> players;

    private static final int RMI_REGISTRY_PORT = 1099;

    public static final String TRACKER_STUB_REGISTRY_KEY = "Tracker";

    /**
     * Constructor
     *
     * @param N
     * @param K
     */
    public Tracker(int N, int K) {
        this.mazeDimensions = new MazeDimensions(N, K);
        this.players = new LinkedHashSet<>(); // Using linkedHashSet to maintain insertion order
    }

    @Override
    public Set<IPlayer> getPlayers() throws RemoteException {
        return this.players;
    }

    @Override
    public MazeDimensions getMazeDimensions() throws RemoteException {
        return this.mazeDimensions;
    }

    @Override
    public synchronized boolean addFirstPlayer(IPlayer player) throws RemoteException {
        if (this.players.size() == 0) {
            this.players.add(player);
            return true;
        }
        return false;
    }

    @Override
    public Set<IPlayer> addPlayer(IPlayer player) throws RemoteException {
        this.players.add(player);
        return this.players;
    }

    @Override
    public Set<IPlayer> removePlayer(IPlayer player) throws RemoteException {
        this.players.remove(player);
        return this.players;
    }

    public static void main(String args[]) {

        /**
         * Read command line arguments
         */
        int port, N, K;
        try {
            port = Integer.parseInt(args[0]);
            N = Integer.parseInt(args[1]);
            K = Integer.parseInt(args[2]);
        } catch(Exception e) {
            System.err.println("Wrong arguments. It should be: java Tracker [port-number] [N] [K]");
            return;
        }

        /**
         * Start the rmi registry, if it's not already running.
         * Get its reference in registry variable
         */
        Registry registry = null;
        try{
            registry = LocateRegistry.createRegistry(RMI_REGISTRY_PORT);
            System.out.println("Started RMI Registry at port:" + RMI_REGISTRY_PORT);
        } catch (RemoteException e) {
            try {
                registry = LocateRegistry.getRegistry(RMI_REGISTRY_PORT);
                System.out.println("RMI Registry already running at port:"+RMI_REGISTRY_PORT);
            } catch (RemoteException e1) {
                System.err.println("Some unknown error occurred while locating registry. Exiting Program");
                System.err.println(e1);
                return;
            }
        }

        /**
         * Create Tracker object and stub.
         */
        ITracker stub;
        try {
            ITracker tracker = new Tracker(N,K);
            stub = (ITracker) UnicastRemoteObject.exportObject(tracker,port);
        } catch (RemoteException e) {
            System.err.println("Error creating Tracker Stub: "+e);
            return;
        }

        /**
         * Bind the stub with registry
         */
        try{
            registry.unbind(TRACKER_STUB_REGISTRY_KEY);
        } catch (NotBoundException | RemoteException e) {
        }
        try {
            registry.bind(TRACKER_STUB_REGISTRY_KEY, stub);
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Unable to bind Tracker stub to registry");
        }

        System.out.println("Tracker Ready!");
    }
}
