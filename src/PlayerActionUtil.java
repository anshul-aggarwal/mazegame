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
	public static void move(IPlayer player, String direction) throws RemoteException {
		GameState gameState = null;
		try {
			UUID requestId = UUID.randomUUID();
			gameState = player.getPrimaryServer().movePlayer(requestId, player.getPlayerName(), direction);
		} catch (RemoteException e) {

		}
		if (gameState != null) {
			player.setGameState(gameState);
		}
	}

	/**
	 * 
	 * @param player
	 * @throws RemoteException
	 */
	public static void refresh(IPlayer player) throws RemoteException {
		GameState gameState = null;
		try {
			gameState = player.getPrimaryServer().getGameState();
		} catch (RemoteException e) {

		}
		if (gameState != null) {
			player.setGameState(gameState);
		}
	}
}
