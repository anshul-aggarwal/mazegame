import java.rmi.RemoteException;

public class DebugUtil {

	public static void printPlayers(IPlayer player, String prelimMsg) {
		System.out.println(prelimMsg);
		try {
			System.out.println("Players: " + player.getGameState().getPlayerMap().keySet().toString());
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
	}

}
