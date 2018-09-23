import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Set;

public class Player implements IPlayer, Serializable {

    private final String playerName;
    private final ITracker trackerStub;


    private GameState gameState;

    public Player(String playerName, ITracker trackerStub) {
        this.playerName = playerName;
        this.trackerStub = trackerStub;

    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void setGameState(GameState gameState) throws RemoteException {
        this.gameState = gameState;
    }

    private void setPlayers(Set<IPlayer> players) {
        this.gameState.setPlayers(players);
    }

    @Override
    public GameState registerPlayer(IPlayer playerStub) throws RemoteException {
        if (this.isPrimary() || this.isBackup()) {
            this.setPlayers(trackerStub.addPlayer(playerStub));
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
    public boolean equals(Object o) {
        try {
            if (o != null && (o instanceof IPlayer) && this.playerName.equals(((IPlayer) o).getPlayerName())) {
                return true;
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    private boolean isPrimary() {
        IPlayer primary = this.gameState.getPrimary();
        return primary != null && this.equals(primary);
    }

    private boolean isBackup() {
        IPlayer backup = this.gameState.getBackup();
        return backup != null && this.equals(backup);
    }
}
