import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
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
	 * This method will be called,
	 * 
	 * i) When this player is being promoted to a server and the server wants to set
	 * the latest game state ii) After any action, setting the gamestate returned by
	 * the server
	 * 
	 * TODO: Calling this function can in turn affect the
	 * 
	 * a) UI. So, Redraw the UI as soon as this function is complete. b)
	 * Communication State. Update Ping player and PServer and BServer.
	 *
	 * @param gameState
	 */
	void setGameState(GameState gameState) throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	GameState getGameState() throws RemoteException;

	/**
	 * This method will be called,
	 * 
	 * i) When updating playerMap (no affect on maze related state) ii) when server
	 * updates the ping topology. (Update Ping player and PServer and BServer.)
	 * 
	 *
	 * @param playerMap
	 */
	void setPlayerMap(LinkedHashMap<String, IPlayer> playerMap) throws RemoteException;

	/**
	 * Update playerMap using Tracker. This function is used when the player has
	 * been idle for quite a long time, and the Primary,Backup server (have crashed)
	 * information is too old to be true.
	 * 
	 * @throws RemoteException
	 */
	void updatePlayerMap() throws RemoteException;

	/**
	 *
	 * @throws RemoteException
	 */
	boolean respondToPing() throws RemoteException;

	/**
	 *
	 * @throws RemoteException
	 */
	String getPingPlayerName() throws RemoteException;

	/**
	 *
	 * @throws RemoteException
	 */
	IPlayer getPingPlayer() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	IPlayer getPrimaryServer() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	IPlayer getBackupServer() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	boolean isPrimary() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	boolean isBackup() throws RemoteException;

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
