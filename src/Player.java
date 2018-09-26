import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Player implements IPlayer, Serializable {

    private final String playerName;
    private final ITracker trackerStub;
    private IPlayer pingPlayer;

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
        if (this.isPrimary())
        {
        	setPingTarget();
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
    
    @Override
    public void setPingTarget() throws RemoteException {
    	String pingPlayer = null;
    	LinkedHashMap<String,IPlayer> playerMap = gameState.getPlayerMap();
    	
    	int playerCount = playerMap.size();
    	if (playerCount > 1) {
    		
            Iterator<String> iter = playerMap.keySet().iterator();
            
            pingPlayer = iter.next();
            
            if (pingPlayer.equals(playerName)) {
            	while(iter.hasNext())
            	{
            		pingPlayer = iter.next();
            	}
            }
            else {
	            String currentString = pingPlayer;
	            while(iter.hasNext()) {
	            	String nextString = iter.next();
	            	if (nextString.equals(playerName))
	            	{
	            		pingPlayer = currentString;
	            	}
	            	else {
	            		currentString = nextString;
	            	}
	            }
            }
            
            System.out.println("Ping target is " + pingPlayer);
            
    	}
    	else
    	{
    		pingPlayer = playerName;
    	}
    	this.pingPlayer = playerMap.get(pingPlayer);
    	//return this.pingPlayer;
    }
    
    @Override
    public void move(String direction) throws RemoteException {
    	//Contains dummy code for movement. TODO add movement code
    	switch(direction) {
    	case "1": System.out.println("Moved West"); break;
    	case "2": System.out.println("Moved South"); break;
    	case "3": System.out.println("Moved East"); break;
    	case "4": System.out.println("Moved North"); break;
    	}
    }

    private boolean isPrimary() {
        String primary = this.gameState.getPrimaryName();
        return this.playerName.equals(primary);
    }

    private boolean isBackup() {
        String backup = this.gameState.getBackupName();
        return this.playerName.equals(backup);
    }

	@Override
	public void refreshGameState() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGame() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPlayer getPingPlayer() throws RemoteException {
		return this.pingPlayer;
	}

}
