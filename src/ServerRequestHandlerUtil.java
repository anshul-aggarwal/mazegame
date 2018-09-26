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
	public static void registerPlayer(UUID requestId, IPlayer server, String playerName, IPlayer playerStub)
			throws RemoteException {
		if ((server.isPrimary() || server.isBackup()) && (!completedRequests.contains(requestId))) {

			// Registering with tracker and Primary Server
			ITracker trackerStub = server.getTrackerStub();
			server.setPlayerMap(trackerStub.addPlayer(playerName, playerStub));

			/*
			 * TODO: Logic to randomly assign a block in maze
			 */

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
	 */
	public static void deregisterPlayer(UUID requestId, IPlayer server, String playerName) {

	}

	/**
	 * 
	 * @param requestId
	 * @param server
	 * @param playerName
	 * @param direction
	 */
	public static void movePlayer(UUID requestId, IPlayer server, String playerName, String direction) {

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
