import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class PingUtil {
	
	//This class handles the pinging between players
//
//	    public static void pingNextPlayer(IPlayer pingPlayer) throws RemoteException {
//	        try {
//		    	if (pingPlayer.respondToPing())
//		        {
//		        	System.out.println("Successfully received a response to the ping. The next player is active.");
//		        }
//	        }
//	        catch (RemoteException e)
//	        {
//	        	System.out.println("Ping Failed. The next player is dead.");
//	        	//informServer
//	        }
//	        catch (Exception f)
//	        {
//	        	System.out.println("Ping failed");
//	        }
//	    }
//
//
//	    public static IPlayer setPingTarget(LinkedHashSet<IPlayer> players, String playerName) throws RemoteException {
//	    	IPlayer pingPlayer = null;
//	    	int playerCount = players.size();
//	    	if (playerCount > 1) {
//	    		Iterator<IPlayer> iter = players.iterator();
//	    		IPlayer temp = iter.next();
//	    		while(iter.hasNext()) {
//	    			if (iter.next().getPlayerName().equals(playerName))
//	    			{
//	    				pingPlayer = temp;
//	    				break;
//	    			}
//	    			else {
//	    				temp = iter.next();
//	    			}
//	    		}
//	    	}
//	    	return pingPlayer;
//	    }

}
