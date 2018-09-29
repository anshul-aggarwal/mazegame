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

			ITracker trackerStub = server.getTrackerStub();
			IPlayer backupServerStub = server.getBackupServer();

			// Registering with tracker and Primary Server
			server.setPlayerMap(trackerStub.addPlayer(playerName, playerStub));
			server.getGameState().addPlayer(playerName);

			DebugUtil.printPlayers(server, "Called from ServerRequestHandlerUtil#register");

			// Maintaining a copy with backup server
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

		if ((server.isPrimary() || server.isBackup()) && (!completedRequests.contains(requestId))
				&& (server.getGameState().getPlayerMap().containsKey(playerName))) {

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

			// Moving the player
			switch (direction) {
			case "1":
				server.getGameState().movePlayer(playerName, 0, -1);
				System.out.println(playerName + " Moving West");
				break;
			case "2":
				server.getGameState().movePlayer(playerName, 1, 0);
				System.out.println(playerName + " Moving South");
				break;
			case "3":
				server.getGameState().movePlayer(playerName, 0, 1);
				System.out.println(playerName + " Moving East");
				break;
			case "4":
				server.getGameState().movePlayer(playerName, -1, 0);
				System.out.println(playerName + " Moving North");
				break;
			}

			// Maintaining the copy with backup server,
			// FIXME: need optimized mechanism, instead of setting the whole gameState
			// every time
			IPlayer backupServerStub = server.getBackupServer();
			if (backupServerStub != null) {
				backupServerStub.markCompletedRequest(requestId, server.getGameState());
			}

			System.out.println("Player movement complete");
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
