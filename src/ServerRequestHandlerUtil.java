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

			// Registering with tracker and Primary Server
			ITracker trackerStub = server.getTrackerStub();
			server.setPlayerMap(trackerStub.addPlayer(playerName, playerStub));
			server.getGameState().addPlayer(playerName);

			// Maintaining a copy with backup server
			IPlayer backupServerStub = server.getBackupServer();
			if (backupServerStub != null) {
				backupServerStub.markCompletedRequest(requestId, server.getGameState());
			}

			System.out.println("Successfully registered player: " + playerName);
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

		// Removing player from tracker and server
		ITracker trackerStub = server.getTrackerStub();
		server.setPlayerMap(trackerStub.removePlayer(playerName));
		server.getGameState().removePlayer(playerName);

		// Maintaining a copy with backup server
		IPlayer backupServerStub = server.getBackupServer();
		if (backupServerStub != null) {
			backupServerStub.markCompletedRequest(requestId, server.getGameState());
		}

		System.out.println("Successfully Removed Player: " + playerName);
	}

	/**
	 * 
	 * @param requestId
	 * @param server
	 * @param playerName
	 * @param direction
	 */
	public static void movePlayer(UUID requestId, Player server, String playerName, String direction) {
		switch (direction) {
			case "1":
				server.getGameState().movePlayer(playerName, 0, -1);
				System.out.println("Moved West");
				break;
			case "2":
				server.getGameState().movePlayer(playerName, 1, 0);
				System.out.println("Moved South");
				break;
			case "3":
				server.getGameState().movePlayer(playerName, 0, 1);
				System.out.println("Moved East");
				break;
			case "4":
				server.getGameState().movePlayer(playerName, -1, 0);
				System.out.println("Moved North");
				break;
		}
	}

	/**
	 * 
	 * @param requestId
	 * @return
	 */
	public static boolean markCompletedRequest(UUID requestId) {
		return completedRequests.add(requestId);
	}
}
