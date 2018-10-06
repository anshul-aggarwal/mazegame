import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This utility class handles Player registration and de-registration
 *
 */
public class PlayerRegistrationUtil {

	private PlayerRegistrationUtil() {
	}

	/**
	 * This method takes care of the entire registration process of a player. Be it
	 * first player or any other player.
	 * <p>
	 * This method will also update the communication/Game state maintained inside
	 * the player object!
	 *
	 * @param player
	 * @throws InterruptedException
	 */
	public static void register(Player player) throws RemoteException, InterruptedException {

		/*
		 * Create Player Stub
		 */
		IPlayer playerStub = (IPlayer) UnicastRemoteObject.exportObject(player, 0); // port 0 -> picks a random
																					// available port for RMI service
																					// port

		/*
		 * Register Player
		 */
		String playerName = player.getPlayerName();
		ITracker trackerStub = player.getTrackerStub();
		GameState gameState = null;
		LinkedHashMap<String, IPlayer> playerMap = null;
		Iterator<Map.Entry<String, IPlayer>> playerMapIterator = null;

		synchronized (DummyLock.class) {

			// LogUtil.printMsg("Trying to register myself");

			/*
			 * Keep on trying unless registered
			 */
			UUID requestId = UUID.randomUUID();

			do {

				// Get player Map from Tracker
				playerMap = trackerStub.getPlayerMap();
				gameState = null;

				if (playerMap.size() == 0) {
					playerMap = trackerStub.addFirstPlayer(playerName, playerStub);
				} else {

					// Try registering with primary player
					playerMapIterator = playerMap.entrySet().iterator();
					Map.Entry<String, IPlayer> primaryServer = playerMapIterator.next();
					String primaryName = primaryServer.getKey();
					IPlayer primaryStub = primaryServer.getValue();

					try {
						gameState = primaryStub.registerPlayer(requestId, playerName, playerStub);
						playerMap = gameState.getPlayerMap();
					} catch (RemoteException e) {

						// If primary Server is not responding
						trackerStub.removePlayer(primaryName);

						if (playerMapIterator.hasNext()) {

							// Try registering with backup Server
							Map.Entry<String, IPlayer> backupServer = playerMapIterator.next();
							String backupName = backupServer.getKey();
							IPlayer backupStub = backupServer.getValue();

							try {
								gameState = backupStub.registerPlayer(requestId, playerName, playerStub);
								playerMap = gameState.getPlayerMap();
							} catch (RemoteException e1) {
								trackerStub.removePlayer(backupName);
								DummyLock.class.wait(800); // Sleep (wait) for the system to generate new Primary Server
							}
						}
					}
				}
			} while (!playerMap.containsKey(playerName));

			/*
			 * Set game state inside current player
			 */
			if (gameState == null) {
				/*
				 * Happens only when you are the primary Server/First player to be registered
				 */
				gameState = new GameState(trackerStub.getMazeDimensions());
				gameState.setPlayerMap(playerMap);
				gameState.addPlayer(playerName);
			}
			player.setGameState(gameState);

		}
	}

	/**
	 * 
	 * @param playerName
	 * @param player
	 * @throws RemoteException
	 */
	public static void deregister(String playerName, Player player) throws RemoteException {

		// LogUtil.printMsg("Trying to deregister " + playerName);
		// LogUtil.printPlayers(player, "In PlayerRegistrationUtil#deregister");

		UUID requestId = UUID.randomUUID();

		GameState gameState = null;
		boolean completedRequest = true;
		do {
			completedRequest = true;
			try {
				gameState = player.getPrimaryServer().deregisterPlayer(requestId, playerName);
			} catch (RemoteException e1) {
				try {
					// LogUtil.printMsg("Failed to deregister with PS, trying with BS");
					gameState = player.getBackupServer().deregisterPlayer(requestId, playerName);
				} catch (RemoteException | NullPointerException e2) {
					player.updatePlayerMap();
					completedRequest = false;
				}
			}
		} while (!completedRequest);

		if (gameState != null) {
			player.setGameState(gameState);
		}

	}

}
