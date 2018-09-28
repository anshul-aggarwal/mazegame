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
	 * @param localPlayerStub
	 * @throws InterruptedException
	 */
	public static void register(IPlayer localPlayerStub) throws RemoteException, InterruptedException {

		String playerName = localPlayerStub.getPlayerName();
		ITracker trackerStub = localPlayerStub.getTrackerStub();
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
				playerMap = trackerStub.addFirstPlayer(playerName, localPlayerStub);
			} else {

				// Try registering with primary player
				playerMapIterator = playerMap.entrySet().iterator();
				Map.Entry<String, IPlayer> primaryServer = playerMapIterator.next();
				String primaryName = primaryServer.getKey();
				IPlayer primaryStub = primaryServer.getValue();

				try {
					gameState = primaryStub.registerPlayer(requestId, playerName, localPlayerStub);
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
							gameState = backupStub.registerPlayer(requestId, playerName, localPlayerStub);
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
			gameState = new GameState(trackerStub.getMazeDimensions());
			playerMap = new LinkedHashMap<>();
			playerMap.put(playerName, localPlayerStub);
			gameState.setPlayerMap(playerMap);
			gameState.addPlayer(playerName);
		}
		localPlayerStub.setGameState(gameState);
	}

	/**
	 * 
	 * @param playerName
	 * @param localPlayerStub
	 */
	public static boolean deregister(String playerName, IPlayer localPlayerStub) {
		UUID requestId = UUID.randomUUID();
		try {
			IPlayer primary = localPlayerStub.getPrimaryServer();
			GameState gameState = primary.deregisterPlayer(requestId, playerName);
			localPlayerStub.setGameState(gameState);
		} catch (RemoteException e) {
			try {
				IPlayer backup = localPlayerStub.getBackupServer();
				GameState gameState = backup.deregisterPlayer(requestId, playerName);
				localPlayerStub.setGameState(gameState);
			} catch (RemoteException e1) {
				return false;
			}
		}
		return true;
	}

}
