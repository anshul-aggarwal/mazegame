import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Game {

	public static void main(String args[]) {

		/**
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
			System.err.println("Wrong arguments. It should be: java Game [registry-ip] [registry-port] [playerId]");
			e.printStackTrace();
			return;
		}

		/**
		 * Locate registry and get tracker stub
		 */
		ITracker trackerStub;
		try {
			Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
			trackerStub = (ITracker) registry.lookup(Tracker.TRACKER_STUB_REGISTRY_KEY);
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Unable to get Tracker Stub. Exiting Game." + e);
			e.printStackTrace();
			return;
		}

		/**
		 * Create player object & stub
		 */
		IPlayer playerStub;
		IPlayer player = new Player(playerName, trackerStub);

		try {
			playerStub = (IPlayer) UnicastRemoteObject.exportObject(player, 0); // port 0 -> picks a random available
																				// port for RMI service port
		} catch (RemoteException e) {
			System.err.println("Unable to create player stub. Exiting." + e);
			e.printStackTrace();
			return;
		}

		/**
		 * Register the player
		 */
		try {
			PlayerRegistrationUtil.register(playerStub);
		} catch (RemoteException | InterruptedException e) {
			System.err.println("Unable to register new player. Exiting");
			e.printStackTrace();
			return;
		}

		/**
		 * Pinging thread
		 *
		 */
		PingUtil ping = new PingUtil(player);
		ping.start();

		/**
		 * Start GUI
		 */
		MazeGui mazeGui;
		try {
			mazeGui = new MazeGui(player);
		} catch (RemoteException e) {
			System.err.println("Unable to render UI. Exiting");
			e.printStackTrace();
			return;
		}

		/**
		 * Start Game
		 */
		LogUtil.printMsg("Started Game");
		try {
			Scanner sc = new Scanner(System.in);
			boolean terminateGame = false;

			while (!terminateGame) {

				mazeGui.updateUI();
				// printGameState(player.getGameState());

				String input;
				try {
					input = sc.next();
				} catch (NoSuchElementException e) {
					input = "9";
				}

				switch (input) {
				case "0":
					PlayerActionUtil.refresh(player);
					break;
				case "1":
				case "2":
				case "3":
				case "4":
					PlayerActionUtil.move(player, input);
					break;
				case "9":
					PlayerRegistrationUtil.deregister(playerName, playerStub);
					terminateGame = true;
					break;
				default:
					LogUtil.printMsg("Invalid input. Press 9 to terminate");
					break;
				}
			}

			ping.terminate();
			sc.close();
			LogUtil.printMsg("Game Over!");
			System.exit(0);
		} catch (RemoteException e) {
			System.err.println("Game exception: ");
			e.printStackTrace();
			return;
		}
	}

	private static void printGameState(GameState gameState) {
		int N = gameState.getMazeDimensions().getN();
		String[][] maze = gameState.getMaze();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (maze[i][j] == null)
					System.out.print("   .");
				else
					System.out.printf("%4S", maze[i][j]);
			}
			System.out.println();
		}
		Map<String, Integer> playerScore = gameState.getPlayerScore();
		for (String name : playerScore.keySet()) {
			System.out.println(name + " = " + playerScore.get(name));
		}
	}

}
