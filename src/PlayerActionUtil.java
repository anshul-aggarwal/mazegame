import java.util.UUID;
import java.rmi.RemoteException;

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
	 */
	public static void move(Player player, String direction) {
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
	 */
	public static void refresh(Player player) {
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
