import java.rmi.RemoteException;
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
	 * @param playerName
	 * @param playerStub
	 * @throws InterruptedException
	 */
	public static void register(String playerName, IPlayer playerStub) throws RemoteException, InterruptedException {

		ITracker trackerStub = playerStub.getTrackerStub();
		GameState gameState = null;
		LinkedHashMap<String, IPlayer> playerMap = null;
		Iterator<Map.Entry<String, IPlayer>> playerMapIterator = null;

		/**
		 * Keep on trying unless registered
		 */
		UUID requestId = UUID.randomUUID();
		do {
			// Get player Map from Tracker
			playerMap = trackerStub.getPlayerMap();

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

					if (!playerMapIterator.hasNext()) {

						// Try registering with backup Server
						Map.Entry<String, IPlayer> backupServer = playerMapIterator.next();
						String backupName = backupServer.getKey();
						IPlayer backupStub = backupServer.getValue();

						try {
							gameState = backupStub.registerPlayer(requestId, playerName, playerStub);
							playerMap = gameState.getPlayerMap();
						} catch (RemoteException e1) {
							trackerStub.removePlayer(backupName);
							Thread.sleep(800); // Sleep (wait) for the system to generate new Primary Server
						}
					}
				}
			}
		} while (!playerMap.containsKey(playerName));

		// Set game state inside current player
		if (gameState == null) {
			// Happens only when you are the primary Server/First player to be registered
			gameState = new GameState(trackerStub.getMazeDimensions(), playerMap);
			/**
			 * Place yourself in the maze somewhere, and generate treasures
			 */
		}
		playerStub.setGameState(gameState);
	}

	/**
	 * 
	 * @param playerName
	 * @param playerStub
	 */
	public static void deregister(String playerName, IPlayer playerStub) {

	}

}
