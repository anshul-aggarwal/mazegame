import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class PingUtil extends Thread {

	//This class handles the pinging between players
	IPlayer player;

	public PingUtil (IPlayer player) {
		this.player = player;
	}
	
	public void run() {
		try {
			while(true)
			{
				pingNextPlayer(player.getPingPlayer());
				Thread.sleep(500);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pingNextPlayer(IPlayer pingPlayer) throws RemoteException {
		try {
			if (pingPlayer.respondToPing())
			{
				//System.out.println("Successfully received a response to the ping. The next player is active.");
			}
		}
		catch (RemoteException e)
		{
			System.out.println("Ping Failed. The next player is dead.");
			//informServer
		}
		catch (Exception f)
		{
			System.out.println("Ping failed");
		}
	}

}
