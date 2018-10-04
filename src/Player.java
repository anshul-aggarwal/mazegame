import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Player implements IPlayer, Serializable {

	private static final long serialVersionUID = 8439847363383306568L;

	/*
	 * Non game/communication related states
	 */
	private final String playerName;
	private final ITracker trackerStub;

	/*
	 * Communication States
	 */
	private Map.Entry<String, IPlayer> pingPlayer;
	private Map.Entry<String, IPlayer> primary;
	private Map.Entry<String, IPlayer> backup;

	/*
	 * Game State
	 */
	private GameState gameState;

	/**
	 * Constructor
	 * 
	 * @param playerName
	 * @param trackerStub
	 */
	public Player(String playerName, ITracker trackerStub) {
		this.playerName = playerName;
		this.trackerStub = trackerStub;
	}

	// --- PLAYER METHODS BEGIN ----
	public ITracker getTrackerStub() {
		return trackerStub;
	}

	@Override
	public String getPlayerName() {
		return this.playerName;
	}

	@Override
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		this.updateCommunicationState();
	}

	@Override
	public GameState getGameState() {
		return this.gameState;
	}

	@Override
	public void setPlayerMap(LinkedHashMap<String, IPlayer> playerMap) {
		this.gameState.setPlayerMap(playerMap);
		this.updateCommunicationState();
	}

	@Override
	public void updatePlayerMap() throws RemoteException {
		this.setPlayerMap(trackerStub.getPlayerMap());
	}

	@Override
	public boolean respondToPing() {
		return true;
	}

	public String getPingPlayerName() {
		return this.pingPlayer != null ? this.pingPlayer.getKey() : null;
	}

	public IPlayer getPingPlayer() {
		return this.pingPlayer != null ? this.pingPlayer.getValue() : null;
	}

	public IPlayer getPrimaryServer() {
		return this.primary != null ? this.primary.getValue() : null;
	}

	public IPlayer getBackupServer() {
		return this.backup != null ? this.backup.getValue() : null;
	}

	public String getBackupServerName() {
		return this.backup != null ? this.backup.getKey() : null;
	}

	public boolean isPrimary() {
		return (this.primary != null) && (this.primary.getKey().equals(this.playerName));
	}

	public boolean isBackup() {
		return (this.backup != null) && (this.backup.getKey().equals(this.playerName));
	}

	// --- PLAYER METHODS END ----

	// --- Communication State Update Private Methods Begin ---

	private void updateCommunicationState() {
		this.setPrimaryAndBackupServerState();
		this.setPingTarget();
	}

	private void setPrimaryAndBackupServerState() {
		LinkedHashMap<String, IPlayer> playerMap = this.gameState.getPlayerMap();
		Iterator<Map.Entry<String, IPlayer>> iter = playerMap.entrySet().iterator();
		this.primary = null;
		this.backup = null;

		if (iter.hasNext()) {
			this.primary = iter.next();
		}
		if (iter.hasNext()) {
			this.backup = iter.next();
		}
	}

	/*
	 * 
	 * Set the ping target. This ping target needs to be reset every time the player
	 * who you are pinging crashes or when a new player is added in case you are the
	 * primary server.
	 * 
	 * The ping target initially is the player who joined before you. The player who
	 * joins last will be pinged by the player who is first in the list, i.e. the
	 * primary server.
	 */
	private void setPingTarget() {
		Map.Entry<String, IPlayer> newPingTarget = null;
		LinkedHashMap<String, IPlayer> playerMap = gameState.getPlayerMap();

		if (playerMap.size() > 1) {

			Iterator<Map.Entry<String, IPlayer>> iter = playerMap.entrySet().iterator();
			newPingTarget = iter.next();

			if (newPingTarget.getKey().equals(playerName)) {
				while (iter.hasNext()) {
					newPingTarget = iter.next();
				}
			} else {
				Map.Entry<String, IPlayer> currentPlayer = newPingTarget;
				while (iter.hasNext()) {
					Map.Entry<String, IPlayer> nextPlayer = iter.next();
					if (nextPlayer.getKey().equals(playerName)) {
						newPingTarget = currentPlayer;
						break;
					} else {
						currentPlayer = nextPlayer;
					}
				}
			}

			LogUtil.printMsg("Ping target is " + newPingTarget.getKey());
		}

		this.pingPlayer = newPingTarget;
	}

	// --- Communication State Update Private Methods End ---

	// --- SERVER METHODS BEGIN ---

	@Override
	public GameState registerPlayer(UUID requestID, String name, IPlayer playerStub) throws RemoteException {
		ServerRequestHandlerUtil.registerPlayer(requestID, this, name, playerStub);
		return this.gameState;
	}

	@Override
	public GameState deregisterPlayer(UUID requestId, String name) throws RemoteException {
		ServerRequestHandlerUtil.deregisterPlayer(requestId, this, name);
		return this.gameState;
	}

	@Override
	public GameState movePlayer(UUID requestId, String name, String direction) throws RemoteException {
		ServerRequestHandlerUtil.movePlayer(requestId, this, name, direction);
		return this.gameState;
	}

	@Override
	public boolean markCompletedRequest(UUID requestId, GameState updatedGameState) throws RemoteException {
		boolean markedCompletedRequest = ServerRequestHandlerUtil.markCompletedRequest(requestId);
		if (markedCompletedRequest) {
			this.setGameState(updatedGameState);
		}
		return markedCompletedRequest;
	}

	// --- SERVER METHODS END ---

}
