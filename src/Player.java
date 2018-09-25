import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Player implements IPlayer, Serializable {

    private final String playerName;
    private final ITracker trackerStub;
    //  private IPlayer pingPlayer;

    private GameState gameState;
    
    public Player(String playerName, ITracker trackerStub) {
        this.playerName = playerName;
        this.trackerStub = trackerStub;
    }

    @Override
    public void setGameState(GameState gameState) throws RemoteException {
        this.gameState = gameState;
    }

    @Override
    public void setPlayerMap(LinkedHashMap<String, IPlayer> playerMap) {
        this.gameState.setPlayerMap(playerMap);
    }
    
    @Override
    public GameState registerPlayer(String name, IPlayer playerStub) throws RemoteException {
        if (this.isPrimary() || this.isBackup()) {
            this.setPlayerMap(trackerStub.addPlayer(name, playerStub));
            System.out.println("Successfully registered player: " + name);
            /*
                TODO: Logic to randomly assign a block in maze
             */
        }
        return this.gameState;
    }
    
    @Override
    public ITracker getTrackerStub() {
        return trackerStub;
    }

    @Override
    public boolean respondToPing() throws RemoteException {
    	System.out.println("I was pinged.");
        return true;
    }

    private boolean isPrimary() {
        String primary = this.gameState.getPrimaryName();
        return this.playerName.equals(primary);
    }

    private boolean isBackup() {
        String backup = this.gameState.getBackupName();
        return this.playerName.equals(backup);
    }

}
