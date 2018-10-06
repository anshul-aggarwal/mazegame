import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface IPlayer extends Remote {

	// --- PLAYER METHODS BEGIN ----

	/**
	 *
	 * @return
	 * @throws RemoteException
	 */
	ITracker getTrackerStub() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	String getPlayerName() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	GameState getGameState() throws RemoteException;

	/**
	 *
	 * @throws RemoteException
	 */
	boolean respondToPing() throws RemoteException;

	// --- PLAYER METHODS END ----

	// --- SERVER METHODS BEGIN ---

	/**
	 * 
	 * @param requestId
	 * @param name
	 * @param playerStub
	 * @return
	 * @throws RemoteException
	 */
	GameState registerPlayer(UUID requestId, String name, IPlayer playerStub) throws RemoteException;

	/**
	 * 
	 * @param requestId
	 * @param name
	 * @return
	 * @throws RemoteException
	 */
	GameState deregisterPlayer(UUID requestId, String name) throws RemoteException;

	/**
	 * 
	 * @param requestId
	 * @param name
	 * @param direction
	 * @return
	 * @throws RemoteException
	 */
	GameState movePlayer(UUID requestId, String name, String direction) throws RemoteException;

	/**
	 * 
	 * @param requestId
	 * @param updatedGameState
	 * @return
	 * @throws RemoteException
	 */
	boolean markCompletedRequest(UUID requestId, GameState updatedGameState) throws RemoteException;

	// --- SERVER METHODS END ---

}
