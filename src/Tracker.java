import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;

public class Tracker implements ITracker {

	private final MazeDimensions mazeDimensions;
	private LinkedHashMap<String, IPlayer> playerMap;

	public static final int RMI_REGISTRY_PORT = 1099;
	public static final String TRACKER_STUB_REGISTRY_KEY = "Tracker";

	/**
	 * Constructor
	 *
	 * @param N
	 * @param K
	 */
	public Tracker(int N, int K) {
		this.mazeDimensions = new MazeDimensions(N, K);
		this.playerMap = new LinkedHashMap<>(); // Using linkedHashMap to maintain insertion order
	}

	@Override
	public LinkedHashMap<String, IPlayer> getPlayerMap() throws RemoteException {
		return this.playerMap;
	}

	@Override
	public MazeDimensions getMazeDimensions() throws RemoteException {
		return this.mazeDimensions;
	}

	@Override
	public synchronized LinkedHashMap<String, IPlayer> addFirstPlayer(String name, IPlayer player)
			throws RemoteException {
		if (this.playerMap.size() == 0) {
			this.playerMap.put(name, player);
		}
		return this.playerMap;
	}

	@Override
	public LinkedHashMap<String, IPlayer> addPlayer(String name, IPlayer player) throws RemoteException {
		this.playerMap.put(name, player);
		return this.playerMap;
	}

	@Override
	public LinkedHashMap<String, IPlayer> removePlayer(String name) throws RemoteException {
		this.playerMap.remove(name);
		return this.playerMap;
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
		} catch (Exception e) {
			System.err.println("Wrong arguments. It should be: java Tracker [port-number] [N] [K]");
			return;
		}

		/**
		 * Start the rmi registry, if it's not already running. Get its reference in
		 * registry variable
		 */
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(RMI_REGISTRY_PORT);
			System.out.println("Started RMI Registry at port:" + RMI_REGISTRY_PORT);
		} catch (RemoteException e) {
			try {
				registry = LocateRegistry.getRegistry(RMI_REGISTRY_PORT);
				System.out.println("RMI Registry already running at port:" + RMI_REGISTRY_PORT);
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
			ITracker tracker = new Tracker(N, K);
			stub = (ITracker) UnicastRemoteObject.exportObject(tracker, port);
		} catch (RemoteException e) {
			System.err.println("Error creating Tracker Stub: " + e);
			return;
		}

		/**
		 * Bind the stub with registry
		 */
		try {
			registry.unbind(TRACKER_STUB_REGISTRY_KEY);
		} catch (NotBoundException | RemoteException e) {
		}
		try {
			registry.bind(TRACKER_STUB_REGISTRY_KEY, stub);
		} catch (RemoteException | AlreadyBoundException e) {
			System.err.println("Unable to bind Tracker stub to registry");
			return;
		}

		System.out.println("Tracker Ready!");
	}
}
