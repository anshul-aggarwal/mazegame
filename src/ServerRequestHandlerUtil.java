import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This utility contains the Server logic to handle all kinds of requests
 *
 */
public class ServerRequestHandlerUtil {

	/*
	 * This set can be kept static since one process is equivalent to one player
	 */
	private static Set<UUID> completedRequests = new HashSet<>();

	/**
	 * 
	 * @param server
	 * @param playerName
	 * @param playerStub
	 * @param requestId
	 * @throws RemoteException
	 */
	public static void registerPlayer(UUID requestId, Player server, String playerName, IPlayer playerStub)
			throws RemoteException {
		if ((server.isPrimary() || server.isBackup()) && (!completedRequests.contains(requestId))) {

			LogUtil.printPlayers(server, "Server: register " + playerName + " [" + requestId + "]");

			// Registering with tracker and Primary Server
			ITracker trackerStub = server.getTrackerStub();
			synchronized (DummyLock.class) {
				server.setPlayerMap(trackerStub.addPlayer(playerName, playerStub));
				server.getGameState().addPlayer(playerName);
				updateBackupServer(requestId, server);
			}

			LogUtil.printMsg("Server: Successfully registered player: " + playerName + " [" + requestId + "]");
		}
	}

	/**
	 * 
	 * @param server
	 * @param playerName
	 * @param requestId
	 * @throws RemoteException
	 */
	public static void deregisterPlayer(UUID requestId, Player server, String playerName) throws RemoteException {

		if ((server.isPrimary() || server.isBackup()) && (!completedRequests.contains(requestId))) {

			LogUtil.printPlayers(server, "Server: #deregisterPlayer " + playerName + " [" + requestId + "]");

			// Removing player from tracker and server
			ITracker trackerStub = server.getTrackerStub();
			synchronized (DummyLock.class) {
				GameState gameState = server.getGameState();
				if (gameState.getPlayerMap().containsKey(playerName)) {
					LogUtil.printMsg("Removing " + playerName);
					server.setPlayerMap(trackerStub.removePlayer(playerName));
					gameState.removePlayer(playerName);
				}
				updateBackupServer(requestId, server);
			}

			LogUtil.printMsg("Server: Successfully Removed Player: " + playerName + " [" + requestId + "]");
		}
	}

	/**
	 * 
	 * @param requestId
	 * @param server
	 * @param playerName
	 * @param direction
	 * @throws RemoteException
	 */
	public static void movePlayer(UUID requestId, Player server, String playerName, String direction)
			throws RemoteException {

		if ((server.isPrimary() || server.isBackup()) && (!completedRequests.contains(requestId))) {

			LogUtil.printPlayers(server, "Server: movePlayer " + playerName + " [" + requestId + "]");

			// Moving the player
			int dY = 0;
			int dX = 0;
			switch (direction) {
			case "1":
				dX = -1;
				// LogUtil.printMsg(playerName + " Moving West");
				break;
			case "2":
				dY = 1;
				// LogUtil.printMsg(playerName + " Moving South");
				break;
			case "3":
				dX = 1;
				// LogUtil.printMsg(playerName + " Moving East");
				break;
			case "4":
				dY = -1;
				// LogUtil.printMsg(playerName + " Moving North");
				break;
			}

			if (dY != 0 || dX != 0) {
				synchronized (DummyLock.class) {
					GameState gameState = server.getGameState();
					if (gameState.getPlayerMap().containsKey(playerName)) {
						gameState.movePlayer(playerName, dY, dX);
					}
					updateBackupServer(requestId, server);
				}
			}

			LogUtil.printMsg("Server: Player movement complete" + " [" + requestId + "]");
		}
	}

	private static void updateBackupServer(UUID requestId, Player server) throws RemoteException {

		boolean updateSuccessful = true;
		IPlayer backupServerStub;
		do {
			String backupServerName = server.getBackupServerName();
			updateSuccessful = true;
			try {
				backupServerStub = server.getBackupServer();
				if (backupServerStub != null) {
					LogUtil.printMsg("Server: Trying to update BS: " + backupServerName + " [" + requestId + "]");
					backupServerStub.markCompletedRequest(requestId, server.getGameState());
				}
			} catch (RemoteException e) {
				/*
				 * FIXME: Needs better handling
				 * 
				 * Handling for when adding a new player, BS goes down i.e. Before pinging
				 * mechanism can detect that BS is down, Server detects this while pushing the
				 * new state
				 */
				LogUtil.printMsg("Server: Removing BS -> " + backupServerName + " : NO RESPONSE");
				server.setPlayerMap(server.getTrackerStub().removePlayer(backupServerName));
				server.getGameState().removePlayer(backupServerName);
				updateSuccessful = false;
			}
		} while (!updateSuccessful);
	}

	/**
	 * 
	 * @param requestId
	 * @return
	 */
	public static boolean markCompletedRequest(UUID requestId) {
		LogUtil.printMsg("Marking Completed Request: " + requestId.toString());
		return completedRequests.add(requestId);
	}
}
