import java.rmi.RemoteException;

public class LogUtil {

	public static void printPlayers(IPlayer player, String prelimMsg) {
		System.out.println("[Thread: " + Thread.currentThread().getName() + "]  ," + prelimMsg);
		try {
			System.out.println("[Thread: " + Thread.currentThread().getName() + "]  ,Players: "
					+ player.getGameState().getPlayerMap().keySet().toString());
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
	}

	public static void printMsg(String msg) {
		System.out.println("[Thread: " + Thread.currentThread().getName() + "]  ," + msg);
	}

}
