import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Game {

	public static void main(String args[]) {

		/*
		 * Read command line arguments
		 */
		String registryHost;
		Integer registryPort;
		String playerName;
		try {
			registryHost = args[0];
			registryPort = Integer.parseInt(args[1]);
			playerName = args[2];
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			System.err.println("Wrong arguments. It should be: java Game [registry-ip] [registry-port] [playerId] ");
			return;
		}

		/*
		 * Locate registry and get tracker stub
		 */
		ITracker trackerStub;
		try {
			Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
			trackerStub = (ITracker) registry.lookup(Tracker.TRACKER_STUB_REGISTRY_KEY);
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Unable to get Tracker Stub. Exiting Game. ");
			return;
		}

		/*
		 * Create player object
		 */
		Player player = new Player(playerName, trackerStub);

		/*
		 * Register the player
		 */
		try {
			PlayerRegistrationUtil.register(player);
		} catch (RemoteException | InterruptedException e) {
			System.err.println("Unable to register new player. Exiting ");
			return;
		}
		LogUtil.printMsg("I am Registered");

		/*
		 * Start Game Thread
		 */
		GameThread gameThread = new GameThread(player);
		gameThread.start();
	}

}
