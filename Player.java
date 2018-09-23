import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player implements IPlayer, Serializable {

    private final String playerName;
    private final ITracker tracker;
    private ArrayList<IPlayer> players;		//Replaced Sset with ArrayList to make indexed access
    private IPlayer pingPlayer;
    private boolean isPrimaryServer;
    private boolean isBackupServer;
    
    
    public Player(String name, ITracker tracker) {
        this.playerName = name;
        this.tracker = tracker;
    }

    @Override
    public String getPlayerName() {
        System.out.println("\nHello, I am " + playerName + ". My function getPlayerName was called.");
        return playerName;
    }

    @Override
    public void addPlayers(ArrayList<IPlayer> players) {
        if (this.players == null) {
            this.players = new ArrayList<>();
        }
        this.players.addAll(players);
        try {
			this.setPingTarget();
		} catch (RemoteException e) {
			System.out.println("Adding ping target failed");
			e.printStackTrace();
		}
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
    public void pingNextPlayer() throws RemoteException {
        try {
	    	if (pingPlayer.respondToPing())
	        {
	        	System.out.println("Successfully received a response to the ping. The next player is active.");
	        }
        }
        catch (RemoteException e)
        {
        	System.out.println("Ping Failed. The next player is dead.");
        	//informServer
        }
    }
    
    @Override
    public boolean respondToPing() throws RemoteException {
    	System.out.println("I was pinged.");
        return true;
    }
    
    @Override
    public void setPingTarget() throws RemoteException {
    	//Temporary - sets the previous as the new pingPlayer. The first player does not get a player to ping at the moment.
    	
    	int playerCount = players.size();
    	if (playerCount > 1) {
    		this.pingPlayer = players.get(playerCount - 2);		//The previous player
    	}
    	
    }

    @Override
    public ITracker getTracker() {
        return tracker;
    }

    @Override
    public boolean equals(Object o) {
        try {
            if (o != null && (o instanceof IPlayer) && this.playerName.equals(((IPlayer) o).getPlayerName())) {
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }


}
