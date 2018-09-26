import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;

public interface ITracker extends Remote {

	/**
	 *
	 * @return
	 * @throws RemoteException
	 */
	LinkedHashMap<String, IPlayer> getPlayerMap() throws RemoteException;

	/**
	 *
	 * @return
	 * @throws RemoteException
	 */
	MazeDimensions getMazeDimensions() throws RemoteException;

	/**
	 *
	 * @param player
	 * @return
	 * @throws RemoteException
	 */
	LinkedHashMap<String, IPlayer> addFirstPlayer(String name, IPlayer player) throws RemoteException;

	/**
	 *
	 * Suggestions:
	 *
	 * For add,
	 *
	 * Check if the caller is present in the player list, only then allow addPlayer
	 *
	 * @param player
	 * @return
	 * @throws RemoteException
	 */
	LinkedHashMap<String, IPlayer> addPlayer(String name, IPlayer player) throws RemoteException;

	/**
	 *
	 * Suggestions:
	 *
	 * For removal,
	 *
	 * Case i) Someone who has not yet joined the game calls remove. In this case,
	 * check the players list size. If its 1, allow! Case ii) For every other case,
	 * just do a contains if the caller is present in the players list, only then
	 * allow!
	 *
	 * @param name
	 * @return
	 * @throws RemoteException
	 */
	LinkedHashMap<String, IPlayer> removePlayer(String name) throws RemoteException;
}
