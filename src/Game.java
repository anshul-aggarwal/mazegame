import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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
		IPlayer player = new Player(playerName, trackerStub);
		IPlayer pingPlayer = null;

		try {
			//IPlayer player = new Player(playerName, trackerStub);
			playerStub = (IPlayer) UnicastRemoteObject.exportObject(player, port);
		} catch (RemoteException e) {
			System.err.println("Unable to create player stub. Exiting." + e);
			return;
		}

		/**
		 * Register the player
		 */
		try {
			RegistrationUtil.register(playerName, playerStub);
		} catch (RemoteException e) {
			System.err.println("Unable to register new player. Exiting");
			return;
		}
		
		/**
		 * Set the ping target. This ping target needs to be reset every time the player who you are pinging crashes
		 * or when a new player is added in case you are the primary server.
		 * 
		 * The ping target initially is the player who joined before you.
		 * The player who joins last will be pinged by the player who is first in the list, i.e. the primary server.
		 */
		try {
			player.setPingTarget();
		} catch (RemoteException e1) {
			System.out.println("Failure setting initial ping target");
			e1.printStackTrace();
		}

		/**
		 * Pinging thread
		 *
		 */		
		Thread ping = new PingUtil(player);
		ping.start();

		/**
         * Start Game
		 */
		try {
			Scanner sc = new Scanner(System.in);
			while (true) {
				//Movement of player
				//System.out.print("Enter c to contact everyone or anything else to quit: ");
				String input = sc.next();
				boolean terminateGame = false;
				switch(input)
				{
				case "0": player.refreshGameState(); break;
				case "1":
				case "2":
				case "3":
				case "4": player.move(input); break;
				case "9": player.exitGame(); break;
				default: System.out.println("Invalid"); terminateGame = true; break;
				}
//				if (input.equals("1")) {
//					System.out.println("SOME DUMMY CODE");
//				} else {
//					break;
//				}
				
				if (terminateGame)
				{
					ping.interrupt();
					break;
				}
			}
			sc.close();
			System.out.println("Game Over!");
			System.exit(0);
		} catch (Exception e) {
			System.err.println("Game exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
