import java.rmi.RemoteException;
import java.util.UUID;

/**
 * This utility is meant for handling all Player actions (except
 * register,de-register)
 *
 */
public class PlayerActionUtil {

	/**
	 * 
	 * @param player
	 * @param direction
	 * @throws RemoteException
	 */
	public static void move(Player player, String direction) throws RemoteException {
		GameState gameState = null;
		boolean completedRequest = true;
		UUID requestId = UUID.randomUUID();

		do {
			completedRequest = true;
			try {
				gameState = player.getPrimaryServer().movePlayer(requestId, player.getPlayerName(), direction);
			} catch (RemoteException e1) {
				try {
					gameState = player.getBackupServer().movePlayer(requestId, player.getPlayerName(), direction);
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

	/**
	 * 
	 * @param player
	 * @throws RemoteException
	 */
	public static void refresh(Player player) throws RemoteException {

		GameState gameState = null;
		boolean completedRequest = true;

		do {
			completedRequest = true;
			try {
				gameState = player.getPrimaryServer().getGameState();
			} catch (RemoteException e1) {
				try {
					gameState = player.getBackupServer().getGameState();
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
