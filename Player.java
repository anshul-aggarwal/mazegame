import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class Player implements IPlayer, Serializable {

    private final String playerName;
    private final ITracker trackerStub;
    private LinkedHashSet<IPlayer> players;		//Replaced Set with LinkedHashset to create ordered elements
    private IPlayer pingPlayer;
    
    private GameState gameState;

    
    public Player(String playerName, ITracker trackerStub) {
        this.playerName = playerName;
        this.trackerStub = trackerStub;
    }

    @Override
    public String getPlayerName() {
        //System.out.println("\nHello, I am " + playerName + ". My function getPlayerName was called.");
        return playerName;
    }
    
    @Override
    public void setGameState(GameState gameState) throws RemoteException {
        this.gameState = gameState;
    }
    
    private void setPlayers(LinkedHashSet<IPlayer> players) {
        this.gameState.setPlayers(players);
    }
    
    @Override
    public GameState registerPlayer(IPlayer playerStub) throws RemoteException {
        if (this.isPrimary() || this.isBackup()) {
            this.setPlayers(trackerStub.addPlayer(playerStub));
            System.out.println("Successfully registered player");
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
//    
//    @Override
//    public boolean equals(Object o) {
//        try {
//            if (o != null && (o instanceof IPlayer) && this.playerName.equals(((IPlayer) o).getPlayerName())) {
//                return true;
//            }
//        } catch (RemoteException e) {
//        }
//        return false;
//    }

    @Override
    public void addPlayers(LinkedHashSet<IPlayer> players) {
        if (this.players == null) {
            this.players = new LinkedHashSet<>();
        }
        this.players.addAll(players);
//        try {
//			this.setPingTarget();
//		} catch (RemoteException e) {
//			System.out.println("Adding ping target failed");
//			e.printStackTrace();
//		}
    }

    @Override
    public void printPlayerNames() throws RemoteException {
        if (players != null && !players.isEmpty()) {
            for (IPlayer p: players) {
                System.out.println(p.getPlayerName());
            }
        }
    }
    
    @Override
    public boolean respondToPing() throws RemoteException {
    	System.out.println("I was pinged.");
        return true;
    }

    private boolean isPrimary() {
        IPlayer primary = this.gameState.getPrimary();
        try {
			return primary != null && this.playerName.equals(primary.getPlayerName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }

    private boolean isBackup() {
        IPlayer backup = this.gameState.getBackup();
        return backup != null && this.equals(backup);
    }
//
//    private IPlayer getPingPlayer() {
//    	Iterator<>
//    }

}
